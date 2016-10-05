package com.barclaycardus.ccd.processor;

import com.barclaycardus.ccd.config.ConfigurationPropertyHolder;
import com.barclaycardus.ccd.dto.Log;
import com.barclaycardus.ccd.handler.SearchHandler;
import com.barclaycardus.ccd.splitter.LogSplitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by abhishek on 30/04/16.
 */
public class FileProcessor {

    private static final int START_INDEX = 0;
    private static final int MAX_BUFFER_SIZE = 1024*1024;
    private static final int NO_READ_INDICATOR = -1;

    private static final Logger logger = LoggerFactory.getLogger(FileProcessor.class);


    private String oldBufferString = "";
    private int bytesProcessed = 0;


    @Autowired
    LogSplitter logSplitter;

    @Autowired
    ConfigurationPropertyHolder configurationPropertyHolder;

    @Autowired
    SearchHandler searchHandlerChain;


    public void process(String filePath) throws IOException{
        logger.info("Started processing " + filePath);
        InputStream fileStream = null;
        String timestampPattern = null;

        byte[] buffer;
        String bufferString = null;
        int bytesRead = 0;

        fileStream =new FileInputStream(filePath);
        buffer = new byte[MAX_BUFFER_SIZE];
        bytesRead = fileStream.read(buffer, START_INDEX, MAX_BUFFER_SIZE);

        if(bytesRead != NO_READ_INDICATOR) {
            timestampPattern = identifyTimestampPatternForFile(buffer);
        }

        if(timestampPattern != null) {
            do {
                logger.info("Successfully read 1MB data.");

                bufferString = oldBufferString.substring(bytesProcessed).concat(new String(buffer, START_INDEX, MAX_BUFFER_SIZE));
                List<Log> logs = logSplitter.split(normalizeBuffer(bufferString, timestampPattern), timestampPattern, filePath);

                for (Log log : logs) {
                    searchHandlerChain.handle(log);
                }

                oldBufferString = bufferString;
                buffer = new byte[MAX_BUFFER_SIZE];
            } while((bytesRead = fileStream.read(buffer, START_INDEX, MAX_BUFFER_SIZE))
                    != NO_READ_INDICATOR);

            searchHandlerChain.handle(logSplitter.split(oldBufferString.substring(bytesProcessed),timestampPattern,filePath).get(0));
        }

        fileStream.close();
    }

    private String identifyTimestampPatternForFile(byte[] buffer) throws IOException {
        String result = null;
        String bufferString = new String(buffer, START_INDEX, MAX_BUFFER_SIZE);
        for (String patternString : configurationPropertyHolder.getTimestampPatterns()) {
            Pattern pattern = Pattern.compile(patternString);
            Matcher matcher = pattern.matcher(bufferString);
            if (matcher.find()) {
                result = patternString;
                break;
            }
        }

        return result;
    }

    private String normalizeBuffer(String buffer, String timestampPattern) {
        Pattern pattern = Pattern.compile(timestampPattern);
        Matcher matcher = pattern.matcher(buffer);

        int startOfFirstMatch = 0;
        int startOfLastMatch = 0;
        int count = 0;
        while(matcher.find()) {
            if(count == 0) {
                count++;
                startOfFirstMatch = matcher.start();
            }
            startOfLastMatch = matcher.start();
            matcher.end();
        }

        bytesProcessed = startOfLastMatch;
        return buffer.substring(startOfFirstMatch, startOfLastMatch);
    }

}

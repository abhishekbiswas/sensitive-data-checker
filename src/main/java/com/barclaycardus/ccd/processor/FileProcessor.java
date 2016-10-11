package com.barclaycardus.ccd.processor;

import com.barclaycardus.ccd.dto.Log;
import com.barclaycardus.ccd.handler.AccountSearchHandler;
import com.barclaycardus.ccd.handler.SearchHandler;
import com.barclaycardus.ccd.utility.LogSplitter;
import com.barclaycardus.ccd.writer.Writer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by abhishek on 30/04/16.
 */
public class FileProcessor implements Runnable {

    private static final int START_INDEX = 0;
    private static final int MAX_BUFFER_SIZE = 1024*1024;
    private static final int NO_READ_INDICATOR = -1;

    private static final Logger logger = LoggerFactory.getLogger(FileProcessor.class);


    private int id;
    private FileProcessingQueue fileProcessingQueue;
    private List<String> timestampPatterns;
    SearchHandler searchHandlerChain;
    Writer writer;

    private String oldBufferString = "";
    private int bytesProcessed = 0;

    private Thread thread;


    public FileProcessor(int id, FileProcessingQueue fileProcessingQueue, List<String> timestampPatterns, Writer writer) {
        this.id = id;
        this.fileProcessingQueue = fileProcessingQueue;
        this.timestampPatterns = timestampPatterns;
        this.writer = writer;
        SearchHandler accountSearchHandler = AccountSearchHandler.getInstance(writer);
        this.searchHandlerChain = accountSearchHandler;
    }

    public void process() {
        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        File file = fileProcessingQueue.dequeue();

        while(file != null) {
            logger.info("File Processor " + id + ": Started processing " + file.getPath());
            InputStream fileStream = null;
            String timestampPattern = null;

            byte[] buffer;
            String bufferString = null;
            int bytesRead = 0;

            try {
                fileStream = new FileInputStream(file.getPath());
                buffer = new byte[MAX_BUFFER_SIZE];
                bytesRead = fileStream.read(buffer, START_INDEX, MAX_BUFFER_SIZE);

                if (bytesRead != NO_READ_INDICATOR) {
                    timestampPattern = identifyTimestampPatternForFile(buffer);
                }

                if (timestampPattern != null) {
                    do {
                        bufferString = oldBufferString.substring(bytesProcessed).concat(new String(buffer, START_INDEX, MAX_BUFFER_SIZE));
                        List<Log> logs = LogSplitter.split(normalizeBuffer(bufferString, timestampPattern), timestampPattern, file.getPath());

                        for (Log log : logs) {
                            searchHandlerChain.handle(log);
                        }

                        oldBufferString = bufferString;
                        buffer = new byte[MAX_BUFFER_SIZE];
                    } while ((bytesRead = fileStream.read(buffer, START_INDEX, MAX_BUFFER_SIZE)) != NO_READ_INDICATOR);

                    searchHandlerChain.handle(LogSplitter.split(oldBufferString.substring(bytesProcessed), timestampPattern, file.getPath()).get(0));
                }

                fileStream.close();
            } catch(IOException exception) {
                logger.info("File Processor " + id + ": Error occurred processing " + file.getPath());
                logger.info("Exception: " + exception);
            }

            logger.info("File Processor " + id + ": Completed processing " + file.getPath());
            file = fileProcessingQueue.dequeue();

        }
        logger.info("File Processor " + id + ": No more file to process ");
    }

    private String identifyTimestampPatternForFile(byte[] buffer) throws IOException {
        String result = null;
        String bufferString = new String(buffer, START_INDEX, MAX_BUFFER_SIZE);
        for (String patternString : timestampPatterns) {
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

    public Thread getThread() {
        return thread;
    }
}

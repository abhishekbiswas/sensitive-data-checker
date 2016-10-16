package com.barclaycardus.ccd.manager;

import com.barclaycardus.ccd.config.ConfigurationPropertyHolder;
import com.barclaycardus.ccd.dto.ArgumentData;
import com.barclaycardus.ccd.handler.AccountSearchHandler;
import com.barclaycardus.ccd.handler.SearchHandler;
import com.barclaycardus.ccd.parser.ArgumentParser;
import com.barclaycardus.ccd.processor.FileProcessingQueue;
import com.barclaycardus.ccd.processor.FileProcessor;
import com.barclaycardus.ccd.validator.ArgumentValidator;
import com.barclaycardus.ccd.writer.ExcelFileWriter;
import com.barclaycardus.ccd.writer.Writer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by abhishek on 29/04/16.
 */
public class ApplicationManager {

    @Autowired
    private ArgumentParser argumentParser;

    @Autowired
    private ArgumentValidator argumentValidator;

    @Autowired
    private FileProcessingQueue fileProcessingQueue;

    @Autowired
    private ConfigurationPropertyHolder configurationPropertyHolder;

    private static final Logger logger = LoggerFactory.getLogger(ApplicationManager.class);

    private ArgumentData argumentData;

    public void process(String[] arguments) throws IOException, InterruptedException {

        long startTime = System.currentTimeMillis();

        argumentData = argumentParser.parse(arguments);

        try {
            argumentValidator.validate(argumentData);
        } catch (Exception exp) {
            logger.error("Argument data validation failed. Exception: " + exp.getMessage());
            System.exit(0);
        }

        logger.info("Successfully validated arguments");

        File inputFolder = new File(argumentData.getInputFolderPath());

        Collection<File> logFiles = FileUtils.listFiles(inputFolder, new WildcardFileFilter(configurationPropertyHolder.getLogFileFormat()),
                DirectoryFileFilter.DIRECTORY);

        logger.info("Files found for processing: " + logFiles.size());

        fileProcessingQueue.addAll(logFiles);

        List<FileProcessor> fileProcessors= new ArrayList<>();
        Writer writer = ExcelFileWriter.getInstance(configurationPropertyHolder.getOutputFile());
        SearchHandler searchHandlerChain = new AccountSearchHandler(writer);

        for(int i = 0; i < configurationPropertyHolder.getThreadCount(); i++) {
            FileProcessor fileProcessor = new FileProcessor.Builder()
                                                .id(i + 1)
                                                .fileProcessingQueue(fileProcessingQueue)
                                                .timestampPatterns(configurationPropertyHolder.getTimestampPatterns())
                                                .searchHadlerChain(searchHandlerChain)
                                                .build();
            fileProcessors.add(fileProcessor);
            fileProcessor.process();
        }

        for(FileProcessor fileProcessor : fileProcessors) {
            fileProcessor.getThread().join();
        }

        long endTime = System.currentTimeMillis();
        long timeElapsed = endTime - startTime;

        logger.info("Total execution time: " + String.format("%02d:%02d:%02d.%03d", TimeUnit.MILLISECONDS.toHours(timeElapsed),
                                                    TimeUnit.MILLISECONDS.toMinutes(timeElapsed) % TimeUnit.HOURS.toMinutes(1),
                                                    TimeUnit.MILLISECONDS.toSeconds(timeElapsed) % TimeUnit.MINUTES.toSeconds(1),
                                                    timeElapsed % 1000));
        logger.info("Results have been saved at: " + configurationPropertyHolder.getOutputFile());
        logger.info("Success!");
    }

}

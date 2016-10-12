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
        argumentData = argumentParser.parse(arguments);

        try {
            argumentValidator.validate(argumentData);
        } catch (Exception exp) {
            logger.error("Argument data validation failed. Exception: " + exp.getMessage());
            System.exit(0);
        }

        File inputFolder = new File(argumentData.getInputFolderPath());

        Collection<File> logFiles = FileUtils.listFiles(inputFolder, new WildcardFileFilter(configurationPropertyHolder.getLogFileFormat()),
                DirectoryFileFilter.DIRECTORY);

        for(File logFile : logFiles) {
            fileProcessingQueue.enqueue(logFile);
        }

        List<FileProcessor> fileProcessors= new ArrayList<>();
        SearchHandler searchHandlerChain = new AccountSearchHandler(new ExcelFileWriter(configurationPropertyHolder.getOutputFile()));

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

        logger.info("Result has been saved at: " + configurationPropertyHolder.getOutputFile());
        logger.info("Success!");
    }

    public ArgumentData getArgumentData() {
        return argumentData;
    }
}

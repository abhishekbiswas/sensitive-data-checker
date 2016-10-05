package com.barclaycardus.ccd.manager;

import com.barclaycardus.ccd.config.ConfigurationPropertyHolder;
import com.barclaycardus.ccd.dto.ArgumentData;
import com.barclaycardus.ccd.parser.ArgumentParser;
import com.barclaycardus.ccd.processor.FileProcessor;
import com.barclaycardus.ccd.validator.ArgumentValidator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by abhishek on 29/04/16.
 */

public class ApplicationManager {

    @Autowired
    private ArgumentParser argumentParser;

    @Autowired
    private ArgumentValidator argumentValidator;

    @Autowired
    private FileProcessor fileProcessor;

    @Autowired
    private ConfigurationPropertyHolder configurationPropertyHolder;

    private static final Logger logger = LoggerFactory.getLogger(ApplicationManager.class);

    private ArgumentData argumentData;

    public void process(String[] arguments) throws IOException {
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

        try {
            for (File logFile : logFiles) {
                fileProcessor.process(logFile.getPath());
            }
        } catch (Exception exp) {
            logger.error("Exception occured: " + exp.getMessage());
        }

        System.out.println("Success!");
    }

    public ArgumentData getArgumentData() {
        return argumentData;
    }
}

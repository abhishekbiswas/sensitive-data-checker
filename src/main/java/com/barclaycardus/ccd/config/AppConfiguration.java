package com.barclaycardus.ccd.config;

import com.barclaycardus.ccd.manager.ApplicationManager;
import com.barclaycardus.ccd.parser.ArgumentParser;
import com.barclaycardus.ccd.parser.DefaultArgumentParser;
import com.barclaycardus.ccd.processor.FileProcessingQueue;
import com.barclaycardus.ccd.validator.ArgumentValidator;
import com.barclaycardus.ccd.validator.DefaultArgumentValidator;
import com.barclaycardus.ccd.writer.ExcelFileWriter;
import com.barclaycardus.ccd.writer.Writer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.Arrays;

/**
 * Created by abhishek on 29/04/16.
 */


@Configuration
@ComponentScan(value={"com.barclaycardus.ccd"})
@PropertySource("classpath:application.properties")
public class AppConfiguration {


    @Autowired
    private Environment env;


    @Bean
    public ConfigurationPropertyHolder configurationPropertyHolder() {
        return new ConfigurationPropertyHolder.Builder()
                        .logFileFormat(env.getProperty("log.file.format"))
                        .timestampPatterns(Arrays.asList(env.getProperty("timestamp.patterns").split(";")))
                        .threadCount(Integer.parseInt(env.getProperty("thread.count")))
                        .build();
    }

    @Bean
    public ArgumentParser argumentParser() {return new DefaultArgumentParser();}

    @Bean
    public ArgumentValidator argumentValidator() {return new DefaultArgumentValidator();}

    @Bean
    public ApplicationManager applicationManager() {return new ApplicationManager();}

    @Bean
    public FileProcessingQueue fileProcessingQueue() {return new FileProcessingQueue();}

}

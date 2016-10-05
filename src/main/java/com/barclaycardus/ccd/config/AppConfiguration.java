package com.barclaycardus.ccd.config;

import com.barclaycardus.ccd.handler.AccountSearchHandler;
import com.barclaycardus.ccd.handler.SearchHandler;
import com.barclaycardus.ccd.manager.ApplicationManager;
import com.barclaycardus.ccd.parser.ArgumentParser;
import com.barclaycardus.ccd.parser.DefaultArgumentParser;
import com.barclaycardus.ccd.processor.FileProcessor;
import com.barclaycardus.ccd.splitter.DefaultLogSplitter;
import com.barclaycardus.ccd.splitter.LogSplitter;
import com.barclaycardus.ccd.utility.DigitSequenceFinder;
import com.barclaycardus.ccd.validator.ArgumentValidator;
import com.barclaycardus.ccd.validator.DefaultArgumentValidator;
import com.barclaycardus.ccd.writer.FileWriter;
import com.barclaycardus.ccd.writer.Writer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * Created by abhishek on 29/04/16.
 */


@Configuration
@ComponentScan(value={"com.barclaycardus.ccd"})
@PropertySource("classpath:application.properties")
public class AppConfiguration {


    @Autowired
    private Environment env;

//    @Autowired
//    private SearchHandler accountSearchHandler;

    @Bean
    public ConfigurationPropertyHolder configurationPropertyHolder() {
        return new ConfigurationPropertyHolder.Builder()
                .logFileFormat(env.getProperty("log.file.format"))
                .timestampPatterns(env.getProperty("timestamp.patterns"))
                .build();
    }

    @Bean
    public ArgumentParser argumentParser() {return new DefaultArgumentParser();}

    @Bean
    public ArgumentValidator argumentValidator() {return new DefaultArgumentValidator();}

    @Bean
    public ApplicationManager applicationManager() {return new ApplicationManager();}

    @Bean
    public LogSplitter logSplitter() {return new DefaultLogSplitter();}

    @Bean
    public FileProcessor fileProcessor() {return new FileProcessor();}

    @Bean
    public DigitSequenceFinder digitSequenceFinder() {return new DigitSequenceFinder();}

    @Bean
    public Writer writer() {return new FileWriter();}

    @Bean
    public SearchHandler searchHandlerChain() {return new AccountSearchHandler();}

}

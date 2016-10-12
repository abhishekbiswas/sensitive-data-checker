package com.barclaycardus.ccd.parser;

import com.barclaycardus.ccd.dto.ArgumentData;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by abhishek on 29/04/16.
 */
public class DefaultArgumentParser implements ArgumentParser{

    private static final String INPUT_FOLDER_OPTION = "i";
    private static final String INPUT_FOLDER_LONG_OPTION = "infolderpath";

    private static final Logger logger = LoggerFactory.getLogger(DefaultArgumentParser.class);

    @Override
    public ArgumentData parse(String[] arguments) {
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = null;
        Options options = getOptions();

        String inputFolderPath = null;

        try {
            commandLine = parser.parse(options, arguments);
        }
        catch(ParseException exp) {
            logger.error("Incorrect usage. " + exp.getMessage() + ".");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("sensitive-data-checker", options);
            System.exit(0);
        }

        if(commandLine.hasOption(INPUT_FOLDER_OPTION)) {
            inputFolderPath = commandLine.getOptionValue(INPUT_FOLDER_OPTION);
        }

        ArgumentData argumentData = new ArgumentData(inputFolderPath);

        return argumentData;
    }

    private Options getOptions() {

        Option inFolderPath   = Option.builder(INPUT_FOLDER_OPTION)
                .longOpt(INPUT_FOLDER_LONG_OPTION)
                .hasArg()
                .desc("input folder path containing log files")
                .required()
                .build();

        Options options = new Options();

        options.addOption(inFolderPath);

        return options;
    }

}

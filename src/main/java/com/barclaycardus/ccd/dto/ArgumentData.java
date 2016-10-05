package com.barclaycardus.ccd.dto;

/**
 * Created by abiswas on 4/29/2016.
 */
public class ArgumentData {

    private String inputFolderPath;
    private String outputFilePath;

    public ArgumentData(String inputFolderPath, String outputFilePath) {
        this.inputFolderPath = inputFolderPath;
        this.outputFilePath = outputFilePath;
    }

    public String getInputFolderPath() {
        return inputFolderPath;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

}

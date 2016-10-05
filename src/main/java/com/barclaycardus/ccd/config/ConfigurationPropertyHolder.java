package com.barclaycardus.ccd.config;

import java.util.Arrays;
import java.util.List;

/**
 * Created by abhishek on 30/04/16.
 */

public class ConfigurationPropertyHolder {

    private String logFileFormat;
    private List<String> timestampPatterns;

    public String getLogFileFormat() {
        return logFileFormat;
    }

    public void setLogFileFormat(String logFileFormat) {
        this.logFileFormat = logFileFormat;
    }

    public List<String> getTimestampPatterns() {
        return timestampPatterns;
    }

    public void setTimestampPatterns(List<String> timestampPatterns) {
        this.timestampPatterns = timestampPatterns;
    }

    public static class Builder {

        private String logFileFormat;
        private String timestampPatterns;

        public Builder logFileFormat(String logFileFormat) {
            this.logFileFormat = logFileFormat;
            return this;
        }

        public Builder timestampPatterns(String timestampPatterns) {
            this.timestampPatterns = timestampPatterns;
            return this;
        }

        ConfigurationPropertyHolder build() {
            ConfigurationPropertyHolder configurationPropertyHolder = new ConfigurationPropertyHolder();
            configurationPropertyHolder.setLogFileFormat(logFileFormat);
            configurationPropertyHolder.setTimestampPatterns(Arrays.asList(timestampPatterns.split(";")));
            return configurationPropertyHolder;
        }

    }

}

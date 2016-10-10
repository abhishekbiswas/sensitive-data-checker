package com.barclaycardus.ccd.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by abhishek on 30/04/16.
 */

public class ConfigurationPropertyHolder {

    private String logFileFormat;
    private List<String> timestampPatterns;
    private int threadCount;

    public ConfigurationPropertyHolder(Builder builder) {
        logFileFormat = builder.logFileFormat;
        timestampPatterns = builder.timestampPatterns;
        threadCount = builder.threadCount;
    }

    public String getLogFileFormat() {
        return logFileFormat;
    }

    public List<String> getTimestampPatterns() {
        return timestampPatterns;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public static class Builder {

        private String logFileFormat;
        private List<String> timestampPatterns;
        private int threadCount;

        public Builder logFileFormat(String logFileFormat) {
            this.logFileFormat = logFileFormat;
            return this;
        }

        public Builder timestampPatterns(List<String> timestampPatterns) {
            this.timestampPatterns = timestampPatterns;
            return this;
        }

        public Builder threadCount(int threadCount) {
            this.threadCount = threadCount;
            return this;
        }

        ConfigurationPropertyHolder build() {
            return new ConfigurationPropertyHolder(this);
        }

    }

}

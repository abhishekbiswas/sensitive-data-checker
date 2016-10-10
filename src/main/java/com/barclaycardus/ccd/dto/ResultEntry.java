package com.barclaycardus.ccd.dto;


/**
 * Created by abhishek on 02/05/16.
 */
public class ResultEntry {

    private String type;
    private String sensitiveData;
    private String filePath;
    private String loggingTimestamp;
    private String logData;

    public ResultEntry(Builder builder) {
        type = builder.type;
        sensitiveData = builder.sensitiveData;
        filePath = builder.filePath;
        loggingTimestamp = builder.loggingTimestamp;
        logData = builder.logData;
    }

    public String getType() {
        return type;
    }

    public String getSensitiveData() {
        return sensitiveData;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getLoggingTimestamp() {
        return loggingTimestamp;
    }

    public String getLogData() {
        return logData;
    }


    public static class Builder {

        private String type;
        private String sensitiveData;
        private String filePath;
        private String loggingTimestamp;
        private String logData;

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder sensitiveData(String sensitiveData) {
            this.sensitiveData = sensitiveData;
            return this;
        }

        public Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder loggingTimestamp(String loggingTimestamp) {
            this.loggingTimestamp = loggingTimestamp;
            return this;
        }

        public Builder logData(String logData) {
            this.logData = logData;
            return this;
        }

        public ResultEntry build() {
            return new ResultEntry(this);
        }
    }

}

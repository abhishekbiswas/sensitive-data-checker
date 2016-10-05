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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSensitiveData() {
        return sensitiveData;
    }

    public void setSensitiveData(String sensitiveData) {
        this.sensitiveData = sensitiveData;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getLoggingTimestamp() {
        return loggingTimestamp;
    }

    public void setLoggingTimestamp(String loggingTimestamp) {
        this.loggingTimestamp = loggingTimestamp;
    }

    public String getLogData() {
        return logData;
    }

    public void setLogData(String logData) {
        this.logData = logData;
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
            ResultEntry resultEntry = new ResultEntry();
            resultEntry.setType(type);
            resultEntry.setSensitiveData(sensitiveData);
            resultEntry.setFilePath(filePath);
            resultEntry.setLoggingTimestamp(loggingTimestamp);
            resultEntry.setLogData(logData);
            return resultEntry;
        }
    }

}

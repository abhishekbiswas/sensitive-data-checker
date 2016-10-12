package com.barclaycardus.ccd.dto;

/**
 * Created by abhishek on 30/04/16.
 */
public class Log {

    private String timestamp;
    private String details;
    private String filePath;

    public Log(String timestamp, String details, String filePath) {
        this.timestamp = timestamp;
        this.details = details;
        this.filePath = filePath;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getDetails() {
        return details;
    }

    public String getFilePath() {
        return filePath;
    }

}

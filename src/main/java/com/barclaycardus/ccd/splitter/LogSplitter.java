package com.barclaycardus.ccd.splitter;

import com.barclaycardus.ccd.dto.Log;

import java.util.List;

/**
 * Created by abhishek on 30/04/16.
 */
public interface LogSplitter {

    List<Log> split(String buffer, String pattern, String filePath);

}

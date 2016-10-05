package com.barclaycardus.ccd.writer;

import com.barclaycardus.ccd.dto.ResultEntry;

import java.io.IOException;

/**
 * Created by abhishek on 30/04/16.
 */
public interface Writer {

    void write(ResultEntry resultEntry) throws IOException;

}

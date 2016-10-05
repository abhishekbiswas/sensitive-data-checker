package com.barclaycardus.ccd.handler;

import com.barclaycardus.ccd.dto.Log;

import java.io.IOException;

/**
 * Created by abhishek on 02/05/16.
 */
public abstract class SearchHandler {

    protected SearchHandler searchHandler;

    public void setSearchHandler(SearchHandler searchHandler) {
        this.searchHandler = searchHandler;
    }

    public abstract void handle(Log log) throws IOException;
}

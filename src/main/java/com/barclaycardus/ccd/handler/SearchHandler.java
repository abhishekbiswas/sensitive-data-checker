package com.barclaycardus.ccd.handler;

import com.barclaycardus.ccd.dto.Log;
import com.barclaycardus.ccd.dto.ResultEntry;

import java.io.IOException;
import java.util.List;

/**
 * Created by abhishek on 02/05/16.
 */
public abstract class SearchHandler {

    protected SearchHandler searchHandler;

    public SearchHandler getSearchHandler() {
        return searchHandler;
    }

    public void setSearchHandler(SearchHandler searchHandler) {
        this.searchHandler = searchHandler;
    }

    public abstract void handle(Log log) throws IOException;
}

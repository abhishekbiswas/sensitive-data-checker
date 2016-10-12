package com.barclaycardus.ccd.handler;

import com.barclaycardus.ccd.dto.Log;

import java.io.IOException;

/**
 * Created by abhishek on 02/05/16.
 */
public abstract class SearchHandler {

    protected SearchHandler successor;

    public void setSearchHandler(SearchHandler successor) {
        this.successor = successor;
    }

    public abstract void handle(Log log) throws IOException;
}

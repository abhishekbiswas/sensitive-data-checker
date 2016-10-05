package com.barclaycardus.ccd.parser;

import com.barclaycardus.ccd.dto.ArgumentData;

/**
 * Created by abhishek on 29/04/16.
 */
public interface ArgumentParser {

    ArgumentData parse(String[] arguments);

}

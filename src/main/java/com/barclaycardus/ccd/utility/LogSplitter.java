package com.barclaycardus.ccd.utility;

import com.barclaycardus.ccd.dto.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by abhishek on 30/04/16.
 */
public class LogSplitter {

    private LogSplitter() {}

    public static List<Log> split(String buffer, String pattern, String filePath) {
        List<Log> logs  = new ArrayList<>();
        Pattern timestampPattern = Pattern.compile(pattern);
        Matcher matcher = timestampPattern.matcher(buffer);

        int count = 0;
        List<Integer> startIndexes = new ArrayList<>();
        List<Integer> endIndexes = new ArrayList<>();
        while(matcher.find()){
            count++;
            startIndexes.add(matcher.start());
            endIndexes.add(matcher.end());
        }

        for(int i = 0; i < count; i++) {
            String timestamp = buffer.substring(startIndexes.get(i), endIndexes.get(i));
            String details = buffer.substring(endIndexes.get(i), (i == (count - 1)) ? buffer.length() : startIndexes.get(i + 1));
            Log log = new Log(timestamp, details, filePath);
            logs.add(log);
        }

        return logs;
    }

}

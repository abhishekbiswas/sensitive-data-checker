package com.barclaycardus.ccd.processor;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by abhishek on 10/10/16.
 */
public class FileProcessingQueue {

    private Queue<File> queue;

    public FileProcessingQueue() {
        queue = new LinkedList<File>();
    }

    public synchronized boolean enqueue(File file) {
        return queue.offer(file);
    }

    public synchronized File dequeue() {
        return queue.poll();
    }

}

package com.barclaycardus.ccd.processor;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by abhishek on 10/10/16.
 */
public class FileProcessingQueue {

    private Queue<File> queue;

    public FileProcessingQueue() {
        queue = new LinkedList<>();
    }

    public synchronized boolean addAll(Collection<File> collection) {
        return queue.addAll(collection);
    }

    public synchronized boolean enqueue(File file) {
        return queue.offer(file);
    }

    public synchronized File dequeue() {
        return queue.poll();
    }

}

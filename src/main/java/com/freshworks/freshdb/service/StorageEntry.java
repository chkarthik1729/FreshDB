package com.freshworks.freshdb.service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

abstract class StorageEntry {
    private long filePointer;
    private int size;
    private final ReadWriteLock lock;

    StorageEntry(long filePointer, int size) {
        this.filePointer = filePointer;
        this.size = size;
        lock  = new ReentrantReadWriteLock();
    }

    long getFilePointer() {
        return filePointer;
    }

    protected void setFilePointer(long filePointer) {
        this.filePointer = filePointer;
    }

    int getSize() {
        return size;
    }

    protected void setSize(int size) {
        this.size = size;
    }

    Lock getReadLock() {
        return lock.readLock();
    }

    Lock getWriteLock() {
        return lock.writeLock();
    }
}

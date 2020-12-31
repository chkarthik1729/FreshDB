package com.freshworks.freshdb.service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

abstract class StorageEntry {
    private long filePointer;
    private int sizeInBytes;
    private final ReadWriteLock lock;

    StorageEntry(long filePointer, int sizeInBytes) {
        this.filePointer = filePointer;
        this.sizeInBytes = sizeInBytes;
        lock  = new ReentrantReadWriteLock();
    }

    long getFilePointer() {
        return filePointer;
    }

    protected void setFilePointer(long filePointer) {
        this.filePointer = filePointer;
    }

    int getSizeInBytes() {
        return sizeInBytes;
    }

    protected void setSizeInBytes(int sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }

    Lock getReadLock() {
        return lock.readLock();
    }

    Lock getWriteLock() {
        return lock.writeLock();
    }
}

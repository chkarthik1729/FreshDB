package com.freshworks.freshdb.service;

import java.io.RandomAccessFile;

public class StorageManager {

    static StorageManager getInstance(RandomAccessFile file) {
        return null;
    }

    private static final long TOTAL_SPACE = 1024 * 1024 * 1024;
    private long usedSpace = 0;

    long allocate(int noOfBytes) {
        return 0;
    }

    void free(long filePointer) {

    }
}

package com.freshworks.freshdb.service;

abstract class StorageEntry {
    private long filePointer;
    private int size;

    StorageEntry(long filePointer, int size) {
        this.filePointer = filePointer;
        this.size = size;
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
}

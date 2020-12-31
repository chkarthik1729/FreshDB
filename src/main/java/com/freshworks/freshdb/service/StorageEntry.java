package com.freshworks.freshdb.service;

class StorageEntry {
    private long filePointer;
    private int sizeInBytes;
    private boolean isAllocated;

    StorageEntry(long filePointer, int sizeInBytes) {
        this.filePointer = filePointer;
        this.sizeInBytes = sizeInBytes;
    }

    long getFilePointer() {
        return filePointer;
    }

    void setFilePointer(long filePointer) {
        this.filePointer = filePointer;
    }

    int getSizeInBytes() {
        return sizeInBytes;
    }

    boolean isAllocated() {
        return isAllocated;
    }

    void setAllocated(boolean allocated) {
        isAllocated = allocated;
    }

    protected void setSizeInBytes(int sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }
}

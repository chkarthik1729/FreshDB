package com.freshworks.freshdb.service;

public class StorageEntryNode extends StorageEntry {
    private StorageEntryNode prev;
    private StorageEntryNode next;
    private boolean isAllocated;

    public StorageEntryNode(long filePointer, int sizeInBytes, StorageEntryNode prev, StorageEntryNode next) {
        super(filePointer, sizeInBytes);
        this.prev = prev;
        this.next = next;
    }


    StorageEntryNode getPrev() {
        return prev;
    }

    void setPrev(StorageEntryNode prev) {
        this.prev = prev;
    }

    StorageEntryNode getNext() {
        return next;
    }

    void setNext(StorageEntryNode next) {
        this.next = next;
    }

    boolean isAllocated() {
        return isAllocated;
    }

    void setAllocated(boolean allocated) {
        isAllocated = allocated;
    }

    public void setSizeInBytes(int sizeInBytes) {
        super.setSizeInBytes(sizeInBytes);
    }

    public void setFilePointer(long filePointer) {
         super.setFilePointer(filePointer);
    }
}

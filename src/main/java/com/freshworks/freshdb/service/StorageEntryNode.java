package com.freshworks.freshdb.service;

public class StorageEntryNode extends StorageEntry {
    StorageEntryNode prev;
    StorageEntryNode next;

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

    public void setSizeInBytes(int sizeInBytes) {
        super.setSizeInBytes(sizeInBytes);
    }
}

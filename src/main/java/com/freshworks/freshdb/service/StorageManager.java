package com.freshworks.freshdb.service;

import com.freshworks.freshdb.exception.SpaceNotAvailableException;

import java.io.RandomAccessFile;

public class StorageManager {

    private static final int TOTAL_SPACE = 1024 * 1024 * 1024;

    private RandomAccessFile file;
    private DoublyLinkedList storageEntryList;
    private int usedSpace = 0;

    private StorageManager(RandomAccessFile file) {
        this.file = file;
    }

    static StorageManager getInstance(RandomAccessFile file) {
        StorageManager manager = new StorageManager(file);
        manager.storageEntryList = new DoublyLinkedList();
        manager.storageEntryList.addFirst(new StorageEntryNode(0, TOTAL_SPACE, null, null));
        return manager;
    }

    StorageEntry allocate(int noOfBytes) throws SpaceNotAvailableException {
        if (getAvailableStorage() < noOfBytes) {
            throw new SpaceNotAvailableException();
        }
        StorageEntry entry = allocateWithBestFitStrategy(noOfBytes);
        if (entry == null) {
            // TODO: runCompaction();
            entry = allocateWithBestFitStrategy(noOfBytes);
        }
        return entry;
    }

    void free(StorageEntry entry) {
        //
    }

    private StorageEntry allocateWithBestFitStrategy(int noOfBytes) {
        StorageEntryNode bestFitNode = getBestFitNode(noOfBytes);
        if (bestFitNode == null) return null;

        int extraSpace = bestFitNode.getSizeInBytes() - noOfBytes;
        if (extraSpace > 0) {
            StorageEntryNode extraNode = new StorageEntryNode(bestFitNode.getFilePointer() + noOfBytes,
                    extraSpace, null, null);
            storageEntryList.addAfter(bestFitNode, extraNode);
        }
        bestFitNode.setAllocated(true);
        return bestFitNode;
    }

    private StorageEntryNode getBestFitNode(int noOfBytes) {
        StorageEntryNode curr = storageEntryList.getHead();
        StorageEntryNode bestFitNode = null;

        while (curr != null) {
            if (curr.isAllocated()) {
                curr = curr.getNext();
                continue;
            }

            if (curr.getSizeInBytes() >= noOfBytes) {
                if (bestFitNode == null) bestFitNode = curr;
                else if (bestFitNode.getSizeInBytes() > curr.getSizeInBytes()) bestFitNode = curr;
            }
            curr = curr.getNext();
        }

        return bestFitNode;
    }

    int getAvailableStorage() {
        return TOTAL_SPACE - usedSpace;
    }
}

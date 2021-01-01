package com.freshworks.freshdb.service;

import com.freshworks.freshdb.exception.SpaceNotAvailableException;

import java.io.IOException;
import java.io.RandomAccessFile;

public class StorageManager {

    private static final int TOTAL_SPACE = 1024 * 1024 * 1024;  // 1 GB

    private final RandomAccessFile file;
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

    StorageEntry allocate(int noOfBytes) throws SpaceNotAvailableException, IOException {
        if (getAvailableStorage() < noOfBytes) {
            throw new SpaceNotAvailableException();
        }
        StorageEntry entry = allocateWithBestFitStrategy(noOfBytes);
        if (entry == null) {
            runCompaction();
            entry = allocateWithBestFitStrategy(noOfBytes);
        }
        usedSpace += noOfBytes;
        return entry;
    }

    private void runCompaction() throws IOException {
        StorageEntryNode curr = storageEntryList.getHead();
        while (curr != null && curr.getNext() != null) {
            StorageEntryNode next = curr.getNext();
            // Bring next forward
            if (!curr.isAllocated()) {
                file.seek(next.getFilePointer());
                String contents = file.readUTF();
                file.seek(curr.getFilePointer());
                file.writeUTF(contents);

                int totalSize = curr.getSize() + next.getSize();
                curr.setSize(next.getSize());
                next.setSize(totalSize - curr.getSize());

                next.setFilePointer(curr.getFilePointer() + curr.getSize());
            }
            curr = next;
        }
    }

    void free(StorageEntry entry) {
        StorageEntryNode entryNode = (StorageEntryNode) entry;
        entryNode.setAllocated(false);
        mergePrevNode(entryNode);
        mergeNextNode(entryNode);
    }

    private void mergePrevNode(StorageEntryNode entryNode) {
        StorageEntryNode prev = entryNode.getPrev();
        if (prev != null && !prev.isAllocated()) {
            entryNode.setFilePointer(prev.getFilePointer());
            entryNode.setSize(entryNode.getSize() + prev.getSize());
            storageEntryList.remove(prev);
        }
    }

    private void mergeNextNode(StorageEntryNode entryNode) {
        StorageEntryNode next = entryNode.getNext();
        if (next != null && !next.isAllocated()) {
            entryNode.setSize(entryNode.getSize() + next.getSize());
            storageEntryList.remove(next);
        }
    }

    private StorageEntry allocateWithBestFitStrategy(int noOfBytes) {
        StorageEntryNode bestFitNode = getBestFitNode(noOfBytes);
        if (bestFitNode == null) return null;

        int extraSpace = bestFitNode.getSize() - noOfBytes;
        if (extraSpace > 0) {
            StorageEntryNode extraNode = new StorageEntryNode(bestFitNode.getFilePointer() + noOfBytes,
                    extraSpace, null, null);
            bestFitNode.setSize(noOfBytes);
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

            if (curr.getSize() >= noOfBytes) {
                if (bestFitNode == null) bestFitNode = curr;
                else if (bestFitNode.getSize() > curr.getSize()) bestFitNode = curr;
            }
            curr = curr.getNext();
        }

        return bestFitNode;
    }

    int getAvailableStorage() {
        return TOTAL_SPACE - usedSpace;
    }
}

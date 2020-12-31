package com.freshworks.freshdb.service;

public class DoublyLinkedList {
    private StorageEntryNode head;
    private StorageEntryNode tail;
    private int size;

    DoublyLinkedList() {
        this.head = this.tail = null;
        size = 0;
    }

    void addFirst(StorageEntryNode e) {

    }

    void addLast(StorageEntryNode e) {

    }

    void addAfter(StorageEntryNode e, StorageEntryNode node) {

    }

    void remove(StorageEntryNode e) {

    }

    StorageEntryNode getHead() {
        return head;
    }

    int size() {
        return size;
    }

    boolean isEmpty() {
        return size == 0;
    }
}

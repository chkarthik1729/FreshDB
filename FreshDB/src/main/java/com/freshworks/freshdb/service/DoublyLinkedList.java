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
        assert e != null;

        if (head == null) {
            head = tail = e;
        } else {
            head.setPrev(e);
            e.setNext(head);
            head = e;
        }
        size++;
    }

    void addLast(StorageEntryNode e) {
        assert e != null;

        if (tail == null) {
            head = tail = e;
        } else {
            tail.setNext(e);
            e.setPrev(tail);
            tail = e;
        }
        size++;
    }

    void addAfter(StorageEntryNode e, StorageEntryNode node) {
        assert e != null;
        if (e.getNext() == null) addLast(node);
        else {
            node.setPrev(e);
            node.setNext(e.getNext());
            e.setNext(node);
            size++;
        }
    }

    void remove(StorageEntryNode e) {
        if (e.getPrev() == null) removeHead();
        else if (e.getNext() == null) removeTail();
        else {
            e.getPrev().setNext(e.getNext());
            e.getNext().setPrev(e.getPrev());
            size--;
        }
    }

    void removeHead() {
        assert head != null;
        head = head.getNext();
        head.setPrev(null);
        size--;
    }

    void removeTail() {
        assert tail != null;
        tail = tail.getPrev();
        tail.setNext(null);
        size--;
    }

    StorageEntryNode getHead() {
        return head;
    }

    int size() {
        return size;
    }
}

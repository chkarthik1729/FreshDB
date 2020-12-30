package com.freshworks.freshdb.service;

public class KeyMeta {
    private final String key;
    private final long expiresAt;
    private final long filePointer;

    private KeyMeta(String key, long filePointer, long expiresAt) {
        this.key = key;
        this.expiresAt = expiresAt;
        this.filePointer = filePointer;
    }

    public static KeyMeta from(String key, long filePointer, long expiresAt) {
        return new KeyMeta(key, filePointer, expiresAt);
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiresAt;
    }

    public String getKey() {
        return key;
    }

    public long getFilePointer() {
        return filePointer;
    }

    public long expiresAt() {
        return expiresAt;
    }
}

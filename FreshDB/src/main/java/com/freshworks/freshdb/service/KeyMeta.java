package com.freshworks.freshdb.service;

public class KeyMeta {
    private final String key;
    private final long expiresAt;
    private final StorageEntry storageEntry;

    private KeyMeta(String key, StorageEntry storageEntry, long expiresAt) {
        this.key = key;
        this.expiresAt = expiresAt;
        this.storageEntry = storageEntry;
    }

    public static KeyMeta from(String key, StorageEntry storageEntry, long expiresAt) {
        return new KeyMeta(key, storageEntry, expiresAt);
    }

    public boolean isExpired() {
        return expiresAt != -1 && System.currentTimeMillis() > expiresAt;
    }

    public String getKey() {
        return key;
    }

    public StorageEntry getStorageEntry() {
        return storageEntry;
    }

    public long expiresAt() {
        return expiresAt;
    }
}

package com.freshworks.freshdb.service;

import com.freshworks.freshdb.KeyStore;
import com.freshworks.freshdb.KeyStoreException;
import com.freshworks.freshdb.exception.*;
import com.google.common.base.Utf8;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FreshDBStore implements KeyStore {

    private static final int MAX_KEY_LENGTH = 32;
    private static final int MAX_VALUE_SIZE = 16 * 1024;

    private final RandomAccessFile valuesFile;
    private final Map<String, KeyMeta> keyToMetaMap = new ConcurrentHashMap<>();
    private final StorageManager storageManager;
    private final SortedSet<KeyMeta> keysMetaWithExpiry =
            Collections.synchronizedSortedSet(new TreeSet<>(new CompareExpiryOfMeta()));

    public FreshDBStore() throws FileNotFoundException {
        this(null);
    }

    public FreshDBStore(String fileName) throws FileNotFoundException {
        String valuesFileName = fileName == null ? ("./FreshDB@" + hashCode()) : fileName;
        valuesFile = new RandomAccessFile(new File(valuesFileName), "rwd");
        storageManager = StorageManager.getInstance(valuesFile);
    }

    @Override
    public void create(String key, String value) throws KeyStoreException, IOException {
        create(key, value, Long.MAX_VALUE);
    }

    @Override
    public void create(String key, String value, long ttlInSeconds) throws KeyStoreException, IOException {
        deleteExpiredKeys();
        validateSizeConstraints(key, value);
        ensureKeyDoesNotExist(key);
        StorageEntry entry = storageManager.allocate(Utf8.encodedLength(value) + 2);
        KeyMeta meta = KeyMeta.from(key, entry, System.currentTimeMillis() + ttlInSeconds * 1000);
        keyToMetaMap.put(key, meta);
        writeValue(entry.getFilePointer(), value);
        keysMetaWithExpiry.add(meta);
    }

    @Override
    public void delete(String key) throws KeyStoreException {
        KeyMeta meta = keyToMetaMap.remove(key);
        if (meta == null) throw new KeyNotFoundException();
        storageManager.free(meta.getStorageEntry());
    }

    @Override
    public String read(String key) throws IOException, KeyStoreException {
        KeyMeta meta = keyToMetaMap.get(key);
        if (meta == null) throw new KeyNotFoundException();
        if (meta.isExpired()) {
            delete(key);
            throw new KeyNotFoundException();
        }
        return readValue(meta.getStorageEntry().getFilePointer());
    }

    private void validateSizeConstraints(String key, String value) throws KeyStoreException {
        if (key.length() > MAX_KEY_LENGTH) throw new KeyTooLargeException();
        if (value.getBytes(StandardCharsets.UTF_8).length > MAX_VALUE_SIZE) throw new ValueTooLargeException();
    }

    private void ensureKeyDoesNotExist(String key) throws KeyStoreException {
        if (keyToMetaMap.containsKey(key)) {
            KeyMeta previousKey = keyToMetaMap.get(key);
            if (previousKey.isExpired()) {
                delete(key);
            }
            else throw new KeyAlreadyExistsException();
        }
    }

    private String readValue(long filePointer) throws IOException {
        valuesFile.seek(filePointer);
        return valuesFile.readUTF();
    }

    private void writeValue(long filePointer, String value) throws IOException {
        valuesFile.seek(filePointer);
        valuesFile.writeUTF(value);
    }

    private void deleteExpiredKeys() throws KeyStoreException {
        KeyMeta curr;
        while ((curr = keysMetaWithExpiry.first()).isExpired()) {
            delete(curr.getKey());
            keysMetaWithExpiry.remove(curr);
        }
    }

    private static class CompareExpiryOfMeta implements Comparator<KeyMeta> {
        @Override
        public int compare(KeyMeta o1, KeyMeta o2) {
            return (int) (o1.expiresAt() - o2.expiresAt());
        }
    }
}
package com.freshworks.freshdb.test;

import com.freshworks.freshdb.KeyStore;
import com.freshworks.freshdb.KeyStoreException;
import com.freshworks.freshdb.exception.*;
import com.freshworks.freshdb.service.FreshDBStore;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

public class FreshDBStoreTest {

    private static KeyStore keyStore;

    @BeforeAll
    public static void getKeyStoreInstance() throws FileNotFoundException {
        keyStore = new FreshDBStore();
    }

    @BeforeEach
    public void refreshDataStore() throws FileNotFoundException {
        File file = new File("./DBFiles/");
        assert(file.isDirectory());
        for (File dbFile : file.listFiles()) {
            dbFile.delete();
        }
        keyStore = new FreshDBStore();
    }

    @Test
    public void testCreateAndRead() throws IOException, KeyStoreException {
        String value = "value-1";
        keyStore.create("key-1", value);
        assertEquals(value, keyStore.read("key-1"));
    }

    @Test
    public void testCreateAndDelete() throws IOException, KeyStoreException {
        keyStore.create("key-2", "value-2");
        assertEquals("value-2", keyStore.read("key-2"));
        keyStore.delete("key-2");
        assertThrows(KeyNotFoundException.class,
                () -> keyStore.read("key-2"));
    }

    @Test
    public void testReadUnavailableKey() {
        assertThrows(KeyNotFoundException.class,
                () -> keyStore.read("unavailable-key"));
    }

    @Test
    public void testCreateSameKeyAgain() throws IOException, KeyStoreException {
        keyStore.create("key-3", "value-3");
        assertThrows(KeyAlreadyExistsException.class,
                () -> keyStore.create("key-3", "value-3"));
    }

    @Test
    public void testReadKeyAfterTTL() throws IOException, KeyStoreException, InterruptedException {
        keyStore.create("key-4", "value-4", 3);
        Thread.sleep(3000);
        assertThrows(KeyNotFoundException.class,
                () -> keyStore.read("key-4"));
    }

    @Test
    public void testCreatingSameKeyAfterTTL() throws IOException, KeyStoreException, InterruptedException {
        keyStore.create("key-5", "value-5", 3);
        Thread.sleep(3000);
        Assertions.assertDoesNotThrow(() -> keyStore.create("key-5", "value-5"));
        assertEquals("value-5", keyStore.read("key-5"));
    }

    @Test
    public void testCreateLargeKey() {
        String key = "k".repeat(33);
        assertThrows(KeyTooLargeException.class,
                () -> keyStore.create(key, "largeKey-value"));
    }

    @Test
    public void testCreateLargeValue() {
        char[] value = new char[(16 * 1024) + 1];
        Arrays.fill(value, 'v');
        String largeValue = new String(value);

        assertThrows(ValueTooLargeException.class,
                () -> keyStore.create("largeValue-key", largeValue));
    }

    @Test
    public void testStorageLimit() throws FileNotFoundException {
        char[] value = new char[16 * 1024];
        Arrays.fill(value, 'v');
        String _16KValue = new String(value);
        assertDoesNotThrow(() -> createMultipleKeysWithValue(0, 100, _16KValue));
        refreshDataStore();
        assertDoesNotThrow(() -> createMultipleKeysWithValue(0, 1000, _16KValue));
        refreshDataStore();
        assertDoesNotThrow(() -> createMultipleKeysWithValue(0, 10_000, _16KValue));
        refreshDataStore();
        // 1GB Storage -> (1024 * 1024) KB; (1024 * 1024) / 16 = 65,536 keys
        assertThrows(SpaceNotAvailableException.class, () -> createMultipleKeysWithValue(0, 100_000, _16KValue));
    }

    @Test
    public void testCompaction() {
        char[] value = new char[16 * 1024];
        Arrays.fill(value, 'v');
        String _16KValue = new String(value);
        // 1GB Storage -> (1024 * 1024) KB; (1024 * 1024) / 16 = 65,536 keys
        assertDoesNotThrow(() -> createMultipleKeysWithValue(0, 60_000, _16KValue));
        deleteMultipleRandomKeysWithIn(10000, 60_000);
        assertDoesNotThrow(() -> createMultipleKeysWithValue(60_000, 5_000, _16KValue));
    }

    static void createMultipleKeysWithValue(int keyOffset, int noOfKeys, String value) throws IOException, KeyStoreException {
        for (int i = 0; i < noOfKeys; i++) {
            String key = "key-" + (i + keyOffset);
            keyStore.create(key, value);
        }
    }

    static void deleteMultipleRandomKeysWithIn(int noOfKeys, int withIn) {
        Random random = new Random();
        IntStream.of(noOfKeys).forEach((keyNo) -> {
            try {
                String key = "key-" + random.nextInt(withIn);
                keyStore.delete(key);
            } catch (KeyStoreException ignore) {
            }
        });
    }
}

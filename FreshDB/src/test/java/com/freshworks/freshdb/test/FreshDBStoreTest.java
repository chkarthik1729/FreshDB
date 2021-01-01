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
import java.io.IOException;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class FreshDBStoreTest {

    private static KeyStore keyStore;

    @BeforeAll
    public static void getKeyStoreInstance() throws IOException {
        keyStore = new FreshDBStore();
    }

    @BeforeEach
    public void refreshDataStore() throws IOException {
        File file = new File("./DBFiles/");
        if (!file.exists()) file.mkdir();
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

    // Heavy test. Would take forever. Run if you really want to see the results
    /*
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
        assertThrows(SpaceNotAvailableException.class,
                () -> createMultipleKeysWithValue(0, 100_000, _16KValue));
    }
     */



    // Heavy test. Would take forever. Run if you really want to see the results
    /*
    @Test
    public void testCompaction() {
        String _10KValue = "v".repeat(10 * 1024);
        String _16KValue = "v".repeat(16 * 1024);
        // db can store 104837 10KB values
        assertDoesNotThrow(() -> createMultipleKeysWithValue(0, 104_837, _10KValue));
        deleteMultipleRandomKeysWithIn(10_000, 60_000);
        assertDoesNotThrow(() -> createMultipleKeysWithValue(104_837, 6_250, _16KValue));
    }
    */

    static void createMultipleKeysWithValue(int keyOffset, int noOfKeys, String value) throws IOException, KeyStoreException {
        for (int i = 0; i < noOfKeys; i++) {
            String key = "key-" + (i + keyOffset);
            System.out.println("Creating " + key);
            keyStore.create(key, value);
        }
    }

    static void deleteMultipleRandomKeysWithIn(int noOfKeys, int withIn) {
        Random random = new Random();
        for (int i = 0; i < noOfKeys; i++) {
            try {
                String key = "key-" + random.nextInt(withIn);
                System.out.println("Deleting " + key);
                keyStore.delete(key);
            } catch (KeyStoreException e) {
                i--;
            }
        }
    }
}

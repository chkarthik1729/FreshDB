package com.freshworks.freshdb;

import com.freshworks.freshdb.exception.*;

import java.io.Closeable;
import java.io.IOException;

public interface KeyStore extends Closeable {

    /**
     * Stores the value for the key
     * @param key
     * @param value
     * @throws IOException if there is any error with the db file
     * @throws KeyStoreException
     */
    void create(String key, String value) throws KeyStoreException, IOException;

    /**
     * Stores the value for the key until ttl seconds
     * @param key
     * @param value
     * @param ttl
     * @throws KeyStoreException
     * @throws IOException
     */
    void create(String key, String value, long ttl) throws KeyStoreException, IOException;

    /**
     * Deletes the key and the value associated with it
     * @param key
     * @throws KeyStoreException
     */
    void delete(String key) throws KeyStoreException;

    /**
     * Gets the value associated with the key
     * @param key
     * @return
     * @throws IOException
     * @throws KeyNotFoundException
     */
    String read(String key) throws IOException, KeyStoreException;
}

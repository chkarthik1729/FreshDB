package com.freshworks.freshdb;

import com.freshworks.freshdb.exception.KeyNotFoundException;

import java.io.IOException;

public interface KeyStore {

    /**
     *
     * @param key
     * @param value
     * @throws KeyStoreException
     * @throws IOException
     */
    void create(String key, String value) throws KeyStoreException, IOException;

    /**
     *
     * @param key
     * @param value
     * @param ttl
     * @throws KeyStoreException
     * @throws IOException
     */
    void create(String key, String value, long ttl) throws KeyStoreException, IOException;

    /**
     *
     * @param key
     * @throws KeyStoreException
     */
    void delete(String key) throws KeyStoreException;

    /**
     *
     * @param ket
     * @return
     * @throws IOException
     * @throws KeyNotFoundException
     */
    String read(String ket) throws IOException, KeyStoreException;
}

package com.freshworks.freshdb.exception;

import com.freshworks.freshdb.KeyStoreException;

public class KeyNotFoundException extends KeyStoreException {

    public KeyNotFoundException() {
        super("Key not found in the datastore");
    }
}

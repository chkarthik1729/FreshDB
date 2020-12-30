package com.freshworks.freshdb.exception;

import com.freshworks.freshdb.KeyStoreException;

public class KeyTooLargeException extends KeyStoreException {

    public KeyTooLargeException() {
        super("Specified key is too large to hold in the datastore");
    }
}

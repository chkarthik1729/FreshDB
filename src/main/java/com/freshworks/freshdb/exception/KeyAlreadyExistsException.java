package com.freshworks.freshdb.exception;

import com.freshworks.freshdb.KeyStoreException;

public class KeyAlreadyExistsException extends KeyStoreException {

    public KeyAlreadyExistsException() {
        super("KeyMeta is not available in the datastore");
    }
}

package com.freshworks.freshdb.exception;

import com.freshworks.freshdb.KeyStoreException;

public class KeyAlreadyExistsException extends KeyStoreException {

    public KeyAlreadyExistsException() {
        super("The key you are trying to create already exists in the datastore");
    }
}

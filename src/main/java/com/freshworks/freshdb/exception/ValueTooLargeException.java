package com.freshworks.freshdb.exception;

import com.freshworks.freshdb.KeyStoreException;

public class ValueTooLargeException extends KeyStoreException {

    public ValueTooLargeException() {
        super("Value is too large to hold in the datastore");
    }
}

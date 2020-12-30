package com.freshworks.freshdb.exception;

import com.freshworks.freshdb.KeyStoreException;

public class SpaceNotAvailableException extends KeyStoreException {

    public SpaceNotAvailableException() {
        super("Not enough space to hold new keys in the datastore");
    }
}

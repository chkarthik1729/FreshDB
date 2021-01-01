package com.freshworks.freshdb.test;

import com.freshworks.freshdb.KeyStore;
import com.freshworks.freshdb.KeyStoreException;
import com.freshworks.freshdb.service.FreshDBStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class FreshDBPerformanceTest {
    private static KeyStore keyStore;

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
    public void testCreation() throws InvocationTargetException, IllegalAccessException, FileNotFoundException {
        Class<?> performanceClass = this.getClass();
        Method[] methods = performanceClass.getDeclaredMethods();
        for (Method method : methods) {
            if (Modifier.isStatic(method.getModifiers())) {
                long startTime = System.nanoTime();
                method.invoke(this);
                long endTime = System.nanoTime();
                long duration = endTime - startTime;

                String methodName = method.getName();
                System.out.println("\n" + methodName + "() took:");
                System.out.println(duration + " nanoseconds");
                System.out.println(duration/1e6 + " milliseconds");
                System.out.println(duration/1e9 + " seconds");

                refreshDataStore();
            }
        }
    }

    static void createHundredKeys() throws IOException, KeyStoreException {
        for (int i = 0; i < 100; i++) {
            String key = "key-" + i;
            String value = "value-" + i;
            keyStore.create(key, value);
        }
    }

    static void createThousandKeys() throws IOException, KeyStoreException {
        for (int i = 0; i < 1000; i++) {
            String key = "key-" + i;
            String value = "value-" + i;
            keyStore.create(key, value);
        }
    }

    static void createTenThousandKeys() throws IOException, KeyStoreException {
        for (int i = 0; i < 10_000; i++) {
            String key = "key-" + i;
            String value = "value-" + i;
            keyStore.create(key, value);
        }
    }
}

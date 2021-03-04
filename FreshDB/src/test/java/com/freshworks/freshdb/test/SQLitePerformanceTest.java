package com.freshworks.freshdb.test;

import com.freshworks.freshdb.KeyStoreException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.*;

public class SQLitePerformanceTest {

    static Connection conn;

    @BeforeAll
    public static void establishConnection() throws SQLException {
        String url = "jdbc:sqlite:DBFiles/sqlitetest.db";
        conn = DriverManager.getConnection(url);
    }

    private void createTable() {
        try (Statement statement = conn.createStatement()) {
            String query =
                    """
                        CREATE TABLE IF NOT EXISTS keystore (
                        key text PRIMARY KEY,
                        value text
                        );
                    """;
            statement.execute(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void deleteTable() {
        try (Statement statement = conn.createStatement()){
            String query = "DROP TABLE keystore;";
            statement.execute(query);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void insertDataIntoKeyStoreTable(String key, String value) {
        String query = "INSERT INTO keystore(key, value) VALUES(?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, key);
            stmt.setString(2, value);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testCreation() throws InvocationTargetException, IllegalAccessException {
        Class<?> performanceClass = this.getClass();
        Method[] methods = performanceClass.getDeclaredMethods();
        for (Method method : methods) {
            if (Modifier.isStatic(method.getModifiers()) && method.getName().startsWith("create")) {
                createTable();

                long startTime = System.nanoTime();
                method.invoke(this);
                long endTime = System.nanoTime();
                long duration = endTime - startTime;

                String methodName = method.getName();
                System.out.println("\n" + methodName + "() took:");
                System.out.println(duration + " nanoseconds");
                System.out.println(duration/1e6 + " milliseconds");
                System.out.println(duration/1e9 + " seconds");

                deleteTable();
            }
        }
    }

    static void createHundredKeys() {
        for (int i = 0; i < 100; i++) {
            String key = "key-" + i;
            String value = "value-" + i;
            insertDataIntoKeyStoreTable(key, value);
        }
    }

    static void createFiveHundredKeys() {
        for (int i = 0; i < 500; i++) {
            String key = "key-" + i;
            String value = "value-" + i;
            insertDataIntoKeyStoreTable(key, value);
        }
    }
    static void createThousandKeys() {
        for (int i = 0; i < 1000; i++) {
            String key = "key-" + i;
            String value = "value-" + i;
            insertDataIntoKeyStoreTable(key, value);
        }
    }
    static void createFiveThousandKeys() {
        for (int i = 0; i < 5000; i++) {
            String key = "key-" + i;
            String value = "value-" + i;
            insertDataIntoKeyStoreTable(key, value);
        }
    }
    static void createTenThousandKeys() {
        for (int i = 0; i < 10_000; i++) {
            String key = "key-" + i;
            String value = "value-" + i;
            insertDataIntoKeyStoreTable(key, value);
        }
    }
    static void createFifteenThousandKeys() {
        for (int i = 0; i < 15_000; i++) {
            String key = "key-" + i;
            String value = "value-" + i;
            insertDataIntoKeyStoreTable(key, value);
        }
    }
    static void createTwentyThousandKeys() {
        for (int i = 0; i < 20_000; i++) {
            String key = "key-" + i;
            String value = "value-" + i;
            insertDataIntoKeyStoreTable(key, value);
        }
    }
    static void createTwentyFiveThousandKeys() {
        for (int i = 0; i < 25_000; i++) {
            String key = "key-" + i;
            String value = "value-" + i;
            insertDataIntoKeyStoreTable(key, value);
        }
    }

    @AfterAll
    public static void closeConnection() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}

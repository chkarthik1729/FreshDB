FreshDB
-------

FreshDB is a file-based key-value datastore that supports basic CRD (Create, Read and Delete) operations. This datastore is meant to be used as a local storage for one single process.

FreshDB is named after Freshworks' style of naming their products. It is initially developed as part of Freshworks' engineering hiring assignment. The requirements of the assignment were:
##### Functional requirements:
1. It can be initialized using an optional file path. If one is not provided, it will reliably create itself in a reasonable location on the computer.
2. A new key-value pair can be added to the datastore using the Create operation. The key is always a string - capped at 32 characters. The value is always a JSON object - capped at 16KB.
3. If Create is invoked for an existing key, an appropriate error must be returned.
4. A Read operation on a key can be performed by providing the key, and receiving the value in respose, as a JSON object.
5. A Delete operation can be performed by providing the key.
6. Every key supports setting a Time-To-Live property when it is created. This property is optional. If provided, it will be evaluated as an integer defining the number of seconds the key must be retained in the datastore. Once the Time-To-Live for a key has expired, the key will no longer be available for Read or Delete operations.
7. Appropriate error responses must always be returned to a client if it uses the datastore in unexpected ways or breaches any limits.
##### Non-Functional requirements:
1. The size of the file storing data must never exceed 1GB.
2. More than one client process cannot be allowed to use the same file as a data store at any given time.
3. A client process is allowed to access the datastore using multiple threads, if it desires to. The datastore must therefore be thread-safe.
4. The client will bear as little memory costs as possible to use this datastore, while deriving maximum performance with respect to response times for accsesing the datastore.


###
This implementation employs a custom storage allocation for the file and maintains a map between `key` and the appropriate `file pointer` where the value is stored in the file. While the memory consumption can be reduced even further by making one or two levels of indexing using B+ Trees, I preferred not to make it that complicated for a hiring assignment. 

The interface `com.freshworks.freshdb.KeyStore` defines the API for FreshDB and `com.freshworks.freshdb.service.FreshDBStore` provides the implementation. Both keys and values are considered to be Strings so as to make them a little more accessible.

Reads and Writes to the file are done using `RandomAccessFile`. Since `RandomAccessFile` is not thread-safe, all of the operations on FreshDB are synchronized instead of applying any explicit locks.

The `test` package also contains a simple performace test by inserting small keys and values to the datastore. Here are the results with a run on `MacBook Pro 13" (2.3GHz Quad-Core Intel i5, 8GB 1233MHz LPDDR3 Ram) running macOS Big Sur 11.1`:
```
createHundredKeys() took:
14665272 nanoseconds
14.665272 milliseconds
0.014665272 seconds

createThousandKeys() took:
85735822 nanoseconds
85.735822 milliseconds
0.085735822 seconds

createTenThousandKeys() took:
1593534235 nanoseconds
1593.534235 milliseconds
1.593534235 seconds
```
package com.freshworks.freshdb.test;

import com.freshworks.freshdb.KeyStore;
import com.freshworks.freshdb.KeyStoreException;
import com.freshworks.freshdb.exception.KeyNotFoundException;
import com.freshworks.freshdb.service.FreshDBStore;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

public class FreshDBStoreTest {

    @Test
    public void testCreateAndRead() throws IOException, KeyStoreException {
        KeyStore keyStore = new FreshDBStore();
        String value = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse hendrerit eros vel aliquam aliquet. Aenean vitae mauris non ex congue pharetra at in nunc. Donec vehicula, mauris sed gravida elementum, sapien odio congue tortor, at placerat velit lorem at ipsum. Phasellus id fermentum leo. Morbi imperdiet turpis non finibus gravida. Integer blandit risus non sapien viverra, vitae porta diam semper. Morbi fringilla interdum tortor. In metus justo, faucibus ut fringilla eget, ultrices ac enim. Ut vehicula, nunc gravida tempus euismod, magna massa congue augue, at sollicitudin neque turpis vel odio. Cras consectetur velit in nisl venenatis convallis. Praesent vestibulum massa a tellus egestas commodo. Aliquam iaculis vel tellus non laoreet. Nam gravida, lacus sit amet auctor mollis, neque sapien porttitor turpis, at sagittis velit leo at eros. Phasellus pharetra semper vestibulum.

                Quisque ac ex accumsan, dictum est sit amet, blandit nisi. Duis ultrices sapien et nibh iaculis, non dapibus sapien eleifend. Aenean dictum vestibulum risus, nec aliquam elit ultricies quis. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin odio risus, pretium id iaculis at, egestas vitae neque. Nulla viverra sit amet arcu nec gravida. Vivamus convallis tincidunt nulla, ut ornare lectus consectetur sed. Mauris velit justo, vehicula ac mollis quis, placerat vel libero. Phasellus vestibulum luctus faucibus. Maecenas ut justo et leo efficitur vulputate at non nisi. Maecenas non consequat leo. Donec quis suscipit dui, non dapibus tellus. Quisque placerat massa at elit vulputate, ac velit.""";
        keyStore.create("Key 1", value);
        assertEquals(value, keyStore.read("Key 1"));
    }

    @Test
    public void testCreateAndDelete() {

    }

    @Test
    public void testReadUnavailableKey() {

    }

    @Test
    public void testCreateSameKeyAgain() {

    }

    @Test
    public void testReadKeyAfterTTL() throws IOException, KeyStoreException, InterruptedException {
        KeyStore keyStore = new FreshDBStore();
        keyStore.create("Key 1", "Value 1", 2);
        assertEquals("Value 1", keyStore.read("Key 1"));
        Thread.sleep(2000);
        assertThrows(KeyNotFoundException.class, () -> keyStore.read("Key 1"));
    }

    @Test
    public void testCreatingSameKeyAfterTTL() {

    }

    @Test
    public void testCreateLargeKey() {

    }

    @Test
    public void testCreateLargeValue() {

    }

    @Test
    public void testCreateInfiniteKeys() {

    }
}

/*
 * Copyright 2018 Heiko Scherrer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openwms.common.transport;

import org.junit.Test;
import org.openwms.common.transport.Barcode.BARCODE_ALIGN;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * A BarcodeTest.
 * 
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 */
public class BarcodeTest {

    /**
     * Logger instance can be used by subclasses.
     */
    private static final Logger logger = LoggerFactory.getLogger(BarcodeTest.class);

    /**
     * Test Barcode instantiation with <code>null</code>.
     */
    @Test
    public final void testBarcodeWithNull() {
        try {
            new Barcode(null);
            fail("NOK:Barcode cannot instanciated with NULL");
        } catch (IllegalArgumentException iae) {
            logger.debug("OK:Not allowed to initiante a Barcode with null");
        }
    }

    /**
     * Test basic behavior of the Barcode class.
     */
    @Test
    public final void testBarcode() {
        new Barcode("TEST");

        Barcode.setLength(20);
        Barcode.setPadder('0');

        Barcode bc3 = new Barcode("RIGHT");
        logger.debug("Test left-padded, right-aligned:[" + bc3 + "]");
        assertTrue("Barcode length must be expanded to 20 characters.", (20 == Barcode.getLength()));
        assertTrue("Barcode must start with 0", bc3.toString().startsWith("0"));

        Barcode.setAlignment(BARCODE_ALIGN.LEFT);
        Barcode bc2 = new Barcode("LEFT");
        logger.debug("Test right-padded, left-aligned:[" + bc2 + "]");
        assertTrue("Barcode must end with 0", bc2.toString().endsWith("0"));
        assertTrue("Barcode must start with LEFT", bc2.toString().startsWith("LEFT"));

        Barcode.setLength(2);
        Barcode bc4 = new Barcode("A123456789");
        logger.debug("Test not-padded:[" + bc4 + "]");
        assertTrue("Barcode must end with 9", bc4.toString().endsWith("9"));
        assertTrue("Barcode must start with A", bc4.toString().startsWith("A"));
    }
}

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

import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;


/**
 * A Barcode is a printable item with an unique identifier to label {@code TransportUnit}s. The identifier has a defined number of
 * characters whereas these characters are aligned either left or right. Non filled positions of a Barcode are padded with a so called
 * padding character.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @GlossaryTerm
 */
@Embeddable
public class Barcode implements Serializable {

    /** Length of a Barcode field. */
    public static final int BARCODE_LENGTH = 20;

    /**
     * A BARCODE_ALIGN defines whether the {@code Barcode} is applied {@code LEFT} or {@code RIGHT}. <p> Only be used when padding is
     * activated. </p>
     *
     * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
     * @version 1.0
     * @since 0.1
     */
    public static enum BARCODE_ALIGN {
        /** Barcode is left aligned. */
        LEFT,
        /** Barcode is right aligned. */
        RIGHT
    }

    /** Define whether to use character padding or not. */
    private static boolean padded = true;

    /**
     * Defines a character used for padding.<br> If the actually length of the {@code Barcode} is less than the maximum defined {@code
     * length} the rest will be filled with {@code padder} characters.
     */
    private static char padder = '0';

    /** Defines the maximum length of characters. */
    private static int length = Barcode.BARCODE_LENGTH;

    /** The alignment of the {@code Barcode}. Could be something of {@link BARCODE_ALIGN}. */
    private static BARCODE_ALIGN alignment = BARCODE_ALIGN.RIGHT;

    /** 'Identifier' of the {@code Barcode}. <p> <i>Note:</i>It is not guaranteed that this field must be unique. </p> */
    @Column(name = "C_BARCODE")
    private String value;

    /*~ ----------------------------- constructors ------------------- */

    /** Dear JPA... */
    protected Barcode() {
    }

    /**
     * Create a new {@code Barcode} with a String.
     *
     * @param value The value of the {@code Barcode} as String
     * @throws IllegalArgumentException when the value is set to <code>null</code>.
     */
    public Barcode(String value) {
        adjustBarcode(value);
    }

    /**
     * Simple factory method to replace default constructor in application logic (still needed for framework stuff).
     *
     * @param value The value of the {@code Barcode} as String
     * @return Formatted Barcode instance
     */
    public static Barcode of(String value) {
        return new Barcode(value);
    }
    /*~ ----------------------------- methods ------------------- */

    /**
     * Force the Barcode to be aligned to the determined rules regarding padding, alignment.
     *
     * @param val The old Barcode as String
     * @return The new aligned Barcode
     */
    public final String adjustBarcode(String val) {
        if (val == null) {
            throw new IllegalArgumentException("Cannot create a barcode without value");
        }
        if (isPadded()) {
            this.value = (alignment == BARCODE_ALIGN.RIGHT) ? StringUtils.leftPad(val, length, padder) : StringUtils
                    .rightPad(val, length, padder);
        } else {
            this.value = val;
        }
        return this.value;
    }

    /**
     * Returns the alignment.
     *
     * @return The alignment
     */
    public static BARCODE_ALIGN getAlignment() {
        return alignment;
    }

    /**
     * Set the alignment.
     *
     * @param align The alignment to set
     */
    public static void setAlignment(BARCODE_ALIGN align) {
        alignment = align;
    }

    /**
     * Check if {@code Barcode} is padded.
     *
     * @return {@literal true} if {@code Barcode} is padded, otherwise {@literal false}.
     */
    public static boolean isPadded() {
        return padded;
    }

    /**
     * Set padded.
     *
     * @param p {@literal true} if {@code Barcode} should be padded, otherwise {@literal false}.
     */
    static void setPadded(boolean p) {
        padded = p;
    }

    /**
     * Return the padding character.
     *
     * @return The padding character.
     */
    public static char getPadder() {
        return padder;
    }

    /**
     * Set the padding character.
     *
     * @param p The padding character to use
     */
    static void setPadder(char p) {
        padder = p;
        padded = true;
    }

    /**
     * Return the {@code Barcode} value.
     *
     * @return The value of the {@code Barcode}
     */
    public String getValue() {
        return value;
    }

    /**
     * Set the {@code Barcode} value.
     *
     * @param value The value to set
     */
    public void setValue(String value) {
        adjustBarcode(value);
    }

    /**
     * Return the length.
     *
     * @return The length
     */
    public static int getLength() {
        return length;
    }

    /**
     * Set the length.
     *
     * @param l The length to set
     */
    static void setLength(int l) {
        length = l;
    }

    /**
     * Return the value of the {@code Barcode} as String.
     *
     * @return As String
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Barcode other = (Barcode) obj;
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }
}
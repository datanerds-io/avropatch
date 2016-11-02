package io.datanerds.avropatch.operation;

import org.apache.avro.reflect.AvroIgnore;

/**
 * This class represents the "remove" operation of RFC 6902 'JavaScript Object Notation (JSON) Patch'.
 *
 * @see <a href="https://tools.ietf.org/html/rfc6902#section-4.2">https://tools.ietf.org/html/rfc6902#section-4.2</a>
 */
public final class Remove implements Operation {
    @AvroIgnore
    public static final String op = "remove";
    public final Path path;

    private Remove() {
        this.path = null;
    }

    /**
     *
     * @param path Target location of value to be removed
     */
    public Remove(Path path) {
        this.path = path;
    }
}
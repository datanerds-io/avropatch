package io.datanerds.avropatch.operation;

import org.apache.avro.reflect.AvroIgnore;
import org.apache.avro.reflect.AvroSchema;

/**
 * This class represents the "replace" operation of RFC 6902 'JavaScript Object Notation (JSON) Patch'.
 *
 * @see <a href="https://tools.ietf.org/html/rfc6902#section-4.3">https://tools.ietf.org/html/rfc6902#section-4.3</a>
 */
public final class Replace<T> implements Operation {
    @AvroIgnore
    public static final String op = "replace";
    public final Path path;
    @AvroSchema(DefaultSchema.VALUE)
    public final T value;

    private Replace() {
        this.path = null;
        this.value = null;
    }

    /**
     *
     * @param path Path pointing out where the JSON value should be replaced
     * @param value Actual value to <i>replace</i> in patched object
     *
     * @see DefaultSchema
     */
    public Replace(Path path, T value) {
        this.path = path;
        this.value = value;
    }
}

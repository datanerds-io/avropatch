package io.datanerds.avropatch.operation;

import org.apache.avro.reflect.AvroIgnore;
import org.apache.avro.reflect.AvroSchema;

/**
 * This class represents the "add" operation of RFC 6902 'JavaScript Object Notation (JSON) Patch'.
 *
 * @see <a href="https://tools.ietf.org/html/rfc6902#section-4.1">https://tools.ietf.org/html/rfc6902#section-4.1</a>
 * @param <T> DefaultSchema type of <i>add</i> operation
 */
public final class Add<T> implements Operation {
    @AvroIgnore
    public static final String op = "add";
    public final Path path;
    @AvroSchema(DefaultSchema.VALUE)
    public final T value;

    private Add() {
        this.path = null;
        this.value = null;
    }

    /**
     *
     * @param path Path pointing out where the JSON value should be added
     * @param value Actual value to <i>add</i> to patched object
     *
     * @see DefaultSchema
     */
    public Add(Path path, T value) {
        this.path = path;
        this.value = value;
    }
}

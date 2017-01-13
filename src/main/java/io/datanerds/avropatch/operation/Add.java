package io.datanerds.avropatch.operation;

import io.datanerds.avropatch.value.DefaultSchema;
import org.apache.avro.reflect.AvroSchema;

/**
 * This class represents the "add" operation of RFC 6902 'JavaScript Object Notation (JSON) Patch'.
 *
 * @see <a href="https://tools.ietf.org/html/rfc6902#section-4.1">https://tools.ietf.org/html/rfc6902#section-4.1</a>
 * @param <T> DefaultSchema type of <i>add</i> operation
 */
public final class Add<T> implements Operation {
    public final Path path;
    @AvroSchema(DefaultSchema.VALUE)
    public final T value;

    @SuppressWarnings("unused") // no-arg constructor needed by Avro
    private Add() {
        this(null, null);
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

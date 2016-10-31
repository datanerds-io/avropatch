package io.datanerds.avropatch.operation;

import org.apache.avro.reflect.AvroIgnore;
import org.apache.avro.reflect.AvroSchema;

public final class Add<T> implements Operation {
    @AvroIgnore
    public static final String op = "add";
    public final Path path;
    @AvroSchema(Value.SCHEMA)
    public final T value;

    /**
     * Private no arg constructor for Avro deserializer.
     */
    private Add() {
        this.path = null;
        this.value = null;
    }

    public Add(Path path, T value) {
        this.path = path;
        this.value = value;
    }
}

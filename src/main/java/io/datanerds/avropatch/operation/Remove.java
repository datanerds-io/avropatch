package io.datanerds.avropatch.operation;

import org.apache.avro.reflect.AvroIgnore;
import org.apache.avro.reflect.AvroSchema;
import org.apache.avro.reflect.Stringable;

public final class Remove implements Operation {
    @AvroIgnore
    public static final String op = "remove";
    public final Path path;

    /**
     * Private no arg constructor for Avro deserializer.
     */
    private Remove() {
        this.path = null;
    }

    public Remove(Path path) {
        this.path = path;
    }
}
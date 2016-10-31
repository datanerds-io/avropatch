package io.datanerds.avropatch.operation;

import org.apache.avro.reflect.AvroIgnore;
import org.apache.avro.reflect.AvroSchema;
import org.apache.avro.reflect.Stringable;

public final class Move implements Operation {
    @AvroIgnore
    public static final String op = "move";
    public final Path from;
    public final Path path;

    /**
     * Private no arg constructor for Avro deserializer.
     */
    private Move() {
        this.from = null;
        this.path = null;
    }

    public Move(Path from, Path path) {
        this.from = from;
        this.path = path;
    }
}
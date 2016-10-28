package io.datanerds.avropatch.operation;

import org.apache.avro.reflect.AvroIgnore;
import org.apache.avro.reflect.AvroSchema;
import org.apache.avro.reflect.Stringable;

public class Move implements Operation {
    @AvroIgnore
    public static final String op = "move";
    @Stringable
    @AvroSchema("\"string\"")
    public final Path path;
    @Stringable
    @AvroSchema("\"string\"")
    public final Path from;

    public Move(Path path, Path from) {
        this.path = path;
        this.from = from;
    }
}
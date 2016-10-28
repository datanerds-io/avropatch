package io.datanerds.avropatch.operation;

import org.apache.avro.reflect.AvroIgnore;
import org.apache.avro.reflect.AvroSchema;
import org.apache.avro.reflect.Stringable;

public class Remove implements Operation {
    @AvroIgnore
    public static final String op = "remove";
    @Stringable
    @AvroSchema("\"string\"")
    public final Path path;

    public Remove(Path path) {
        this.path = path;
    }
}
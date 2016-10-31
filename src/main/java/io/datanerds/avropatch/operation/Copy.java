package io.datanerds.avropatch.operation;

import org.apache.avro.reflect.AvroIgnore;
import org.apache.avro.reflect.AvroSchema;
import org.apache.avro.reflect.Stringable;

public class Copy implements Operation {
    @AvroIgnore
    public static final String op = "copy";
    @Stringable
    @AvroSchema("\"string\"")
    public final Path path;
    @Stringable
    @AvroSchema("\"string\"")
    public final Path from;

    public Copy(Path path, Path from) {
        this.path = path;
        this.from = from;
    }
}

package io.datanerds.avropatch.operation;

import io.datanerds.avropatch.Path;
import org.apache.avro.reflect.AvroIgnore;
import org.apache.avro.reflect.AvroSchema;

public class Move implements Operation {
    @AvroIgnore
    public static final String op = "move";
    Path path;
    Path from;
}
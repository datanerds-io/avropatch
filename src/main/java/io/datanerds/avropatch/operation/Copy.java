package io.datanerds.avropatch.operation;

import io.datanerds.avropatch.Path;
import org.apache.avro.reflect.AvroIgnore;

public class Copy implements Operation {
    @AvroIgnore
    public static final String op = "copy";
    Path path;
    Path from;
}

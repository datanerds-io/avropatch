package io.datanerds.avropatch.operation;

import io.datanerds.avropatch.Path;
import org.apache.avro.reflect.AvroIgnore;
import org.apache.avro.reflect.AvroSchema;

public class Replace<T> implements Operation {
    @AvroIgnore
    public static final String op = "replace";
    Path path;
    @AvroSchema("\"string\"")
    T value;

}

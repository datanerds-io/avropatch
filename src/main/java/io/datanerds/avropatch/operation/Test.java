package io.datanerds.avropatch.operation;

import io.datanerds.avropatch.Path;
import org.apache.avro.reflect.AvroIgnore;
import org.apache.avro.reflect.AvroSchema;

public class Test<T> implements Operation {
    @AvroIgnore
    public static final String op = "test";
    Path path;
    @AvroSchema("\"string\"")
    T value;

}

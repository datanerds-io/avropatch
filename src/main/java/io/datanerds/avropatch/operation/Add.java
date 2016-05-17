package io.datanerds.avropatch.operation;

import io.datanerds.avropatch.Path;
import org.apache.avro.reflect.AvroIgnore;
import org.apache.avro.reflect.AvroSchema;

public class Add<T> implements Operation {

    @AvroIgnore
    public static final String op = "add";
    Path path;
    @AvroSchema("\"string\"")
    T value;

}

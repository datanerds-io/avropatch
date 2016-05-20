package io.datanerds.avropatch.operation;

import com.github.fge.jackson.jsonpointer.JsonPointer;
import org.apache.avro.reflect.AvroIgnore;
import org.apache.avro.reflect.AvroSchema;
import org.apache.avro.reflect.Stringable;

public class Add<T> implements Operation {

    @AvroIgnore
    public static final String op = "add";
    @Stringable
    @AvroSchema("\"string\"")
    public final JsonPointer path;
    @AvroSchema(Value.SCHEMA)
    public final T value;

    public Add() {
        this.path = null;
        this.value = null;
    }
    public Add(JsonPointer path, T value) {
        this.path = path;
        this.value = value;
    }
}

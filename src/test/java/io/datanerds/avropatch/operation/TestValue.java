package io.datanerds.avropatch.operation;

import org.apache.avro.reflect.AvroSchema;

public class TestValue <T> {
    @AvroSchema(Value.SCHEMA)
    public final T value;

    TestValue() {
        this.value = null;
    }

    TestValue(T value) {
        this.value = value;
    }
}
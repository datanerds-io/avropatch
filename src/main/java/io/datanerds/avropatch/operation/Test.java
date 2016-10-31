package io.datanerds.avropatch.operation;

import org.apache.avro.reflect.AvroIgnore;
import org.apache.avro.reflect.AvroSchema;
import org.apache.avro.reflect.Stringable;

public final class Test<T> implements Operation {
    @AvroIgnore
    public static final String op = "test";
    public final Path path;
    @AvroSchema(Value.SCHEMA)
    public final T value;

    /**
     * Private no arg constructor for Avro deserializer.
     */
    private Test() {
        this.path = null;
        this.value = null;
    }

    public Test(Path path, T value) {
        this.path = path;
        this.value = value;
    }
}

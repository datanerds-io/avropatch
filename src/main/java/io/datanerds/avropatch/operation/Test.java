package io.datanerds.avropatch.operation;

import com.github.fge.jackson.jsonpointer.JsonPointer;
import org.apache.avro.reflect.AvroIgnore;
import org.apache.avro.reflect.AvroSchema;
import org.apache.avro.reflect.Stringable;

public class Test<T> implements Operation {
    @AvroIgnore
    public static final String op = "test";
    @Stringable
    @AvroSchema("\"string\"")
    public final JsonPointer path;
    @AvroSchema(Value.SCHEMA)
    public final T value;

    public Test(JsonPointer path, T value) {
        this.path = path;
        this.value = value;
    }
}

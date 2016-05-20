package io.datanerds.avropatch.operation;

import com.github.fge.jackson.jsonpointer.JsonPointer;
import org.apache.avro.reflect.AvroIgnore;
import org.apache.avro.reflect.AvroSchema;
import org.apache.avro.reflect.Stringable;

public class Replace<T> implements Operation {
    @AvroIgnore
    public static final String op = "replace";
    @Stringable
    @AvroSchema("\"string\"")
    public final JsonPointer path;
    @AvroSchema(Value.SCHEMA)
    public final T value;

    public Replace(JsonPointer path, T value) {
        this.path = path;
        this.value = value;
    }
}

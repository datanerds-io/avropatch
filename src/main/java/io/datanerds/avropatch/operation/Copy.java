package io.datanerds.avropatch.operation;

import com.github.fge.jackson.jsonpointer.JsonPointer;
import org.apache.avro.reflect.AvroIgnore;
import org.apache.avro.reflect.AvroSchema;
import org.apache.avro.reflect.Stringable;

public class Copy implements Operation {
    @AvroIgnore
    public static final String op = "copy";
    @Stringable
    @AvroSchema("\"string\"")
    public final JsonPointer path;
    @Stringable
    @AvroSchema("\"string\"")
    public final JsonPointer from;

    public Copy(JsonPointer path, JsonPointer from) {
        this.path = path;
        this.from = from;
    }
}

package io.datanerds.avropatch.operation;

import com.github.fge.jackson.jsonpointer.JsonPointer;
import org.apache.avro.reflect.AvroIgnore;
import org.apache.avro.reflect.AvroSchema;
import org.apache.avro.reflect.Stringable;

public class Remove implements Operation {
    @AvroIgnore
    public static final String op = "remove";
    @Stringable
    @AvroSchema("\"string\"")
    public final JsonPointer path;

    public Remove(JsonPointer path) {
        this.path = path;
    }
}
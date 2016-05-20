package io.datanerds.avropatch.operation;

import com.github.fge.jackson.jsonpointer.JsonPointer;
import org.apache.avro.reflect.AvroIgnore;
import org.apache.avro.reflect.AvroSchema;
import org.apache.avro.reflect.Stringable;

public class Move implements Operation {
    @AvroIgnore
    public static final String op = "move";
    @Stringable
    @AvroSchema("\"string\"")
    public final JsonPointer path;
    @Stringable
    @AvroSchema("\"string\"")
    public final JsonPointer from;

    public Move(JsonPointer path, JsonPointer from) {
        this.path = path;
        this.from = from;
    }
}
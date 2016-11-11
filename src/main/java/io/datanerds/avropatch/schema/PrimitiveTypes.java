package io.datanerds.avropatch.schema;

import org.apache.avro.Schema;

import static org.apache.avro.Schema.create;

public interface PrimitiveTypes {
    Schema BOOLEAN = create(Schema.Type.BOOLEAN);
    Schema DOUBLE = create(Schema.Type.DOUBLE);
    Schema FLOAT = create(Schema.Type.FLOAT);
    Schema INTEGER = create(Schema.Type.INT);
    Schema LONG = create(Schema.Type.LONG);
    Schema NULL = create(Schema.Type.NULL);
    Schema STRING = create(Schema.Type.STRING);
    Object NO_DEFAULT = null;
}

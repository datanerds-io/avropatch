package io.datanerds.avropatch.value.type;

import org.apache.avro.Schema;

import static org.apache.avro.Schema.create;

/**
 * This enum holds the schema constants for the Avro's default value types.
 */
public enum PrimitiveType {
    BOOLEAN(create(Schema.Type.BOOLEAN)),
    DOUBLE(create(Schema.Type.DOUBLE)),
    FLOAT(create(Schema.Type.FLOAT)),
    INTEGER(create(Schema.Type.INT)),
    LONG(create(Schema.Type.LONG)),
    NULL(create(Schema.Type.NULL)),
    STRING(create(Schema.Type.STRING));

    private final Schema schema;

    PrimitiveType(Schema schema) {
        this.schema = schema;
    }

    public Schema getSchema() {
        return schema;
    }
}

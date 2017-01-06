package io.datanerds.avropatch.value.type;

import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;

/**
 * This interface holds the schema constants for the custom type representing a {@link java.util.UUID} value.
 *
 * @see io.datanerds.avropatch.value.conversion.UUIDConversion
 */
public interface UuidType {
    String NAME = LogicalTypes.uuid().getName();
    String DOC = "UUID serialized via two long values: Its most significant and least significant 64 bits.";
    int SIZE = 2 * Long.BYTES;
    Schema SCHEMA = LogicalTypes.uuid().addToSchema(Schema.createFixed(NAME, DOC, null, SIZE));
}
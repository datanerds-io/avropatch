package io.datanerds.avropatch.value.type;

import org.apache.avro.LogicalType;
import org.apache.avro.Schema;

/**
 * This interface holds the schema constants for the custom type representing a {@link java.math.BigInteger} value.
 *
 * @see io.datanerds.avropatch.value.conversion.BigIntegerConversion
 */
public interface BigIntegerType {
    String NAME = "big-integer";
    LogicalType LOGICAL_TYPE = new LogicalType(NAME);
    Schema SCHEMA = LOGICAL_TYPE.addToSchema(Schema.create(Schema.Type.BYTES));
}
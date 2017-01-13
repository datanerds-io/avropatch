package io.datanerds.avropatch.value.type;

import org.apache.avro.LogicalType;
import org.apache.avro.Schema;

/**
 * This interface holds the schema constants for the custom type representing a {@link java.util.Date} value.
 *
 * @see io.datanerds.avropatch.value.conversion.DateConversion
 */
public interface TimestampType {
    String NAME = "timestamp";
    String DOC = "Timestamp representing the number of milliseconds since January 1, 1970, 00:00:00 GMT";
    LogicalType LOGICAL_TYPE = new LogicalType(NAME);
    int SIZE = Long.BYTES;
    Schema SCHEMA = LOGICAL_TYPE.addToSchema(Schema.createFixed(NAME, DOC, null, SIZE));
}
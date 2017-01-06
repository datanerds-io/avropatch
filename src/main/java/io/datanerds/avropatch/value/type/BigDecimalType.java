package io.datanerds.avropatch.value.type;

import org.apache.avro.LogicalType;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;

/**
 * This interface holds the schema constants for the custom type representing a {@link java.math.BigDecimal} value.
 *
 * @see io.datanerds.avropatch.value.conversion.BigDecimalConversion
 */
public interface BigDecimalType {
    String NAME = "big-decimal";
    String DOC = "BigDecimal value represented via its scale and unscaled value.";
    LogicalType LOGICAL_TYPE = new LogicalType(NAME);
    Schema RECORD = SchemaBuilder
            .record("decimal")
            .doc(DOC)
            .fields()
            .name("unscaledValue").type(BigIntegerType.SCHEMA).noDefault()
            .name("scale").type(Schema.create(Schema.Type.INT)).noDefault()
            .endRecord();

    Schema SCHEMA = LOGICAL_TYPE.addToSchema(RECORD);
}
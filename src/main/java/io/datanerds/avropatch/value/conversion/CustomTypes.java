package io.datanerds.avropatch.value.conversion;

import org.apache.avro.LogicalType;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;

/**
 * This interface holds the schema constants for all supported custom types.
 *
 * @see UUIDConversion
 * @see DateConversion
 * @see BigDecimalConversion
 * @see BigIntegerConversion
 */
public interface CustomTypes {

    interface BigDecimalType {
        String NAME = "big-decimal";
        String DOC = "BigDecimal value represented via it's scale and unscaled value.";
        LogicalType LOGICAL_TYPE = new LogicalType(NAME);
        Schema RECORD = SchemaBuilder.record("decimal").doc(BigDecimalType.DOC).fields()
                .name("unscaledValue").type(BigIntegerType.SCHEMA).noDefault()
                .name("scale").type(Schema.create(Schema.Type.INT)).noDefault()
                .endRecord();

        Schema SCHEMA = BigDecimalType.LOGICAL_TYPE.addToSchema(RECORD);
    }

    interface BigIntegerType {
        String NAME = "big-integer";
        LogicalType LOGICAL_TYPE = new LogicalType(NAME);
        Schema SCHEMA = LOGICAL_TYPE.addToSchema(Schema.create(Schema.Type.BYTES));

    }

    interface DateType {
        String NAME = "timestamp";
        String DOC = "Timestamp representing the number of milliseconds since January 1, 1970, 00:00:00 GMT";
        LogicalType LOGICAL_TYPE = new LogicalType(NAME);
        int SIZE = Long.BYTES;
        Schema SCHEMA = LOGICAL_TYPE.addToSchema(Schema.createFixed(NAME, DOC, null, SIZE));
    }

    interface UuidType {
        String NAME = LogicalTypes.uuid().getName();
        String DOC = "UUID serialized via two long values: It's most significant and least significant 64 bits.";
        int SIZE = 2 * Long.BYTES;
        Schema SCHEMA = LogicalTypes.uuid().addToSchema(Schema.createFixed(NAME, DOC, null, SIZE));
    }
}

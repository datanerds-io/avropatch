package io.datanerds.avropatch.schema;

import io.datanerds.avropatch.Patch;
import io.datanerds.avropatch.value.conversion.BigDecimalConversion;
import io.datanerds.avropatch.value.conversion.BigIntegerConversion;
import io.datanerds.avropatch.value.conversion.DateConversion;
import io.datanerds.avropatch.value.conversion.UUIDConversion;
import org.apache.avro.LogicalType;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Type;
import org.apache.avro.SchemaBuilder;

import java.util.Arrays;

import static io.datanerds.avropatch.schema.OperationTypes.NAMESPACE;
import static io.datanerds.avropatch.schema.PrimitiveTypes.*;
import static org.apache.avro.Schema.createRecord;
import static org.apache.avro.Schema.createUnion;

/**
 * This interface holds the schema constants for all supported custom types.
 *
 * @see UUIDConversion
 * @see DateConversion
 * @see BigDecimalConversion
 * @see BigIntegerConversion
 * @see io.datanerds.avropatch.operation.Path
 */
public interface CustomTypes {

    interface PatchType {
        String NAME = Patch.class.getSimpleName();
        String DOC = "This record represents a PATCH holding a sequence of operations to apply to a given object.";
        static Schema create(Schema valueSchema) {
            Schema.Field operations = new Schema.Field("operations",
                    Schema.createArray(OperationTypes.Operation.create(valueSchema)),
                    "Sequence of operations to apply to a given object.", NO_DEFAULT);
            return createRecord(NAME, DOC, Patch.class.getPackage().getName(), false, Arrays.asList(operations));
        }
    }

    interface BigDecimalType {
        String NAME = "big-decimal";
        String DOC = "BigDecimal value represented via it's scale and unscaled value.";
        LogicalType LOGICAL_TYPE = new LogicalType(NAME);
        Schema RECORD = SchemaBuilder.record("decimal").doc(DOC).fields()
                .name("unscaledValue").type(BigIntegerType.SCHEMA).noDefault()
                .name("scale").type(Schema.create(Type.INT)).noDefault()
                .endRecord();

        Schema SCHEMA = LOGICAL_TYPE.addToSchema(RECORD);
    }

    interface BigIntegerType {
        String NAME = "big-integer";
        LogicalType LOGICAL_TYPE = new LogicalType(NAME);
        Schema SCHEMA = LOGICAL_TYPE.addToSchema(Schema.create(Type.BYTES));

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

    interface Path {
        String NAME = io.datanerds.avropatch.operation.Path.class.getSimpleName();
        String DOC = "JSON Path serialized as String array holding its parts.";
        Schema SCHEMA = SchemaBuilder.record(NAME).doc(DOC).namespace(NAMESPACE).fields()
                .name("parts").type(Schema.createArray(Schema.create(Type.STRING))).noDefault()
                .endRecord();
    }

    Schema VALUE_TYPE_UNION = createUnion(BOOLEAN, DOUBLE, FLOAT, INTEGER, LONG, NULL, STRING,
            BigDecimalType.SCHEMA, BigIntegerType.SCHEMA, DateType.SCHEMA, UuidType.SCHEMA);
}

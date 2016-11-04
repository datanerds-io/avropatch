package io.datanerds.avropatch.value.conversion;

import org.apache.avro.Conversion;
import org.apache.avro.LogicalType;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.IndexedRecord;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * TODO
 *
 * @see BigIntegerConversion
 */
public class BigDecimalConversion extends Conversion<BigDecimal> {

    private static final String NAME = "big-decimal";
    private static final String DOC = "TODO";
    private static final Schema RECORD = SchemaBuilder.record("decimal").doc(DOC).fields().name("unscaledValue").type(BigIntegerConversion.SCHEMA).noDefault().name("scale").type(Schema.create(Schema.Type.INT)).noDefault().endRecord();
    public static final Schema SCHEMA = new LogicalType(NAME).addToSchema(RECORD);

    @Override
    public Schema getRecommendedSchema() {
        return SCHEMA;
    }

    @Override
    public Class<BigDecimal> getConvertedType() {
        return BigDecimal.class;
    }

    @Override
    public String getLogicalTypeName() {
        return NAME;
    }

    @Override
    public BigDecimal fromRecord(IndexedRecord value, Schema schema, LogicalType type) {
        BigInteger unscaledValue = BigIntegerConversion.fromBytes((ByteBuffer)value.get(0));
        int scale = (int) value.get(1);

        return new BigDecimal(unscaledValue, scale);
    }

    @Override
    public IndexedRecord toRecord(BigDecimal value, Schema schema, LogicalType type) {
        GenericData.Record record = new GenericData.Record(schema);
        record.put(0, BigIntegerConversion.toBytes(value.unscaledValue()));
        record.put(1, value.scale());
        return record;
    }
}

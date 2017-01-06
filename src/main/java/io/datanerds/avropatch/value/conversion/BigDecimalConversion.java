package io.datanerds.avropatch.value.conversion;

import io.datanerds.avropatch.value.type.BigDecimalType;
import org.apache.avro.Conversion;
import org.apache.avro.LogicalType;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.IndexedRecord;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * This class converts a {@link BigDecimal} value into an Avro {@link IndexedRecord} and back. It depends on
 * {@link BigIntegerConversion} since it serializes the {@link BigDecimal}s unscaled value into a {@link BigInteger} and
 * its scale into an {@link Integer}.
 * Also, its logical type is statically registered in {@link LogicalTypes}, because it introduces a new custom type name
 * 'big-decimal'.
 *
 * @see BigIntegerConversion
 * @see LogicalTypes
 */
public class BigDecimalConversion extends Conversion<BigDecimal> implements BigDecimalType {

    static {
        LogicalTypes.register(NAME, schema -> LOGICAL_TYPE);
    }

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
        BigInteger unscaledValue = (BigInteger)value.get(0);
        int scale = (int) value.get(1);
        return new BigDecimal(unscaledValue, scale);
    }

    @Override
    public IndexedRecord toRecord(BigDecimal value, Schema schema, LogicalType type) {
        GenericData.Record record = new GenericData.Record(schema);
        record.put(0, value.unscaledValue());
        record.put(1, value.scale());
        return record;
    }
}

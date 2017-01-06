package io.datanerds.avropatch.value.conversion;

import io.datanerds.avropatch.value.type.BigIntegerType;
import org.apache.avro.Conversion;
import org.apache.avro.LogicalType;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class BigIntegerConversion extends Conversion<BigInteger> implements BigIntegerType {

    static {
        LogicalTypes.register(NAME, schema -> LOGICAL_TYPE);
    }

    @Override
    public Schema getRecommendedSchema() {
        return SCHEMA;
    }

    @Override
    public Class<BigInteger> getConvertedType() {
        return BigInteger.class;
    }

    @Override
    public String getLogicalTypeName() {
        return NAME;
    }

    @Override
    public BigInteger fromBytes(ByteBuffer value, Schema schema, LogicalType type)  {
        return new BigInteger(value.array());
    }

    @Override
    public ByteBuffer toBytes(BigInteger value, Schema schema, LogicalType type)  {
        return ByteBuffer.wrap(value.toByteArray());
    }
}

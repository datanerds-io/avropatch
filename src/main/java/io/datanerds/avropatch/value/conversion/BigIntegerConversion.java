package io.datanerds.avropatch.value.conversion;

import io.datanerds.avropatch.schema.CustomTypes.BigIntegerType;
import org.apache.avro.Conversion;
import org.apache.avro.LogicalType;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class BigIntegerConversion extends Conversion<BigInteger> {

    static {
        LogicalTypes.register(BigIntegerType.NAME, schema -> BigIntegerType.LOGICAL_TYPE);
    }

    @Override
    public Schema getRecommendedSchema() {
        return BigIntegerType.SCHEMA;
    }

    @Override
    public Class<BigInteger> getConvertedType() {
        return BigInteger.class;
    }

    @Override
    public String getLogicalTypeName() {
        return BigIntegerType.NAME;
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

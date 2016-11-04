package io.datanerds.avropatch.value.conversion;

import org.apache.avro.Conversion;
import org.apache.avro.LogicalType;
import org.apache.avro.Schema;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class BigIntegerConversion extends Conversion<BigInteger> {
    private static final String NAME = "big-integer";
    public static final Schema SCHEMA = new LogicalType(NAME).addToSchema(Schema.create(Schema.Type.BYTES));

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
        return fromBytes(value);
    }

    @Override
    public ByteBuffer toBytes(BigInteger value, Schema schema, LogicalType type)  {
        return toBytes(value);
    }

    protected static BigInteger fromBytes(ByteBuffer value) {
        return new BigInteger(value.array());
    }

    protected static  ByteBuffer toBytes(BigInteger value)  {
        return ByteBuffer.wrap(value.toByteArray());
    }
}

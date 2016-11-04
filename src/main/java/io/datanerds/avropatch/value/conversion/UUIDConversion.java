package io.datanerds.avropatch.value.conversion;

import org.apache.avro.Conversion;
import org.apache.avro.LogicalType;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericFixed;

import java.nio.ByteBuffer;
import java.util.UUID;

public class UUIDConversion extends Conversion<UUID> {
    private static final String NAME = LogicalTypes.uuid().getName();
    private static final String DOC = "TODO";
    private static final int SIZE = 2 * Long.BYTES;
    public static final Schema SCHEMA = LogicalTypes.uuid().addToSchema(Schema.createFixed(NAME, DOC, null, SIZE));

    @Override
    public Schema getRecommendedSchema() {
        return SCHEMA;
    }

    @Override
    public Class<UUID> getConvertedType() {
        return UUID.class;
    }

    @Override
    public String getLogicalTypeName() {
        return NAME;
    }

    @Override
    public GenericFixed toFixed(UUID value, Schema schema, LogicalType type) {
        return new GenericFixed() {
            @Override
            public byte[] bytes() {
                ByteBuffer buffer = ByteBuffer.allocate(SIZE);
                buffer.putLong(value.getLeastSignificantBits());
                buffer.putLong(value.getMostSignificantBits());
                return buffer.array();
            }

            @Override
            public Schema getSchema() {
                return SCHEMA;
            }
        };
    }

    @Override
    public UUID fromFixed(GenericFixed value, Schema schema, LogicalType type) {
        ByteBuffer buffer = ByteBuffer.wrap(value.bytes());
        long leastSignificantBits = buffer.getLong();
        long mostSignificantBits = buffer.getLong();
        return new UUID(mostSignificantBits, leastSignificantBits);
    }
}
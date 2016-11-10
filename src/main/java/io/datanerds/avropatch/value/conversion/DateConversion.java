package io.datanerds.avropatch.value.conversion;

import io.datanerds.avropatch.schema.CustomTypes.DateType;
import org.apache.avro.Conversion;
import org.apache.avro.LogicalType;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericFixed;

import java.nio.ByteBuffer;
import java.util.Date;

/**
 * This class is an implementation of a timestamp representing the number of milliseconds since
 * January 1, 1970, 00:00:00 GMT. Avro's built-in logical type <tt>timestamp-millis</tt> is not used since it can only
 * be used with an underlying long type which collides with primitive long in union for value schema.
 *
 * @see Conversion
 * @see org.apache.avro.LogicalTypes.TimestampMillis
 * @see org.apache.avro.Schema.Type
 */
public class DateConversion extends Conversion<Date> {

    static {
        LogicalTypes.register(DateType.NAME, schema -> DateType.LOGICAL_TYPE);
    }

    @Override
    public Schema getRecommendedSchema() {
        return DateType.SCHEMA;
    }

    @Override
    public Class<Date> getConvertedType() {
        return Date.class;
    }

    @Override
    public String getLogicalTypeName() {
        return DateType.NAME;
    }

    @Override
    public GenericFixed toFixed(Date value, Schema schema, LogicalType type) {
        return new GenericFixed() {
            @Override
            public byte[] bytes() {
                ByteBuffer buffer = ByteBuffer.allocate(DateType.SIZE);
                buffer.putLong(value.getTime());
                return buffer.array();
            }

            @Override
            public Schema getSchema() {
                return DateType.SCHEMA;
            }
        };
    }

    @Override
    public Date fromFixed(GenericFixed value, Schema schema, LogicalType type) {
        ByteBuffer buffer = ByteBuffer.wrap(value.bytes());
        long time = buffer.getLong();
        return new Date(time);
    }
}
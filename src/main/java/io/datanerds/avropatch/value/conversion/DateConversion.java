package io.datanerds.avropatch.value.conversion;

import org.apache.avro.Conversion;
import org.apache.avro.LogicalType;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;

import java.util.Date;

/**
 * This class is an implementation for Avro's built-in logical type <tt>timestamp-millis</tt> representing the
 * number of milliseconds since January 1, 1970, 00:00:00 GMT.
 *
 * @see Conversion
 * @see org.apache.avro.LogicalTypes.TimestampMillis
 */
public class DateConversion extends Conversion<Date> {
    private static final String NAME = LogicalTypes.timestampMillis().getName();
    public static final Schema SCHEMA = LogicalTypes.timestampMillis().addToSchema(Schema.create(Schema.Type.LONG));

    @Override
    public Schema getRecommendedSchema() {
        return SCHEMA;
    }

    @Override
    public Class<Date> getConvertedType() {
        return Date.class;
    }

    @Override
    public String getLogicalTypeName() {
        return NAME;
    }

    @Override
    public Long toLong(Date value, Schema schema, LogicalType type) {
        return value.getTime();
    }

    @Override
    public Date fromLong(Long value, Schema schema, LogicalType type) {
        return new Date(value);
    }
}
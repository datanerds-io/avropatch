package io.datanerds.avropatch.value.conversion;

import org.apache.avro.Conversion;
import org.apache.avro.LogicalType;
import org.apache.avro.LogicalTypes;
import org.apache.avro.generic.GenericData;

/**
 * This abstract class serves as base class for all custom type conversions supported by this library.
 * @param <T>
 */
public abstract class AvroConversion<T> extends Conversion<T> {

    public void register(GenericData data) {
        data.addLogicalTypeConversion(this);
    }

    protected static void registerLogicalType(String name, LogicalType type) {
        LogicalTypes.register(name, schema -> type);
    }

}

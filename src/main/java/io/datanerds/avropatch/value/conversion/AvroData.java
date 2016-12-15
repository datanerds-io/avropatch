package io.datanerds.avropatch.value.conversion;

import avro.shaded.com.google.common.collect.ImmutableList;
import org.apache.avro.Conversion;
import org.apache.avro.reflect.ReflectData;

import java.util.List;

public final class AvroData extends ReflectData {

    public static final List<Conversion<?>> CONVERTERS = ImmutableList.of(
            new DateConversion(),
            new BigIntegerConversion(),
            new BigDecimalConversion(),
            new UUIDConversion()
    );

    private static final AvroData INSTANCE = new AvroData();

    private AvroData() {
        CONVERTERS.forEach(this::addLogicalTypeConversion);
    }

    public static AvroData get() {
        return INSTANCE;
    }
}

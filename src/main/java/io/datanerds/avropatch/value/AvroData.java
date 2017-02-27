package io.datanerds.avropatch.value;

import io.datanerds.avropatch.value.conversion.BigDecimalConversion;
import io.datanerds.avropatch.value.conversion.BigIntegerConversion;
import io.datanerds.avropatch.value.conversion.DateConversion;
import io.datanerds.avropatch.value.conversion.UUIDConversion;
import org.apache.avro.Conversion;
import org.apache.avro.reflect.ReflectData;

import java.util.Arrays;
import java.util.List;

public final class AvroData extends ReflectData {

    private static final List<Conversion<?>> CONVERTERS = Arrays.asList(
            new DateConversion(),
            new BigIntegerConversion(),
            new BigDecimalConversion(),
            new UUIDConversion()
    );

    private static final AvroData INSTANCE = new AvroData();

    private AvroData() {
        CONVERTERS.forEach(this::addLogicalTypeConversion);
    }

    /**
     * Hides {@link ReflectData#get()} and returns {@link ReflectData} subclass initialized with custom converters.
     * @see ReflectData#get()
     * @see DateConversion
     * @see BigIntegerConversion
     * @see BigDecimalConversion
     * @see UUIDConversion
     * @return singleton instance
     */
    public static AvroData get() {
        return INSTANCE;
    }
}

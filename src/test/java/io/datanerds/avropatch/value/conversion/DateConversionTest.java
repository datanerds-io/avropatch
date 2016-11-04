package io.datanerds.avropatch.value.conversion;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.avro.Schema;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

public class DateConversionTest {

    @Test
    public void serializesSingleValue() throws IOException {
        ConversionTester
                .withSchemata(DateConversion.SCHEMA)
                .withConverters(new DateConversion())
                .reserializeAndAssert(new Date())
                .reserializeAndAssert(new Date());
    }

    @Test
    public void serializesList() throws IOException {
        ConversionTester
                .withSchemata(Schema.createArray(DateConversion.SCHEMA))
                .withConverters(new DateConversion())
                .reserializeAndAssert(ImmutableList.of(new Date(), new Date(), new Date(), new Date()));

    }

    @Test
    public void serializesMap() throws IOException {
        ConversionTester
                .withSchemata(Schema.createMap(DateConversion.SCHEMA))
                .withConverters(new DateConversion())
                .reserializeAndAssert(ImmutableMap.of(
                        "key 1", new Date(),
                        "key 2", new Date(),
                        "key 3", new Date()));
    }
}
package io.datanerds.avropatch.value.conversion;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.datanerds.avropatch.value.type.UuidType;
import org.apache.avro.Schema;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

public class UUIDConversionTest {

    @Test
    public void serializesSingleValue() throws IOException {
        ConversionTester
                .withSchemata(UuidType.SCHEMA)
                .withConverters(new UUIDConversion())
                .reserializeAndAssert(UUID.randomUUID())
                .reserializeAndAssert(UUID.randomUUID());
    }

    @Test
    public void serializesList() throws IOException {
        ConversionTester
                .withSchemata(Schema.createArray(UuidType.SCHEMA))
                .withConverters(new UUIDConversion())
                .reserializeAndAssert(ImmutableList.of(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()));
    }

    @Test
    public void serializesMap() throws IOException {
        ConversionTester
                .withSchemata(Schema.createMap(UuidType.SCHEMA))
                .withConverters(new UUIDConversion())
                .reserializeAndAssert(ImmutableMap.of(
                        "key 1", UUID.randomUUID(),
                        "key 2", UUID.randomUUID(),
                        "key 3", UUID.randomUUID()));
    }
}
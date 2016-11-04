package io.datanerds.avropatch.value.conversion;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.avro.Schema;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;

public class BigDecimalConversionTest {

    @Test
    public void serializesSingleValue() throws IOException {
        ConversionTester
                .withSchemata(BigDecimalConversion.SCHEMA)
                .withConverters(new BigDecimalConversion())
                .reserializeAndAssert(BigDecimal.valueOf(1234567890L))
                .reserializeAndAssert(new BigDecimal("1234567890123456.78901234567890123456789012345678901234567890"));
    }

    @Test
    public void serializesList() throws IOException {
        ConversionTester
                .withSchemata(Schema.createArray(BigDecimalConversion.SCHEMA))
                .withConverters(new BigDecimalConversion())
                .reserializeAndAssert(ImmutableList.of(BigDecimal.valueOf(1234567890L),
                        new BigDecimal("12345678901234567.8901234567890123456789012345678901234567890"),
                        new BigDecimal("9867451234560123985690347590378412095789345624401928453450891"
                                + "9894375093427509345702.349572349089034759349046758903758902347568902347589023467")));
    }

    @Test
    public void serializesMap() throws IOException {
        ConversionTester
                .withSchemata(Schema.createMap(BigDecimalConversion.SCHEMA))
                .withConverters(new BigDecimalConversion())
                .reserializeAndAssert(ImmutableMap.of(
                        "key 1", new BigDecimal("12345678901234567.8901234567890123456789012345678901234567890"),
                        "key 2", new BigDecimal("9867451231209578924401928453450891.4675890375890232347589023467")));
    }

}
package io.datanerds.avropatch.value.conversion;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.avro.Schema;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;

public class BigIntegerConversionTest {

    @Test
    public void serializesSingleValue() throws IOException {
        ConversionTester
                .withSchemata(BigIntegerConversion.SCHEMA)
                .withConverters(new BigIntegerConversion())
                .reserializeAndAssert(BigInteger.valueOf(1234567890L))
                .reserializeAndAssert(new BigInteger("123456789012345678901234567890123456789012345678901234567890"));
    }

    @Test
    public void serializesList() throws IOException {
        ConversionTester
                .withSchemata(Schema.createArray(BigIntegerConversion.SCHEMA))
                .withConverters(new BigIntegerConversion())
                .reserializeAndAssert(ImmutableList.of(BigInteger.valueOf(1234567890L),
                        new BigInteger("123456789012345678901234567890123456789012345678901234567890"),
                        new BigInteger("9867451234560123985690347590378412095789345624401928453450891"
                                + "98943750934275093457023495723490890347590349046758903758902347568902347589023467")));
    }

    @Test
    public void serializesMap() throws IOException {
        ConversionTester
                .withSchemata(Schema.createMap(BigIntegerConversion.SCHEMA))
                .withConverters(new BigIntegerConversion())
                .reserializeAndAssert(ImmutableMap.of(
                        "key 1", new BigInteger("123456789012345678901234567890123456789012345678901234567890"),
                        "key 2", new BigInteger("875634975613486341456634912563464598436589234658974365134")));
    }
}
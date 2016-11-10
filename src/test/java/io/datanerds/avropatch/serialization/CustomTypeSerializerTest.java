package io.datanerds.avropatch.serialization;

import avro.shaded.com.google.common.collect.ImmutableList;
import io.datanerds.avropatch.Patch;
import io.datanerds.avropatch.operation.Add;
import io.datanerds.avropatch.operation.Path;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.*;

public class CustomTypeSerializerTest {

    @Ignore("work in progress")
    @Test
    public void test() throws IOException {
        CustomTypeSerializer serializer = new CustomTypeSerializer.Builder().withBigDecimal().build();
        Patch patch = new Patch(ImmutableList.of(new Add(Path.of("hello"), new BigDecimal("23946712384.4"))));
        byte[] bytes = serializer.toBytes(patch);
        Patch newPatch = serializer.toObject(bytes);
    }

}
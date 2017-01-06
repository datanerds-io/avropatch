package io.datanerds.avropatch.serialization;

import com.google.common.collect.ImmutableList;
import io.datanerds.avropatch.Patch;
import io.datanerds.avropatch.operation.Add;
import io.datanerds.avropatch.operation.Copy;
import io.datanerds.avropatch.operation.Path;
import io.datanerds.avropatch.operation.Replace;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.UUID;

import static io.datanerds.avropatch.operation.matcher.PatchMatcher.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CustomTypeSerializerTest {

    @Test
    public void withCustomTypes() throws IOException {
        CustomTypeSerializer serializer = new CustomTypeSerializer.Builder().withCustomTypes().build();
        Patch patch = new Patch(ImmutableList.of(new Add(Path.of("hello"), new BigDecimal("23946712384.49879324"))));
        byte[] bytes = serializer.toBytes(patch);
        assertThat(patch, is(equalTo(serializer.toObject(bytes))));
    }

    @Test
    public void withCustomClass() throws IOException {
        CustomTypeSerializer serializer = new CustomTypeSerializer.Builder()
                .withArray()
                    .nullable()
                    .withPrimitives()
                    .withCustomTypes()
                .endArray()
                .withType(Bimmel.class)
                .build();
        Patch patch = new Patch(ImmutableList.of(
                new Replace(Path.of("hello"), new Bimmel("string", 42, UUID.randomUUID(), new Bimmel.Bommel("Gaga")))));
        byte[] bytes = serializer.toBytes(patch);
        assertThat(patch, is(equalTo(serializer.toObject(bytes))));
    }


    @Test
    public void withMultipleOperations() throws IOException {
        CustomTypeSerializer serializer = new CustomTypeSerializer.Builder()
                .withArray()
                    .withPrimitives()
                    .withCustomTypes()
                .endArray()
                .nullable()
                .withPrimitives()
                .withType(Bimmel.class)
                .build();
        Patch patch = new Patch(ImmutableList.of(
                new io.datanerds.avropatch.operation.Test(Path.of("hello", "world"), null),
                new Add(Path.of("hello", "world"), "string"),
                new Replace(Path.of("hello"), new Bimmel("string", 42, UUID.randomUUID(), new Bimmel.Bommel("Gaga"))),
                new Copy(Path.of("from", "here"), Path.of("to", "there")),
                new Add(Path.of("oO"), ImmutableList.of(new BigInteger("897696124"), 42, new BigInteger("4796923435"))),
                new Replace(Path.of("lol"), ImmutableList.of(new Date(), new Date(), new Date()))));
        byte[] bytes = serializer.toBytes(patch);
        assertThat(patch, is(equalTo(serializer.toObject(bytes))));
    }
}
package io.datanerds.avropatch.serialization;

import com.google.common.collect.ImmutableList;
import io.datanerds.avropatch.Patch;
import io.datanerds.avropatch.operation.Add;
import io.datanerds.avropatch.operation.Copy;
import io.datanerds.avropatch.operation.Path;
import io.datanerds.avropatch.operation.Replace;
import io.datanerds.avropatch.value.Bimmel;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PatchMapperTest {

    private final UUID resource = UUID.randomUUID();

    @Test
    public void withCustomTypes() throws IOException {
        PatchMapper serializer = PatchMapper.builder().withCustomTypes().build();
        Patch patch = Patch.from(resource,
                ImmutableList.of(new Add<>(Path.of("hello"), new BigDecimal("23946712384.49879324"))));
        byte[] bytes = serializer.toBytes(patch);
        assertThat(patch, is(equalTo(serializer.toPatch(bytes))));
    }

    @Test
    public void withCustomClass() throws IOException {
        PatchMapper serializer = PatchMapper.builder()
                .with(PatchMapper.arrayBuilder()
                        .nullable()
                        .withAvroPrimitives()
                        .withCustomTypes()
                        .build())
                .withType(Bimmel.class)
                .build();
        Patch patch = Patch.from(resource, ImmutableList.of(
                new Replace<>(Path.of("hello"), new Bimmel("string", 42, UUID.randomUUID(), new Bimmel.Bommel("Gaga")))));
        byte[] bytes = serializer.toBytes(patch);
        assertThat(patch, is(equalTo(serializer.toPatch(bytes))));
    }

    @Test
    public void withMultipleOperations() throws IOException {
        PatchMapper serializer = PatchMapper.builder()
                .with(PatchMapper.arrayBuilder()
                        .withAvroPrimitives()
                        .withCustomTypes()
                        .build())
                .nullable()
                .withAvroPrimitives()
                .withType(Bimmel.class)
                .build();
        Patch patch = Patch.from(resource, ImmutableList.of(
                new io.datanerds.avropatch.operation.Test<>(Path.of("hello", "world"), null),
                new Add<>(Path.of("hello", "world"), "string"),
                new Replace<>(Path.of("hello"), new Bimmel("string", 42, UUID.randomUUID(), new Bimmel.Bommel("Gaga"))),
                new Copy(Path.of("from", "here"), Path.of("to", "there")),
                new Add<>(Path.of("oO"), ImmutableList.of(new BigInteger("897696124"), 42, new BigInteger("4796923435"))),
                new Replace<>(Path.of("lol"), ImmutableList.of(new Date(), new Date(), new Date()))));
        byte[] bytes = serializer.toBytes(patch);
        assertThat(patch, is(equalTo(serializer.toPatch(bytes))));
    }
}
package io.datanerds.avropatch.serialization;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.datanerds.avropatch.Patch;
import io.datanerds.avropatch.operation.*;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class DefaultPatchMapperTest {

    private final PatchMapper mapper = new PatchMapper();
    private final UUID resource = UUID.randomUUID();

    @Test
    public void serializesAdd() throws IOException {
        Patch patch = patchOf(new Add<>(Path.of("person", "name"), "John Doe"));
        byte[] bytes = mapper.toBytes(patch);

        Patch deserialized = mapper.toPatch(bytes);
        assertThat(deserialized.size(), is(equalTo(1)));
        assertThat(deserialized.getOperation(0), is(equalTo(new Add<>(Path.of("person", "name"), "John Doe"))));
    }

    @Test
    public void serializesCopy() throws IOException {
        Patch patch = patchOf(new Copy(Path.parse("/person/firstName"), Path.parse("/person/lastName")));
        byte[] bytes = mapper.toBytes(patch);

        Patch deserialized = mapper.toPatch(bytes);
        assertThat(deserialized.size(), is(equalTo(1)));
        assertThat(deserialized.getOperation(0), is(equalTo(new Copy(Path.parse("/person/firstName"), Path.parse("/person/lastName")))));
    }

    @Test
    public void serializesMove() throws IOException {
        Patch patch = patchOf(new Move(Path.parse("/person/firstName"), Path.parse("/person/lastName")));
        byte[] bytes = mapper.toBytes(patch);

        Patch deserialized = mapper.toPatch(bytes);
        assertThat(deserialized.size(), is(equalTo(1)));
        assertThat(deserialized.getOperation(0), is(equalTo(new Move(Path.parse("/person/firstName"), Path.parse("/person/lastName")))));
    }

    @Test
    public void serializesRemove() throws IOException {
        Patch patch = patchOf(new Remove(Path.parse("/person/name")));
        byte[] bytes = mapper.toBytes(patch);

        Patch deserialized = mapper.toPatch(bytes);
        assertThat(deserialized.size(), is(equalTo(1)));
        assertThat(deserialized.getOperation(0), is(equalTo(new Remove(Path.parse("/person/name")))));
    }

    @Test
    public void serializesReplace() throws IOException {
        Patch patch = patchOf(new Replace<>(Path.parse("/person/number"), 42));
        byte[] bytes = mapper.toBytes(patch);

        Patch deserialized = mapper.toPatch(bytes);
        assertThat(deserialized.size(), is(equalTo(1)));
        assertThat(deserialized.getOperation(0), is(equalTo(new Replace<>(Path.parse("/person/number"), 42))));
    }

    @Test
    public void serializesTest() throws IOException {
        Patch patch = patchOf(new io.datanerds.avropatch.operation.Test<>(Path.parse("/person/number"), 42L));
        byte[] bytes = mapper.toBytes(patch);

        Patch deserialized = mapper.toPatch(bytes);
        assertThat(deserialized.size(), is(equalTo(1)));
        assertThat(deserialized.getOperation(0),
                is(equalTo(new io.datanerds.avropatch.operation.Test<>(Path.parse("/person/number"), 42L))));
    }

    @Test
    public void serializesBunchOfOperations() throws IOException {
        Patch patch = new Patch<>(resource, ImmutableList.of(
                new Add<>(Path.of("person", "name"), "John Doe"),
                new Copy(Path.parse("/person/firstName"), Path.parse("/person/lastName")),
                new Move(Path.parse("/person/firstName"), Path.parse("/person/lastName")),
                new Remove(Path.parse("/person/name")),
                new Replace<>(Path.parse("/person/number"), 42),
                new io.datanerds.avropatch.operation.Test<>(Path.parse("/person/number"), 42L)));

        byte[] bytes = mapper.toBytes(patch);
        assertThat(patch, is(equalTo(mapper.toPatch(bytes))));

        ByteBuffer buffer = mapper.toByteBuffer(patch);
        assertThat(patch, is(equalTo(mapper.toPatch(buffer))));
    }

    @Test
    public void serializesDefaultValueTypes() throws IOException {
        Date date = new Date();
        UUID uuid = UUID.randomUUID();
        Patch<UUID> patch = new Patch<>(resource, ImmutableList.of(
                new Add<>(Path.of("some", "value"), "John Doe"),
                new Add<>(Path.of("some", "value"), 42),
                new Add<>(Path.of("some", "value"), 42L),
                new Add<>(Path.of("some", "value"), uuid),
                new Add<>(Path.of("some", "value"), new BigDecimal("128976548936549275.9674592348654789")),
                new Add<>(Path.of("some", "value"), new BigInteger("90374692364523789623490569234562347895")),
                new Add<>(Path.of("some", "value"), true),
                new Add<>(Path.of("some", "value"), date),
                new Add<>(Path.of("some", "value"), 4234.2345)));

        byte[] bytes = mapper.toBytes(patch);
        assertThat(patch, is(equalTo(mapper.toPatch(bytes))));

        ByteBuffer buffer = mapper.toByteBuffer(patch);
        assertThat(patch, is(equalTo(mapper.toPatch(buffer))));
    }

    @Test
    public void serializesArbitraryHeadersWithoutOperations() throws IOException {
        Patch patch = new Patch<>(resource, Collections.emptyList(), ImmutableMap.of(
                "header 1", UUID.randomUUID(),
                "header 2", new Date(),
                "header 3", 1234L,
                "header 4", new BigDecimal("3214123453.123512345"))
        );
        byte[] bytes = mapper.toBytes(patch);
        assertThat(patch, is(equalTo(mapper.toPatch(bytes))));

        ByteBuffer buffer = mapper.toByteBuffer(patch);
        assertThat(patch, is(equalTo(mapper.toPatch(buffer))));
    }

    @Test
    public void serializesArbitraryHeadersWithOperations() throws IOException {
        Patch patch = new Patch<>(ImmutableMap.of(
                "header 1", UUID.randomUUID(),
                "header 2", new Date(),
                "header 3", 1234L,
                "header 4", new BigDecimal("3214123453.123512345")), ImmutableList.of(
                    new Copy(Path.of("from", "here"), Path.of("to", "there")),
                    new Move(Path.of("from", "here"), Path.of("to", "there")))
        );
        byte[] bytes = mapper.toBytes(patch);
        assertThat(patch, is(equalTo(mapper.toPatch(bytes))));

        ByteBuffer buffer = mapper.toByteBuffer(patch);
        assertThat(patch, is(equalTo(mapper.toPatch(buffer))));
    }

    private <T extends Operation> Patch patchOf(T operation) {
        return new Patch<>(resource, ImmutableList.of(operation));
    }
}

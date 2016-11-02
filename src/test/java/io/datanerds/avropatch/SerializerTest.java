package io.datanerds.avropatch;

import com.google.common.collect.ImmutableList;
import io.datanerds.avropatch.operation.*;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static io.datanerds.avropatch.operation.matcher.OperationMatchers.hasItem;
import static io.datanerds.avropatch.operation.matcher.OperationMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;


public class SerializerTest {

    @Test
    public void serializesAdd() throws IOException {
        Patch patch = new Patch();
        patch.addOperation(new Add<>(Path.of("person", "name"), "John Doe"));
        byte[] bytes = Serializer.toBytes(patch);

        List<Operation> operations = Serializer.toPatch(bytes).getOperations();
        assertThat(operations, hasSize(1));
        assertThat(operations, hasItem(new Add<>(Path.of("person", "name"), "John Doe")));
    }

    @Test
    public void serializesCopy() throws IOException {
        Patch patch = new Patch();
        patch.addOperation(new Copy(Path.parse("/person/firstName"), Path.parse("/person/lastName")));
        byte[] bytes = Serializer.toBytes(patch);

        List<Operation> operations = Serializer.toPatch(bytes).getOperations();
        assertThat(operations, hasSize(1));
        assertThat(operations, hasItem(new Copy(Path.parse("/person/firstName"), Path.parse("/person/lastName"))));
    }

    @Test
    public void serializesMove() throws IOException {
        Patch patch = new Patch();
        patch.addOperation(new Move(Path.parse("/person/firstName"), Path.parse("/person/lastName")));
        byte[] bytes = Serializer.toBytes(patch);

        List<Operation> operations = Serializer.toPatch(bytes).getOperations();
        assertThat(operations, hasSize(1));
        assertThat(operations, hasItem(new Move(Path.parse("/person/firstName"), Path.parse("/person/lastName"))));
    }

    @Test
    public void serializesRemove() throws IOException {
        Patch patch = new Patch();
        patch.addOperation(new Remove(Path.parse("/person/name")));
        byte[] bytes = Serializer.toBytes(patch);

        List<Operation> operations = Serializer.toPatch(bytes).getOperations();
        assertThat(operations, hasSize(1));
        assertThat(operations, hasItem(new Remove(Path.parse("/person/name"))));
    }

    @Test
    public void serializesReplace() throws IOException {
        Patch patch = new Patch();
        patch.addOperation(new Replace(Path.parse("/person/number"), 42));
        byte[] bytes = Serializer.toBytes(patch);

        List<Operation> operations = Serializer.toPatch(bytes).getOperations();
        assertThat(operations, hasItem(new Replace(Path.parse("/person/number"), 42)));
    }

    @Test
    public void serializesTest() throws IOException {
        Patch patch = new Patch();
        patch.addOperation(new io.datanerds.avropatch.operation.Test(Path.parse("/person/number"), 42L));
        byte[] bytes = Serializer.toBytes(patch);

        List<Operation> operations = Serializer.toPatch(bytes).getOperations();
        assertThat(operations, hasSize(1));
        assertThat(operations, hasItem(new io.datanerds.avropatch.operation.Test(Path.parse("/person/number"), 42L)));
    }

    @Test
    public void serializesBunchOfOperations() throws IOException {
        Patch patch = new Patch(ImmutableList.of(new Add<>(Path.of("person", "name"), "John Doe"),
                new Copy(Path.parse("/person/firstName"), Path.parse("/person/lastName")),
                new Move(Path.parse("/person/firstName"), Path.parse("/person/lastName")),
                new Remove(Path.parse("/person/name")),
                new Replace(Path.parse("/person/number"), 42),
                new io.datanerds.avropatch.operation.Test(Path.parse("/person/number"), 42L)));

        byte[] bytes = Serializer.toBytes(patch);
        List<Operation> operations = Serializer.toPatch(bytes).getOperations();
        assertThat(operations, hasSize(6));
        assertThat(operations, hasItems(new Add<>(Path.of("person", "name"), "John Doe"),
                new Copy(Path.parse("/person/firstName"), Path.parse("/person/lastName")),
                new Move(Path.parse("/person/firstName"), Path.parse("/person/lastName")),
                new Remove(Path.parse("/person/name")),
                new Replace(Path.parse("/person/number"), 42),
                new io.datanerds.avropatch.operation.Test(Path.parse("/person/number"), 42L)));
    }

}
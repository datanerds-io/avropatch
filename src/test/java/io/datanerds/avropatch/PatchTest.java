package io.datanerds.avropatch;

import avro.shaded.com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableList;
import com.google.common.testing.EqualsTester;
import io.datanerds.avropatch.operation.Operation;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.datanerds.avropatch.operation.OperationGenerator.randomOperation;

public class PatchTest {

    private final Map<String, ?> headers = ImmutableMap.of("test 1", "bla", "test 2", UUID.randomUUID());
    private final List<Operation> operations = ImmutableList.of(randomOperation(), randomOperation(), randomOperation());

    @Test
    public void equality() {
        new EqualsTester()
                .addEqualityGroup(new Patch(operations, headers), new Patch(operations, headers))
                .addEqualityGroup(new Patch(operations))
                .addEqualityGroup(new Patch(ImmutableList.of(randomOperation()), headers))
                .addEqualityGroup(new Patch(ImmutableList.of(randomOperation())))
                .testEquals();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void immutableOperations() {
        new Patch(operations, headers).getOperations().add(randomOperation());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void immutableHeaders() {
        new Patch(operations, headers).getHeaders().remove("test 1");
    }
}
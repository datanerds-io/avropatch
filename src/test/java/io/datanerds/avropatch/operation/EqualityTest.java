package io.datanerds.avropatch.operation;

import com.google.common.testing.EqualsTester;
import org.junit.Test;

import static io.datanerds.avropatch.operation.OperationGenerator.createSomeOperations;

public class EqualityTest {

    @Test
    public void equality() {
        new EqualsTester()
                .addEqualityGroup(createSomeOperations(OperationGenerator::add))
                .addEqualityGroup(new Add<>(Path.of("hello"), "test 1"), new Add<>(Path.of("hello"), "test 1"))
                .addEqualityGroup(new Copy(Path.of("from", "here"), Path.of("to", "there")),
                new Copy(Path.of("from", "here"), Path.of("to", "there")))
                .addEqualityGroup(new Copy(Path.of("foo", "bar"), Path.of("barz", "123")),
                        new Copy(Path.of("foo", "bar"), Path.of("barz", "123")))
                .addEqualityGroup(new Move(Path.of("from", "here"), Path.of("to", "there")),
                        new Move(Path.of("from", "here"), Path.of("to", "there")))
                .addEqualityGroup(new Move(Path.of("foo", "bar"), Path.of("barz", "123")),
                        new Move(Path.of("foo", "bar"), Path.of("barz", "123")))
                .addEqualityGroup(new Remove(Path.of("from", "here")), new Remove(Path.of("from", "here")))
                .addEqualityGroup(new Remove(Path.of("foo", "bar")), new Remove(Path.of("foo", "bar")))
                .addEqualityGroup(createSomeOperations(OperationGenerator::replace))
                .addEqualityGroup(createSomeOperations(OperationGenerator::test))
                .testEquals();
    }
}

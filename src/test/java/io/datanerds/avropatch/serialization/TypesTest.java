package io.datanerds.avropatch.serialization;

import io.datanerds.avropatch.operation.*;
import org.junit.Test;

import java.io.IOException;

import static io.datanerds.avropatch.serialization.OperationSerializationTester.createSomeOperations;

public class TypesTest {

    @Test
    public void add() throws IOException {
        new OperationSerializationTester(Add.class)
                .reserializeAndAssert(createSomeOperations(value -> new Add<>(Path.of("hello", "world"), value)));
    }

    @Test
    public void copy() throws IOException {
        new OperationSerializationTester(Copy.class)
                .reserializeAndAssert(new Copy(Path.of("from", "here"), Path.of("to", "there")));
    }

    @Test
    public void move() throws IOException {
        new OperationSerializationTester(Move.class)
            .reserializeAndAssert(new Move(Path.of("from", "here"), Path.of("to", "there")));
    }

    @Test
    public void remove() throws IOException {
        new OperationSerializationTester(Remove.class)
                .reserializeAndAssert(new Remove(Path.of("from", "here")));
    }

    @Test
    public void replace() throws IOException {
        new OperationSerializationTester(Replace.class).reserializeAndAssert(
                createSomeOperations(value -> new Replace<>(Path.of("hello"), value)));
    }

    @Test
    public void test() throws IOException {
        new OperationSerializationTester(io.datanerds.avropatch.operation.Test.class)
                .reserializeAndAssert(
                        createSomeOperations(
                                value -> new io.datanerds.avropatch.operation.Test<>(Path.of("hello", "world"), value)));
    }
}
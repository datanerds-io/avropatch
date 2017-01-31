package io.datanerds.avropatch.serialization;

import io.datanerds.avropatch.operation.*;
import org.junit.Test;

import java.io.IOException;

import static io.datanerds.avropatch.operation.OperationGenerator.createSomeOperations;

public class TypesTest {

    @Test
    public void add() throws IOException {
        new OperationSerializationTester(Add.class).reserializeAndAssert(createSomeOperations(OperationGenerator::add));
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
        new OperationSerializationTester(Replace.class)
                .reserializeAndAssert(createSomeOperations(OperationGenerator::replace));
    }

    @Test
    public void test() throws IOException {
        new OperationSerializationTester(io.datanerds.avropatch.operation.Test.class)
                .reserializeAndAssert( createSomeOperations(OperationGenerator::test));
    }
}
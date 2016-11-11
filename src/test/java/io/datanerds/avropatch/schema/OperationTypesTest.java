package io.datanerds.avropatch.schema;

import com.google.common.collect.ImmutableMap;
import io.datanerds.avropatch.operation.*;
import org.apache.avro.Schema;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;

import static io.datanerds.avropatch.operation.matcher.OperationMatchers.equalTo;
import static io.datanerds.avropatch.schema.CustomTypes.VALUE_TYPE_UNION;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class OperationTypesTest {

    @Test
    public void add() throws IOException {
        OperationTester tester = new OperationTester(Add.class);
        List<Operation> adds = tester.createSomeOperations(value -> new Add<>(Path.of("hello", "world"), value));
        for (Operation add : adds) {
            assertThat(add, is(equalTo(tester.reserialize(add))));
        }
    }

    @Test
    public void copy() throws IOException {
        SerializationTester<Operation> tester = new OperationTester(Copy.class);
        Copy copy = new Copy(Path.of("from", "here"), Path.of("to", "there"));
        assertThat(copy, is(equalTo(tester.reserialize(copy))));
    }

    @Test
    public void move() throws IOException {
        SerializationTester<Operation> tester = new OperationTester(Move.class);
        Move copy = new Move(Path.of("from", "here"), Path.of("to", "there"));
        assertThat(copy, is(equalTo(tester.reserialize(copy))));
    }

    @Test
    public void remove() throws IOException {
        SerializationTester<Operation> tester = new OperationTester(Remove.class);
        Remove remove = new Remove(Path.of("from", "here"));
        assertThat(remove, is(equalTo(tester.reserialize(remove))));
    }

    @Test
    public void replace() throws IOException {
        OperationTester tester = new OperationTester(Replace.class);
        List<Operation> replaces = tester.createSomeOperations(value -> new Replace<>(Path.of("hello"), value));
        for (Operation replace : replaces) {
            assertThat(replace, is(equalTo(tester.reserialize(replace))));
        }
    }

    @Test
    public void test() throws IOException {
        OperationTester tester = new OperationTester(io.datanerds.avropatch.operation.Test.class);
        List<Operation> tests = tester.createSomeOperations(
                value -> new io.datanerds.avropatch.operation.Test<>(Path.of("hello", "world"), value));
        for (Operation test : tests) {
            assertThat(test, is(equalTo(tester.reserialize(test))));
        }
    }

    static class OperationTester extends SerializationTester<Operation> {

        private static final Map<Class<? extends Operation>, Schema> schemata = new ImmutableMap.Builder()
                .put(Add.class, OperationTypes.Add.create(VALUE_TYPE_UNION))
                .put(Copy.class, OperationTypes.Copy.create())
                .put(Move.class, OperationTypes.Move.create())
                .put(Remove.class, OperationTypes.Remove.create())
                .put(Replace.class, OperationTypes.Replace.create(VALUE_TYPE_UNION))
                .put(io.datanerds.avropatch.operation.Test.class, OperationTypes.Test.create(VALUE_TYPE_UNION))
                .build();

        public OperationTester(Class<? extends Operation> clazz) {
            super(schemata.get(clazz));
        }

        public static List<Operation> createSomeOperations(Function<Object, Operation> operationFunction) {
            List<Operation> operations = new ArrayList<>();
            operations.add(createOperation(operationFunction, "hello world"));
            operations.add(createOperation(operationFunction, 42));
            operations.add(createOperation(operationFunction, 42L));
            operations.add(createOperation(operationFunction, 123.456d));
            operations.add(createOperation(operationFunction, 123.456f));
            operations.add(createOperation(operationFunction, new BigInteger("83647896845639495762501378945698056348956")));
            operations.add(createOperation(operationFunction, new BigDecimal("956740578902345.56734895627895")));
            operations.add(createOperation(operationFunction, UUID.randomUUID()));
            operations.add(createOperation(operationFunction, new Date()));

            return operations;
        }

        private static <T> Operation createOperation(Function<T, Operation> operationFunction, T value) {
            return operationFunction.apply(value);
        }
    }
}
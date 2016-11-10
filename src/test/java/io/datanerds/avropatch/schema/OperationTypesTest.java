package io.datanerds.avropatch.schema;

import io.datanerds.avropatch.operation.*;
import io.datanerds.avropatch.value.conversion.*;
import org.apache.avro.Schema;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
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

    Schema.Parser parser;

    @Before
    public void setup() throws IOException {
        AvroData.get();
        Map<String, Schema> types = new HashMap<>();
        types.put(Add.class.getSimpleName(), annotateNamespace(OperationTypes.Add.create(VALUE_TYPE_UNION)));
        types.put(Copy.class.getSimpleName(), annotateNamespace(OperationTypes.Copy.create()));
        types.put(Move.class.getSimpleName(), annotateNamespace(OperationTypes.Move.create()));
        types.put(Remove.class.getSimpleName(), annotateNamespace(OperationTypes.Remove.create()));
        types.put(Replace.class.getSimpleName(), annotateNamespace(OperationTypes.Replace.create(VALUE_TYPE_UNION)));
        types.put(io.datanerds.avropatch.operation.Test.class.getSimpleName(),
                annotateNamespace(OperationTypes.Test.create(VALUE_TYPE_UNION)));

        parser = new Schema.Parser();
        parser.addTypes(types);
    }

    @Test
    public void add() throws IOException {
        System.out.println(OperationTypes.Add.create(VALUE_TYPE_UNION).toString());
        Schema schema = parse(Add.class);
        SerializationTester<Operation> tester = new SerializationTester(schema);

        List<Operation> adds = createSomeOperations(value -> new Add<>(Path.of("hello", "world"), value));
        for (Operation add : adds) {
            assertThat(add, is(equalTo(tester.reserialize(add))));
        }
    }

    @Test
    public void copy() throws IOException {
        Schema schema = parse(Copy.class);
        SerializationTester<Operation> tester = new SerializationTester(schema);

        Copy copy = new Copy(Path.of("from", "here"), Path.of("to", "there"));
        assertThat(copy, is(equalTo(tester.reserialize(copy))));
    }

    @Test
    public void move() throws IOException {
        Schema schema = parse(Move.class);
        SerializationTester<Operation> tester = new SerializationTester(schema);

        Move copy = new Move(Path.of("from", "here"), Path.of("to", "there"));
        assertThat(copy, is(equalTo(tester.reserialize(copy))));
    }

    @Test
    public void remove() throws IOException {
        Schema schema = parse(Remove.class);
        SerializationTester<Operation> tester = new SerializationTester(schema);

        Remove remove = new Remove(Path.of("from", "here"));
        assertThat(remove, is(equalTo(tester.reserialize(remove))));
    }

    @Test
    public void replace() throws IOException {
        Schema schema = parse(Replace.class);
        SerializationTester<Operation> tester = new SerializationTester(schema);

        List<Operation> replaces = createSomeOperations(value -> new Replace<>(Path.of("hello", "world"), value));
        for (Operation replace : replaces) {
            assertThat(replace, is(equalTo(tester.reserialize(replace))));
        }
    }

    @Test
    public void test() throws IOException {
        Schema schema = parse(io.datanerds.avropatch.operation.Test.class);
        SerializationTester<Operation> tester = new SerializationTester(schema);

        List<Operation> tests = createSomeOperations(
                value -> new io.datanerds.avropatch.operation.Test<>(Path.of("hello", "world"), value));
        for (Operation test : tests) {
            assertThat(test, is(equalTo(tester.reserialize(test))));
        }
    }

    private Schema parse(Class<? extends Operation> clazz) {
        return parser.parse(String.format("\"%s\"", clazz.getName()));
    }

    private List<Operation> createSomeOperations(Function<Object, Operation> operationFunction) {
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

    private <T> Operation createOperation(Function<T, Operation> operationFunction, T value) {
        return operationFunction.apply(value);
    }

    private static Schema annotateNamespace(Schema schema) throws IOException {
        ObjectMapper mappper = new ObjectMapper();
        Map<String, Object> jsonSchema = mappper.readValue(schema.toString(), Map.class);
        jsonSchema.put("namespace", Operation.class.getPackage().getName());
        Schema.Parser parser = new Schema.Parser();
        return parser.parse(mappper.writeValueAsString(jsonSchema));
    }
}
package io.datanerds.avropatch.schema;

import com.google.common.collect.ImmutableMap;
import io.datanerds.avropatch.operation.*;
import io.datanerds.avropatch.serialization.OperationTypes;
import io.datanerds.avropatch.value.type.BigDecimalType;
import io.datanerds.avropatch.value.type.BigIntegerType;
import io.datanerds.avropatch.value.type.TimestampType;
import io.datanerds.avropatch.value.type.UuidType;
import org.apache.avro.Schema;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;

import static io.datanerds.avropatch.operation.matcher.OperationMatchers.equalTo;
import static io.datanerds.avropatch.value.type.PrimitiveType.*;
import static org.apache.avro.Schema.createUnion;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class OperationSerializationTester extends SerializationTester<Operation> {

    private static final Schema VALUE_TYPE_UNION = createUnion(BOOLEAN.getSchema(), DOUBLE.getSchema(),
            FLOAT.getSchema(), INTEGER.getSchema(), LONG.getSchema(), NULL.getSchema(), STRING.getSchema(),
            BigDecimalType.SCHEMA, BigIntegerType.SCHEMA, TimestampType.SCHEMA, UuidType.SCHEMA);

    private static final Map<Class<? extends Operation>, Schema> SCHEMATA = new ImmutableMap.Builder()
            .put(Add.class, OperationTypes.Add.create(VALUE_TYPE_UNION))
            .put(Copy.class, OperationTypes.Copy.create())
            .put(Move.class, OperationTypes.Move.create())
            .put(Remove.class, OperationTypes.Remove.create())
            .put(Replace.class, OperationTypes.Replace.create(VALUE_TYPE_UNION))
            .put(io.datanerds.avropatch.operation.Test.class, OperationTypes.Test.create(VALUE_TYPE_UNION))
            .build();

    public OperationSerializationTester(Class<? extends Operation> clazz) {
        super(SCHEMATA.get(clazz));
    }

    public static List<Operation> createSomeOperations(Function<Object, Operation> operationFunction) {
        List<Operation> operations = new ArrayList<>();
        operations.add(createOperation(operationFunction, "hello world"));
        operations.add(createOperation(operationFunction, 42));
        operations.add(createOperation(operationFunction, 42L));
        operations.add(createOperation(operationFunction, 123.456d));
        operations.add(createOperation(operationFunction, 123.456f));
        operations.add(createOperation(operationFunction, new BigInteger("8364789684563949576378945698056348956")));
        operations.add(createOperation(operationFunction, new BigDecimal("956740578902345.56734895627895")));
        operations.add(createOperation(operationFunction, UUID.randomUUID()));
        operations.add(createOperation(operationFunction, new Date()));

        return operations;
    }

    @Override
    public void reserializeAndAssert(Operation value) throws IOException {
        Operation operation = reserialize(value);
        assertThat(value, is(equalTo(operation)));
    }

    public void reserializeAndAssert(List<Operation> operations) throws IOException {
        for (Operation operation : operations) {
            reserializeAndAssert(operation);
        }
    }

    private static <T> Operation createOperation(Function<T, Operation> operationFunction, T value) {
        return operationFunction.apply(value);
    }
}
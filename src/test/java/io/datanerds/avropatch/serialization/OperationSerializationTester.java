package io.datanerds.avropatch.serialization;

import com.google.common.collect.ImmutableMap;
import io.datanerds.avropatch.operation.*;
import io.datanerds.avropatch.value.type.BigDecimalType;
import io.datanerds.avropatch.value.type.BigIntegerType;
import io.datanerds.avropatch.value.type.TimestampType;
import io.datanerds.avropatch.value.type.UuidType;
import org.apache.avro.Schema;

import java.io.IOException;
import java.util.*;

import static io.datanerds.avropatch.value.type.PrimitiveType.*;
import static org.apache.avro.Schema.createUnion;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class OperationSerializationTester extends SerializationTester<Operation> {

    private static final Schema VALUE_TYPE_UNION = createUnion(BOOLEAN.getSchema(), DOUBLE.getSchema(),
            FLOAT.getSchema(), INTEGER.getSchema(), LONG.getSchema(), NULL.getSchema(), STRING.getSchema(),
            BigDecimalType.SCHEMA, BigIntegerType.SCHEMA, TimestampType.SCHEMA, UuidType.SCHEMA);

    private static final Map<Class<? extends Operation>, Schema> SCHEMATA = new ImmutableMap.Builder()
            .put(Add.class, Types.Add.create(VALUE_TYPE_UNION))
            .put(Copy.class, Types.Copy.create())
            .put(Move.class, Types.Move.create())
            .put(Remove.class, Types.Remove.create())
            .put(Replace.class, Types.Replace.create(VALUE_TYPE_UNION))
            .put(io.datanerds.avropatch.operation.Test.class, Types.Test.create(VALUE_TYPE_UNION))
            .build();

    public OperationSerializationTester(Class<? extends Operation> clazz) {
        super(SCHEMATA.get(clazz));
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
}
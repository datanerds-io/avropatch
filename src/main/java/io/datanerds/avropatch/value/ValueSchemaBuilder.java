package io.datanerds.avropatch.value;

import io.datanerds.avropatch.serialization.PatchMapper;
import io.datanerds.avropatch.value.type.BigDecimalType;
import io.datanerds.avropatch.value.type.BigIntegerType;
import io.datanerds.avropatch.value.type.TimestampType;
import io.datanerds.avropatch.value.type.UuidType;
import org.apache.avro.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static io.datanerds.avropatch.value.type.PrimitiveType.*;

/**
 * This abstract class provides a builder skeleton for constructing an object depending on an Avro {@link Schema} of
 * type {@code Union}. E.g., it enables defining the Avro {@link Schema} used to describe the generic type for the
 * values of {@link io.datanerds.avropatch.operation.Add}, {@link io.datanerds.avropatch.operation.Replace} and
 * {@link io.datanerds.avropatch.operation.Test} operations by passing in the appropriate functional to the constructor.
 * Essentially, it creates a {@link org.apache.avro.Schema.UnionSchema} holding all types specified via its API and
 * calls the instantiator functional with that particualr {@link Schema} instance.
 * @see PatchMapper PatchMapper for implementations.
 */
public abstract class ValueSchemaBuilder<T> {
    private static Logger logger = LoggerFactory.getLogger(ValueSchemaBuilder.class);
    private final Function<Schema, T> instantiator;
    private final List<Schema> types;

    protected ValueSchemaBuilder(Function<Schema, T> instantiator) {
        Objects.nonNull(instantiator);
        this.instantiator = instantiator;
        this.types = new ArrayList<>();
    }

    public T build() {
        Schema schema = makeSchema();
        logger.debug("Generated schema {}", schema);
        return instantiator.apply(schema);
    }

    /**
     * Allows values for {@link io.datanerds.avropatch.operation.Add},
     * {@link io.datanerds.avropatch.operation.Replace} and {@link io.datanerds.avropatch.operation.Test} to be
     * nullable.
     *
     * @return the builder instance
     */
    public ValueSchemaBuilder<T> nullable() {
        types.add(NULL.getSchema());
        return this;
    }

    /**
     * Adds primitives supported by Avro out of the box to the {@link Schema} defining the value types for
     * {@link io.datanerds.avropatch.operation.Add}, {@link io.datanerds.avropatch.operation.Replace} and
     * {@link io.datanerds.avropatch.operation.Test}, namely {@link Boolean}, {@link Double}, {@link Integer},
     * {@link Long} and {@link String}.
     *
     * @return the builder instance
     */
    public ValueSchemaBuilder<T> withAvroPrimitives() {
        types.addAll(Arrays.asList(BOOLEAN.getSchema(), DOUBLE.getSchema(), FLOAT.getSchema(), INTEGER.getSchema(),
                LONG.getSchema(), STRING.getSchema()));
        return this;
    }

    /**
     * Adds custom types to the {@link Schema} defining the value types for
     * {@link io.datanerds.avropatch.operation.Add}, {@link io.datanerds.avropatch.operation.Replace} and
     * {@link io.datanerds.avropatch.operation.Test} with custom converters registered in {@link PatchMapper},
     * namely {@link java.math.BigDecimal}, {@link java.math.BigInteger}, {@link java.util.Date} and
     * {@link java.util.UUID}.
     *
     * @return the builder instance
     * @see io.datanerds.avropatch.value.conversion.BigDecimalConversion
     * @see io.datanerds.avropatch.value.conversion.BigIntegerConversion
     * @see io.datanerds.avropatch.value.conversion.DateConversion
     * @see io.datanerds.avropatch.value.conversion.UUIDConversion
     */
    public ValueSchemaBuilder<T> withCustomTypes() {
        types.addAll(
                Arrays.asList(BigDecimalType.SCHEMA, BigIntegerType.SCHEMA, TimestampType.SCHEMA, UuidType.SCHEMA));
        return this;
    }

    /**
     * Generates a {@link Schema} for the given type via reflections and adds it to set of supported types.
     * @param type to be added to the {@code Union}
     * @return the builder instance
     */
    public ValueSchemaBuilder<T> withType(Type type) {
        return with(AvroData.get().getSchema(type));
    }

    /**
     * Adds the schema to the {@link org.apache.avro.Schema.UnionSchema} defining the value types for
     * {@link io.datanerds.avropatch.operation.Add}, {@link io.datanerds.avropatch.operation.Replace} and
     * {@link io.datanerds.avropatch.operation.Test}. Be aware that you might need to register your
     * {@link org.apache.avro.Conversion} implementation if the {@code schema} contains non default types.
     * @param schema to be added  to the {@code Union}
     * @return the builder instance
     */
    public ValueSchemaBuilder<T> with(Schema schema) {
        types.add(schema);
        return this;
    }

    private Schema makeSchema() {
        if (types.size() == 1) {
            return types.get(0);
        } else {
            return Schema.createUnion(types);
        }
    }
}
package io.datanerds.avropatch.value;

import avro.shaded.com.google.common.collect.Lists;
import io.datanerds.avropatch.serialization.PatchMapper;
import io.datanerds.avropatch.value.type.*;
import org.apache.avro.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.datanerds.avropatch.value.type.PrimitiveType.NULL;

/**
 * This abstract class provides a builder skeleton for constructing an object depending on an Avro {@link Schema} of
 * type {@code Union}. E.g., it enables defining the Avro {@link Schema} used to describe the generic type for the
 * values of {@link io.datanerds.avropatch.operation.Add}, {@link io.datanerds.avropatch.operation.Replace} and
 * {@link io.datanerds.avropatch.operation.Test} operations by passing in the appropriate functional to the constructor.
 * Essentially, it creates a {@code UnionSchema} holding all types specified via its API and
 * calls the instantiator functional with that particular {@link Schema} instance.
 * @see PatchMapper PatchMapper for usage.
 */
public class ValueSchemaBuilder<T> {

    private static final List<Schema> AVRO = Stream.of(PrimitiveType.values())
            .filter(type -> type != NULL)
            .map(PrimitiveType::getSchema)
            .collect(Collectors.toList());

    private static final List<Schema> CUSTOM = Arrays.asList(
            BigDecimalType.SCHEMA, BigIntegerType.SCHEMA, TimestampType.SCHEMA, UuidType.SCHEMA);

    private static Logger logger = LoggerFactory.getLogger(ValueSchemaBuilder.class);
    private final Function<Schema, T> instantiator;
    private final Set<Schema> types;

    public ValueSchemaBuilder(Function<Schema, T> instantiator) {
        Objects.nonNull(instantiator);
        this.instantiator = instantiator;
        this.types = new HashSet<>();
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
        types.addAll(AVRO);
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
        types.addAll(CUSTOM);
        return this;
    }

    /**
     * Shortcut for {@code withAvroPrimitives().withCustomTypes()}.
     * @see #withCustomTypes()
     * @see #withAvroPrimitives()
     * @return the builder instance
     */
    public ValueSchemaBuilder<T> withSupportedTypes() {
        return withAvroPrimitives().withCustomTypes();
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
     * Adds the schema to the {@code UnionSchema} defining the value types for
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

    /**
     * Instantiates a builder for creating an Avro {@link Schema} of type {@code map}.
     * @return the builder instance
     */
    public static ValueSchemaBuilder<Schema> mapBuilder() {
        return new ValueSchemaBuilder<>(Schema::createMap);
    }

    /**
     * Instantiates a builder for creating an Avro {@link Schema} of type {@code array}.
     * @return the builder instance
     */
    public static ValueSchemaBuilder<Schema> arrayBuilder() {
        return new ValueSchemaBuilder<>(Schema::createArray);
    }

    /**
     * Instantiates a builder for creating an Avro {@link Schema} of type {@code union}.
     * @return the builder instance
     */
    public static ValueSchemaBuilder<Schema> unionBuilder() {
        return new ValueSchemaBuilder<>(schema -> schema);
    }

    private Schema makeSchema() {
        if (types.size() == 1) {
            return types.iterator().next();
        } else {
            return Schema.createUnion(Lists.newArrayList(types));
        }
    }
}
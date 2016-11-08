package io.datanerds.avropatch.value.conversion;

import org.apache.avro.Conversion;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.io.*;
import org.apache.avro.reflect.ReflectData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public final class ConversionTester<T> {

    private final DatumWriter<T> writer;
    private final DatumReader<T> reader;
    private final ReflectData data;

    private ConversionTester(Schema schema) {
        Objects.nonNull(schema);
        data = new ReflectData();
        reader = data.createDatumReader(schema);
        writer = data.createDatumWriter(schema);
    }

    public static final <T> ConversionTester<T> withSchemata(Schema... types) {
        Objects.nonNull(types);
        if (types.length < 1) {
            throw new IllegalArgumentException("At least one type needed for schema");
        }

        SchemaBuilder.UnionAccumulator<Schema> union = SchemaBuilder.unionOf().type(types[0]);
        for (int i = 1; i < types.length; i++) {
            union.and().type(types[i]);
        }
        return new ConversionTester(union.endUnion());
    }

    public ConversionTester<T> withConverters(Conversion<?>... converters) {
        Arrays.asList(converters).forEach(conversion -> data.addLogicalTypeConversion(conversion));
        return this;
    }

    public ConversionTester<T> reserializeAndAssert(T value) throws IOException {
        byte[] bytes = toBytes(value);
        T object = toObject(bytes);
        assertEquality(value, object);
        return this;
    }

    private void assertEquality(T value, T object) {
        if (value instanceof Collection) {
            // Avro serializes collections as arrays
            Collection collection = (Collection) value;
            assertThat(collection.toArray(), is(equalTo(object)));
        } else {
            assertThat(object, is(equalTo(value)));
        }
    }

    private byte[] toBytes(T value) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Encoder binaryEncoder = EncoderFactory.get().directBinaryEncoder(outputStream, null);

        writer.write(value, binaryEncoder);
        return outputStream.toByteArray();
    }

    private T toObject(byte[] bytes) throws IOException {
        return reader.read(null, DecoderFactory.get().binaryDecoder(bytes, null));
    }
}
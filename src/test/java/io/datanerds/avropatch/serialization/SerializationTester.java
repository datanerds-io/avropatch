package io.datanerds.avropatch.serialization;

import io.datanerds.avropatch.value.AvroData;
import org.apache.avro.Schema;
import org.apache.avro.io.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SerializationTester<T> {

    private final DatumWriter<T> writer;
    private final DatumReader<T> reader;

    public SerializationTester(Schema schema) {
        Objects.nonNull(schema);
        writer = AvroData.get().createDatumWriter(schema);
        reader = AvroData.get().createDatumReader(schema);
    }

    public T reserialize(T value) throws IOException {
        byte[] bytes = toBytes(value);
        return toObject(bytes);
    }

    public void reserializeAndAssert(T value) throws IOException {
        T object = reserialize(value);
        assertEquality(object, value);
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

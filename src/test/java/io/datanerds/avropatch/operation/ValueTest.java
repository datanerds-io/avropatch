package io.datanerds.avropatch.operation;

import com.google.common.collect.ImmutableList;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericContainer;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.ReflectDatumReader;
import org.apache.avro.reflect.ReflectDatumWriter;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ValueTest {

    private static final Schema schema = ReflectData.get().getSchema(TestValue.class);

    @Test
    public void simpleValues() throws IOException {
        verify(new TestValue<>(42));
        verify(new TestValue<>(42L));
        verify(new TestValue<>((float)42.1234));
        verify(new TestValue<>(42.1234));
        verify(new TestValue<>("Hello World!"));
    }

    @Test
    public void listValues() throws IOException {
        verify(new TestValue<>(ImmutableList.of(12, 21, 42, 99)));
        verify(new TestValue<>(ImmutableList.of(12L, 21L, 42L, 99L)));
        verify(new TestValue<>(ImmutableList.of((float)12.34, (float)45.6789)));
        verify(new TestValue<>(ImmutableList.of(12.34, 45.6789)));
        verify(new TestValue<>(ImmutableList.of("Hello", "World", "!")));
    }

    private <T> void verify(TestValue<T> value) throws IOException {
        GenericRecord record = toAvro(value, schema);
        TestValue<?> object = toObject(record, schema);
        assertThat(value.value, is(equalTo(object.value)));
    }

    public static <T extends GenericContainer, R> R toObject(T avro, Schema schema) throws IOException {
        DatumWriter<T> writer = new GenericDatumWriter<>(schema);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writer.write(avro, EncoderFactory.get().directBinaryEncoder(out, null));
        DatumReader<R> reader = new ReflectDatumReader<>(schema);
        return reader.read(null, DecoderFactory.get().binaryDecoder(out.toByteArray(), null));
    }

    public static <T, R extends GenericContainer> R toAvro(T object, Schema schema) throws IOException {
        DatumWriter<T> writer = new ReflectDatumWriter<>(schema);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writer.write(object, EncoderFactory.get().directBinaryEncoder(out, null));
        DatumReader<R> reader = new GenericDatumReader<>(schema);
        return reader.read(null, DecoderFactory.get().binaryDecoder(out.toByteArray(), null));

    }
}

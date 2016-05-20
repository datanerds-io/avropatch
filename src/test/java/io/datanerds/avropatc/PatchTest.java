package io.datanerds.avropatc;

import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.google.common.collect.ImmutableList;
import io.datanerds.avropatch.Patch;
import io.datanerds.avropatch.operation.Add;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PatchTest {

    private static final Logger logger = LoggerFactory.getLogger(PatchTest.class);

    @Test
    public void debug() {
        Schema schema = ReflectData.get().getSchema(Patch.class);
        logger.debug("Schema for Patch: {}", schema);
    }

    @Test
    public void addString() throws IOException {
        Schema schema = ReflectData.get().getSchema(Add.class);
        Add<String> add = new Add<>(JsonPointer.of("person", "name"), "hello");
        GenericRecord record = toAvro(add, schema);
        Add<String> add2 = toObject(record, schema);
        assertThat(add.value, is(equalTo(add2.value)));
        assertThat(add.path, is(equalTo(add2.path)));
    }

    @Test
    public void addStringList() throws IOException {
        Schema schema = ReflectData.get().getSchema(Add.class);
        Add<List<String>> add = new Add<>(JsonPointer.of("person", "name"), ImmutableList.of("hello", "world"));
        GenericRecord record = toAvro(add, schema);
        Add<String> add2 = toObject(record, schema);
        assertThat(add.value, is(equalTo(add2.value)));
        assertThat(add.path, is(equalTo(add2.path)));
    }

    private <T extends GenericContainer, R> R toObject(T avro, Schema schema) throws IOException {
        DatumWriter<T> writer = new GenericDatumWriter<>(schema);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writer.write(avro, EncoderFactory.get().directBinaryEncoder(out, null));
        DatumReader<R> reader = new ReflectDatumReader<>(schema);
        return reader.read(null, DecoderFactory.get().binaryDecoder(out.toByteArray(), null));
    }

    private <T, R extends GenericContainer> R toAvro(T object, Schema schema) throws IOException {
        DatumWriter<T> writer = new ReflectDatumWriter<>(schema);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writer.write(object, EncoderFactory.get().directBinaryEncoder(out, null));
        DatumReader<R> reader = new GenericDatumReader<>(schema);
        return reader.read(null, DecoderFactory.get().binaryDecoder(out.toByteArray(), null));

    }

}
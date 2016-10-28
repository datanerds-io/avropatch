package io.datanerds.avropatch;

import com.google.common.collect.ImmutableList;
import io.datanerds.avropatch.operation.Add;
import io.datanerds.avropatch.operation.Path;
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

import static io.datanerds.avropatch.TestUtil.toAvro;
import static io.datanerds.avropatch.TestUtil.toObject;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PatchTest {

    private static final Logger logger = LoggerFactory.getLogger(PatchTest.class);

    @Test
    public void debug() {
        Schema schema = ReflectData.get().getSchema(Patch.class);
        logger.info("Schema for Patch: {}", schema);
    }

    @Test
    public void addString() throws IOException {
        Schema schema = ReflectData.get().getSchema(Add.class);
        Add<String> add = new Add<>(Path.of("person", "name"), "hello");
        GenericRecord record = toAvro(add, schema);
        Add<String> add2 = toObject(record, schema);
        assertThat(add.value, is(equalTo(add2.value)));
        assertThat(add.path, is(equalTo(add2.path)));
    }

    @Test
    public void addStringList() throws IOException {
        Schema schema = ReflectData.get().getSchema(Add.class);
        Add<List<String>> add = new Add<>(Path.of("person", "name"), ImmutableList.of("hello", "world"));
        GenericRecord record = toAvro(add, schema);
        Add<String> add2 = toObject(record, schema);
        assertThat(add.value, is(equalTo(add2.value)));
        assertThat(add.path, is(equalTo(add2.path)));
    }

}
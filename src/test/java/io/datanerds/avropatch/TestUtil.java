package io.datanerds.avropatch;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericContainer;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.reflect.ReflectDatumReader;
import org.apache.avro.reflect.ReflectDatumWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TestUtil {

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

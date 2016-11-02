package io.datanerds.avropatch;

import org.apache.avro.Schema;
import org.apache.avro.io.*;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.ReflectDatumReader;
import org.apache.avro.reflect.ReflectDatumWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Serializer {

    private static final Schema schema = ReflectData.get().getSchema(Patch.class);
    private static final DatumWriter<Patch> writer = new ReflectDatumWriter<>(schema);

    public static byte[] toBytes(Patch patch) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Encoder binaryEncoder = EncoderFactory.get().directBinaryEncoder(outputStream, null);

        writer.write(patch, binaryEncoder);
        return outputStream.toByteArray();
    }

    public static Patch toPatch(byte[] bytes) throws IOException {
        DatumReader<Patch> reader = new ReflectDatumReader<>(schema);
        return reader.read(null, DecoderFactory.get().binaryDecoder(bytes, null));
    }
}
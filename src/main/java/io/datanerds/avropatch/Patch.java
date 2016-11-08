package io.datanerds.avropatch;

import avro.shaded.com.google.common.collect.ImmutableList;
import io.datanerds.avropatch.operation.Operation;
import io.datanerds.avropatch.value.conversion.*;
import org.apache.avro.Schema;
import org.apache.avro.io.*;
import org.apache.avro.reflect.ReflectData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Patch {

    private static final PatchSerializer SERIALIZER = new PatchSerializer();
    public static final Schema SCHEMA = SERIALIZER.schema;

    private final List<Operation> operations;

    public Patch() {
        operations = new ArrayList<>();
    }

    public Patch(List<Operation> operations) {
        this.operations = new ArrayList<>(operations);
    }

    public boolean addOperation(Operation operation) {
        return operations.add(operation);
    }

    public List<Operation> getOperations() {
        return Collections.unmodifiableList(operations);
    }

    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Encoder binaryEncoder = EncoderFactory.get().directBinaryEncoder(outputStream, null);
        SERIALIZER.writer.write(this, binaryEncoder);
        return outputStream.toByteArray();
    }

    public static Patch of(byte[] bytes) throws IOException {
        return SERIALIZER.reader.read(null, DecoderFactory.get().binaryDecoder(bytes, null));
    }

    private static class PatchSerializer {

        static final List<AvroConversion<?>> AVRO_CONVERSIONS =
                ImmutableList.of(new DateConversion(), new BigIntegerConversion(), new BigDecimalConversion(),
                        new UUIDConversion());
        final DatumWriter<Patch> writer;
        final DatumReader<Patch> reader;
        final Schema schema;

        private PatchSerializer() {
            ReflectData data = new ReflectData();
            AVRO_CONVERSIONS.forEach(avroConversion -> avroConversion.register(data));
            schema = data.getSchema(Patch.class);

            writer = data.createDatumWriter(schema);
            reader = data.createDatumReader(schema);
        }
    }
}

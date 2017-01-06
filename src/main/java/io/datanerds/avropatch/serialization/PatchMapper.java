package io.datanerds.avropatch.serialization;

import io.datanerds.avropatch.Patch;
import io.datanerds.avropatch.value.AvroData;
import io.datanerds.avropatch.value.ValueSchemaBuilder;
import org.apache.avro.Schema;
import org.apache.avro.io.*;

import javax.annotation.concurrent.ThreadSafe;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * This mapper provides functionality for converting between {@link Patch} objects and {@code byte}[] facilitating Avro.
 */
@ThreadSafe
public final class PatchMapper {
    private final DatumWriter<Patch> writer;
    private final DatumReader<Patch> reader;

    /**
     * Constructs the default {@link PatchMapper} with support for values of type {@link Boolean}, {@link Double},
     * {@link Integer}, {@link Long}, {@link String}, {@link java.math.BigDecimal}, {@link java.math.BigInteger},
     * {@link java.util.Date} and {@link java.util.UUID}.
     */
    public PatchMapper() {
        this(AvroData.get().getSchema(Patch.class));
    }

    @SuppressWarnings("unchecked") // Assignment is safe since we're always passing in a Patch schema
    private PatchMapper(Schema schema) {
        this.writer = AvroData.get().createDatumWriter(schema);
        this.reader = AvroData.get().createDatumReader(schema);
    }

    public byte[] toBytes(Patch value) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Encoder binaryEncoder = EncoderFactory.get().directBinaryEncoder(outputStream, null);
        writer.write(value, binaryEncoder);
        return outputStream.toByteArray();
    }

    public Patch toPatch(byte[] bytes) throws IOException {
        return reader.read(null, DecoderFactory.get().binaryDecoder(bytes, null));
    }

    public static final ValueSchemaBuilder<PatchMapper> builder() {
        return new Builder();
    }

    public static final ValueSchemaBuilder<Schema> arrayBuilder() {
        return new ArraySchemaBuilder();
    }

    public static final ValueSchemaBuilder<Schema> mapBuilder() {
        return new MapSchemaBuilder();
    }

    private static final class Builder extends ValueSchemaBuilder<PatchMapper> {
        private Builder() {
            super(schema -> new PatchMapper(PatchType.create(schema)));
        }
    }

    private static class ArraySchemaBuilder extends ValueSchemaBuilder<Schema> {
        private ArraySchemaBuilder() {
            super(schema -> Schema.createArray(schema));
        }
    }

    private static class MapSchemaBuilder extends ValueSchemaBuilder<Schema> {
        private MapSchemaBuilder() {
            super(schema -> Schema.createMap(schema));
        }
    }
}

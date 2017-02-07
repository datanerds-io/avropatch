package io.datanerds.avropatch.serialization;

import io.datanerds.avropatch.Patch;
import io.datanerds.avropatch.value.AvroData;
import io.datanerds.avropatch.value.ValueSchemaBuilder;
import org.apache.avro.Schema;
import org.apache.avro.io.*;

import javax.annotation.concurrent.ThreadSafe;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * This mapper provides functionality for converting between {@link Patch} objects and {@code byte}[] facilitating Avro.
 */
@ThreadSafe
public final class PatchMapper {
    private final DatumWriter<Patch<?>> writer;
    private final DatumReader<Patch<?>> reader;

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

    public <T> byte[] toBytes(Patch<T> value) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Encoder binaryEncoder = EncoderFactory.get().directBinaryEncoder(outputStream, null);
        writer.write(value, binaryEncoder);
        return outputStream.toByteArray();
    }

    @SuppressWarnings("unchecked")
    public <T> Patch<T> toPatch(byte[] bytes) throws IOException {
        return (Patch<T>) reader.read(null, DecoderFactory.get().binaryDecoder(bytes, null));
    }

    /**
     * Reads a {@link Patch} from a {@link ByteBuffer} assuming that its current position is set to zero.
     * @param buffer buffer of Avro bytes
     * @param <T> the expected resource type
     * @return the deserialized {@link Patch} object
     * @throws IOException if reading from {@link java.nio.Buffer} fails
     */
    public <T> Patch<T> toPatch(ByteBuffer buffer) throws IOException {
        return toPatch(toBytes(buffer));
    }

    public <T> ByteBuffer toByteBuffer(Patch<T> value) throws IOException {
        return toByteBuffer(toBytes(value));
    }

    private static ByteBuffer toByteBuffer(byte[] bytes) throws IOException {
        return ByteBuffer.wrap(bytes);
    }

    private static byte[] toBytes(ByteBuffer buffer) throws IOException {
        if (buffer.hasArray()) {
            return buffer.array();
        }
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return bytes;
    }

    /**
     * Instantiates a builder for creating a {@link PatchMapper} which may be used as follows.
     * <blockquote><pre>
     * PatchMapper mapper = PatchMapper.builder()
     *          .nullable()
     *          .withAvroPrimitives()
     *          .withCustomTypes()
     *          .build();
     * Patch patch = new Patch(...);
     * byte[] bytes = serializer.toBytes(patch);
     * </pre></blockquote>
     * @see ValueSchemaBuilder
     * @see PatchMapper
     * @return the builder instance
     */
    public static ValueSchemaBuilder<PatchMapper> builder() {
        return new ValueSchemaBuilder<>(schema -> new PatchMapper(Types.Patch.create(schema)));
    }

    /**
     * Instantiates a builder for creating an Avro {@link Schema} of type {@code array} which may be used for the
     * generic value of {@link io.datanerds.avropatch.operation.Add}, {@link io.datanerds.avropatch.operation.Replace}
     * and {@link io.datanerds.avropatch.operation.Test} operations by registering the resulting {@link Schema} to a
     * {@link PatchMapper}.
     * <blockquote><pre>
     * PatchMapper mapper = PatchMapper.builder()
     *          .with(PatchMapper.arrayBuilder()
     *                  .nullable()
     *                  .withAvroPrimitives()
     *                  .build())
     *          .build();
     * Patch patch = new Patch(...);
     * byte[] bytes = serializer.toBytes(patch);
     * </pre></blockquote>
     * @see ValueSchemaBuilder#arrayBuilder()
     * @see Schema
     * @see #builder()
     * @return the builder instance
     */
    public static ValueSchemaBuilder<Schema> arrayBuilder() {
        return ValueSchemaBuilder.arrayBuilder();
    }

    /**
     * Instantiates a builder for creating an Avro {@link Schema} of type {@code map} which may be used for the
     * generic value of {@link io.datanerds.avropatch.operation.Add}, {@link io.datanerds.avropatch.operation.Replace}
     * and {@link io.datanerds.avropatch.operation.Test} operations by registering the resulting {@link Schema} to a
     * {@link PatchMapper}.
     * <blockquote><pre>
     * PatchMapper mapper = PatchMapper.builder()
     *          .with(PatchMapper.mapBuilder()
     *                  .nullable()
     *                  .withAvroPrimitives()
     *                  .build())
     *          .build();
     * Patch patch = new Patch(...);
     * byte[] bytes = serializer.toBytes(patch);
     * </pre></blockquote>
     * @see ValueSchemaBuilder#mapBuilder()
     * @see Schema
     * @see #builder()
     * @return the builder instance
     */
    public static ValueSchemaBuilder<Schema> mapBuilder() {
        return ValueSchemaBuilder.mapBuilder();
    }
}

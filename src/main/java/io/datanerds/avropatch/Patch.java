package io.datanerds.avropatch;

import io.datanerds.avropatch.operation.DefaultSchema;
import io.datanerds.avropatch.operation.Operation;
import io.datanerds.avropatch.value.conversion.*;
import org.apache.avro.Schema;
import org.apache.avro.io.*;
import org.apache.avro.reflect.AvroSchema;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * This class represents a JSON PATCH operation holding a sequence of operations to apply to a given object.
 *
 * @see <a href="https://tools.ietf.org/html/rfc6902">https://tools.ietf.org/html/rfc6902</a>
 */
public class Patch {

    private static final PatchSerializer SERIALIZER = new PatchSerializer();

    @AvroSchema(DefaultSchema.HEADERS)
    private final Map<String, ?> headers;
    private final List<Operation> operations;

    public Patch() {
        this(new ArrayList<>(), new HashMap<>());
    }

    public Patch(List<Operation> operations) {
        this(new ArrayList<>(operations), new HashMap<>());
    }

    public Patch(List<Operation> operations, Map<String, ?> headers) {
        this.operations = new ArrayList<>(operations);
        this.headers = new HashMap<>(headers);
    }

    public boolean addOperation(Operation operation) {
        return operations.add(operation);
    }

    public List<Operation> getOperations() {
        return Collections.unmodifiableList(operations);
    }

    public Map<String, ?> getHeaders() {
        return Collections.unmodifiableMap(headers);
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
        final DatumWriter<Patch> writer;
        final DatumReader<Patch> reader;

        private PatchSerializer() {
            Schema schema = AvroData.get().getSchema(Patch.class);
            writer = AvroData.get().createDatumWriter(schema);
            reader = AvroData.get().createDatumReader(schema);
        }
    }
}

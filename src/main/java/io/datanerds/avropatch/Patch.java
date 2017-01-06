package io.datanerds.avropatch;

import io.datanerds.avropatch.value.DefaultSchema;
import io.datanerds.avropatch.operation.Operation;
import io.datanerds.avropatch.value.AvroData;
import org.apache.avro.Schema;
import org.apache.avro.io.*;
import org.apache.avro.reflect.AvroSchema;

import javax.annotation.concurrent.ThreadSafe;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * This class represents a JSON PATCH operation holding a sequence of operations to apply to a given object.
 *
 * @see <a href="https://tools.ietf.org/html/rfc6902">https://tools.ietf.org/html/rfc6902</a>
 */
@ThreadSafe
public class Patch {

    private static final PatchSerializer SERIALIZER = new PatchSerializer();

    @AvroSchema(DefaultSchema.HEADERS)
    private final Map<String, ?> headers;
    private final List<Operation> operations;

    @SuppressWarnings("unused") // no-arg constructor needed by Avro
    private Patch() {
        this(new ArrayList<>(), new HashMap<>());
    }

    public Patch(List<Operation> operations) {
        this(operations, Collections.EMPTY_MAP);
    }

    public Patch(List<Operation> operations, Map<String, ?> headers) {
        this.operations = new ArrayList<>(operations);
        this.headers = new HashMap<>(headers);
    }

    public List<Operation> getOperations() {
        return Collections.unmodifiableList(operations);
    }

    public Map<String, ?> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    public byte[] toBytes() throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Encoder encoder = EncoderFactory.get().directBinaryEncoder(output, null);
        SERIALIZER.writer.write(this, encoder);
        return output.toByteArray();
    }

    public static Patch of(byte[] bytes) throws IOException {
        return SERIALIZER.reader.read(null, DecoderFactory.get().binaryDecoder(bytes, null));
    }

    private static class PatchSerializer {
        final DatumWriter<Patch> writer;
        final DatumReader<Patch> reader;

        private PatchSerializer() {
            Schema schema = AvroData.get().getSchema(Patch.class);
            this.writer = AvroData.get().createDatumWriter(schema);
            this.reader = AvroData.get().createDatumReader(schema);
        }
    }
}

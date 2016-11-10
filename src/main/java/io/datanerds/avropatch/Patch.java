package io.datanerds.avropatch;

import io.datanerds.avropatch.operation.Operation;
import io.datanerds.avropatch.value.conversion.*;
import org.apache.avro.Schema;
import org.apache.avro.io.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents a JSON PATCH operation holding a sequence of operations to apply to a given object.
 *
 * @see <a href="https://tools.ietf.org/html/rfc6902">https://tools.ietf.org/html/rfc6902</a>
 */
public class Patch {

    private static final PatchSerializer SERIALIZER = new PatchSerializer();

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
        final DatumWriter<Patch> writer;
        final DatumReader<Patch> reader;

        private PatchSerializer() {
            Schema schema = AvroData.get().getSchema(Patch.class);

            writer = AvroData.get().createDatumWriter(schema);
            reader = AvroData.get().createDatumReader(schema);
        }
    }
}

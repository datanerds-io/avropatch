package io.datanerds.avropatch;

import io.datanerds.avropatch.value.DefaultSchema;
import io.datanerds.avropatch.operation.Operation;
import org.apache.avro.reflect.AvroSchema;
import java.util.*;

/**
 * This class represents a JSON PATCH operation holding a sequence of operations to apply to a given object.
 *
 * @see <a href="https://tools.ietf.org/html/rfc6902">https://tools.ietf.org/html/rfc6902</a>
 */
public class Patch {

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
}

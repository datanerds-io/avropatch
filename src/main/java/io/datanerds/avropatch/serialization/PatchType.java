package io.datanerds.avropatch.serialization;

import io.datanerds.avropatch.Patch;
import org.apache.avro.Schema;
import java.util.Arrays;

import static org.apache.avro.Schema.createRecord;

/**
 * This interface holds the schema constants for the {@link Patch} type.
 * @see Patch
 */
public interface PatchType {
    Object NO_DEFAULT = null;
    String NAME = Patch.class.getSimpleName();
    String DOC = "This record represents a PATCH holding a sequence of operations to apply to a given object.";
    static Schema create(Schema valueSchema) {
        Schema.Field operations = new Schema.Field("operations",
                Schema.createArray(OperationTypes.Operation.create(valueSchema)),
                "Sequence of operations to apply to a given object.", NO_DEFAULT);
        return createRecord(NAME, DOC, Patch.class.getPackage().getName(), false, Arrays.asList(operations));
    }
}

package io.datanerds.avropatch.schema;

import io.datanerds.avropatch.schema.CustomTypes.Path;
import org.apache.avro.Schema;

import java.util.Arrays;

import static io.datanerds.avropatch.schema.PrimitiveTypes.NO_DEFAULT;
import static org.apache.avro.Schema.*;
import static org.apache.avro.Schema.createRecord;

/**
 * This class holds schemata for all patch {@link io.datanerds.avropatch.operation.Operation}s. All schemata are not
 * bound to any specific namespace and therefore domain independent.
 * {@link io.datanerds.avropatch.Patch}/{@link io.datanerds.avropatch.schema.CustomTypes.PatchType} binds it to this
 * library's domain/namespace.
 */
public interface OperationTypes {

    interface Add {
        String NAME = io.datanerds.avropatch.operation.Add.class.getSimpleName();
        String DOC = "This record represents the add operation of RFC 6902 for JSON Patch'.";

        static Schema create(Schema valueSchema) {
            Field path = new Field("path", Path.SCHEMA, "Path pointing out where the JSON value should be added",
                    NO_DEFAULT);
            Field value = new Field("value", valueSchema, "Actual value to add to patched object", NO_DEFAULT);
            return createRecord(NAME, DOC, null, false, Arrays.asList(path, value));
        }
    }

    interface Copy {
        String NAME = io.datanerds.avropatch.operation.Copy.class.getSimpleName();
        String DOC = "This record represents the copy operation of RFC 6902 for JSON Patch'.";

        static Schema create() {
            Field from = new Field("from", Path.SCHEMA, "Source location of the value to be copied", NO_DEFAULT);
            Field path = new Field("path", Path.SCHEMA, "Target location of value to be copied", NO_DEFAULT);
            return createRecord(NAME, DOC, null, false, Arrays.asList(from, path));
        }
    }

    interface Move {
        String NAME = io.datanerds.avropatch.operation.Move.class.getSimpleName();
        String DOC = "This record represents the move operation of RFC 6902 for JSON Patch'.";

        static Schema create() {
            Field from = new Field("from", Path.SCHEMA, "Source location of the value to be moved", NO_DEFAULT);
            Field path = new Field("path", Path.SCHEMA, "Target location for the value to be moved to", NO_DEFAULT);
            return createRecord(NAME, DOC, null, false, Arrays.asList(from, path));
        }
    }

    interface Remove {
        String NAME = io.datanerds.avropatch.operation.Remove.class.getSimpleName();
        String DOC = "This record represents the remove operation of RFC 6902 for JSON Patch'.";

        static Schema create() {
            Field path = new Field("path", Path.SCHEMA, "Target location of value to be removed", NO_DEFAULT);
            return createRecord(NAME, DOC, null, false, Arrays.asList(path));
        }
    }

    interface Replace {
        String NAME = io.datanerds.avropatch.operation.Replace.class.getSimpleName();
        String DOC = "This record represents the replace operation of RFC 6902 for JSON Patch'.";

        static Schema create(Schema valueSchema) {
            Field path = new Field("path", Path.SCHEMA, "Path pointing out where the JSON value should be replaced",
                    NO_DEFAULT);
            Field value = new Field("value", valueSchema, "Actual value to replaced in patched object", NO_DEFAULT);
            return createRecord(NAME, DOC, null, false, Arrays.asList(path, value));
        }
    }

    interface Test {
        String NAME = io.datanerds.avropatch.operation.Test.class.getSimpleName();
        String DOC = "This record represents the test operation of RFC 6902 for JSON Patch'.";

        static Schema create(Schema valueSchema) {
            Field path = new Field("path", Path.SCHEMA, "Path pointing out which JSON value should be tested against",
                    NO_DEFAULT);
            Field value = new Field("value", valueSchema, "Actual value to test< against", NO_DEFAULT);
            return createRecord(NAME, DOC, null, false, Arrays.asList(path, value));
        }
    }

    interface Operation {
        static Schema create(Schema valueSchema) {
            return createUnion(Add.create(valueSchema), Copy.create(), Move.create(), Remove.create(),
                    Replace.create(valueSchema), Test.create(valueSchema));
        }
    }

}

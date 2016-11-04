package io.datanerds.avropatch.operation;

import org.apache.avro.Schema;

/**
 * This interface holds the schema for all valid value types.
 */
interface Value {
    String SCHEMA =
            "[" +
                    "\"boolean\"," +
                    "\"long\"," +
                    "\"int\"," +
                    "\"float\"," +
                    "\"double\"," +
                    "\"string\"," +
                    "{\"type\": \"array\", \"items\": [" +
                        "\"long\"," +
                        "\"int\"," +
                        "\"float\"," +
                        "\"double\"," +
                        "\"string\"" +
                    "]}" +
            "]";
}

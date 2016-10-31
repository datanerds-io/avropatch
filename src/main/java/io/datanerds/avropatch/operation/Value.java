package io.datanerds.avropatch.operation;

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

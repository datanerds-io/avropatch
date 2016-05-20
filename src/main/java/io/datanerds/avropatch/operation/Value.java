package io.datanerds.avropatch.operation;

class Value {
    protected static final String SCHEMA =
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

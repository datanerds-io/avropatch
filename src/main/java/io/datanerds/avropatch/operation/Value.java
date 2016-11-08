package io.datanerds.avropatch.operation;

/**
 * This interface holds the schema for all valid value types.
 */
interface Value {
    String SCHEMA =
            "[{"
                    + "    \"type\": \"record\","
                    + "    \"name\": \"decimal\","
                    + "    \"fields\": [{"
                    + "        \"name\": \"unscaledValue\","
                    + "        \"type\": {"
                    + "            \"type\": \"bytes\","
                    + "            \"logicalType\": \"big-integer\""
                    + "        }"
                    + "    }, {"
                    + "        \"name\": \"scale\","
                    + "        \"type\": \"int\""
                    + "    }],"
                    + "    \"logicalType\": \"big-decimal\""
                    + "}, {"
                    + "    \"type\": \"bytes\","
                    + "    \"logicalType\": \"big-integer\""
                    + "}, \"boolean\", {"
                    + "    \"type\": \"fixed\","
                    + "    \"name\": \"timestamp\","
                    + "    \"size\": 8,"
                    + "    \"logicalType\": \"timestamp\""
                    + "}, \"double\", \"float\", \"int\", \"long\", \"null\", \"string\", {"
                    + "    \"type\": \"fixed\","
                    + "    \"name\": \"uuid\","
                    + "    \"size\": 16,"
                    + "    \"logicalType\": \"uuid\""
                    + "}, {"
                    + "    \"type\": \"array\","
                    + "    \"items\": [\"decimal\", {"
                    + "        \"type\": \"bytes\","
                    + "        \"logicalType\": \"big-integer\""
                    + "    }, \"boolean\", \"timestamp\", \"double\", \"float\", \"int\", \"long\", \"null\", \"string\", \"uuid\"]"
                    + "}, {\n"
                    + "    \"type\": \"map\","
                    + "    \"values\": [\"decimal\", {"
                    + "        \"type\": \"bytes\","
                    + "        \"logicalType\": \"big-integer\""
                    + "    }, \"boolean\", \"timestamp\", \"double\", \"float\", \"int\", \"long\", \"null\", \"string\", \"uuid\"]"
                    + "}]";
}

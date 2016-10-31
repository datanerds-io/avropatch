package io.datanerds.avropatch.operation;

import org.apache.avro.reflect.AvroIgnore;

public final class Copy implements Operation {
    @AvroIgnore
    public static final String op = "copy";
    public final Path from;
    public final Path path;

    /**
     * Private no arg constructor for Avro deserializer.
     */
    private Copy() {
        from = null;
        path = null;
    }
    public Copy(Path from, Path path) {
        this.from = from;
        this.path = path;
    }
}

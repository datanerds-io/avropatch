package io.datanerds.avropatch.operation;

import org.apache.avro.reflect.AvroIgnore;

/**
 * This class represents the "copy" operation of RFC 6902 'JavaScript Object Notation (JSON) Patch'.
 *
 * @see <a href="https://tools.ietf.org/html/rfc6902#section-4.5">https://tools.ietf.org/html/rfc6902#section-4.5</a>
 */
public final class Copy implements Operation {
    @AvroIgnore
    public static final String op = "copy";
    public final Path from;
    public final Path path;

    private Copy() {
        from = null;
        path = null;
    }

    /**
     *
     * @param from Source location of the value to be copied
     * @param path Target location of value to be copied
     */
    public Copy(Path from, Path path) {
        this.from = from;
        this.path = path;
    }
}

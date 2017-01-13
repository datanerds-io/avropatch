package io.datanerds.avropatch.operation;

/**
 * This class represents the "copy" operation of RFC 6902 'JavaScript Object Notation (JSON) Patch'.
 *
 * @see <a href="https://tools.ietf.org/html/rfc6902#section-4.5">https://tools.ietf.org/html/rfc6902#section-4.5</a>
 */
public final class Copy implements Operation {
    public final Path from;
    public final Path path;

    @SuppressWarnings("unused") // no-arg constructor needed by Avro
    private Copy() {
        this(null, null);
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

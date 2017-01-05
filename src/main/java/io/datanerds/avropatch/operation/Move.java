package io.datanerds.avropatch.operation;

/**
 * This class represents the "move" operation of RFC 6902 'JavaScript Object Notation (JSON) Patch'.
 *
 * @see <a href="https://tools.ietf.org/html/rfc6902#section-4.4">https://tools.ietf.org/html/rfc6902#section-4.4</a>
 */
public final class Move implements Operation {
    public final Path from;
    public final Path path;

    @SuppressWarnings("unused") // no-arg constructor needed by Avro
    private Move() {
        this(null, null);
    }

    /**
     *
     * @param from Source location of the value to be moved
     * @param path Target location for the value to be moved to
     */
    public Move(Path from, Path path) {
        this.from = from;
        this.path = path;
    }
}
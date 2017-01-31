package io.datanerds.avropatch.operation;

import javax.annotation.Generated;
import java.util.Objects;

/**
 * This class represents the "remove" operation of RFC 6902 'JavaScript Object Notation (JSON) Patch'.
 *
 * @see <a href="https://tools.ietf.org/html/rfc6902#section-4.2">https://tools.ietf.org/html/rfc6902#section-4.2</a>
 */
public final class Remove implements Operation {
    public final Path path;

    @SuppressWarnings("unused") // no-arg constructor needed by Avro
    private Remove() {
        this(null);
    }

    /**
     *
     * @param path Target location of value to be removed
     */
    public Remove(Path path) {
        this.path = path;
    }

    @Override
    @Generated("IntelliJ IDEA")
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Remove remove = (Remove) o;
        return Objects.equals(path, remove.path);
    }

    @Override
    @Generated("IntelliJ IDEA")
    public int hashCode() {
        return Objects.hash(path);
    }
}
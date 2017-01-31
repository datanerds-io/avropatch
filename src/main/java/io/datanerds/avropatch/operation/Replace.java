package io.datanerds.avropatch.operation;

import io.datanerds.avropatch.value.DefaultSchema;
import org.apache.avro.reflect.AvroSchema;

import javax.annotation.Generated;
import java.util.Objects;

/**
 * This class represents the "replace" operation of RFC 6902 'JavaScript Object Notation (JSON) Patch'.
 *
 * @see <a href="https://tools.ietf.org/html/rfc6902#section-4.3">https://tools.ietf.org/html/rfc6902#section-4.3</a>
 */
public final class Replace<T> implements Operation {
    public final Path path;
    @AvroSchema(DefaultSchema.VALUE)
    public final T value;

    @SuppressWarnings("unused") // no-arg constructor needed by Avro
    private Replace() {
        this(null, null);
    }

    /**
     *
     * @param path Path pointing out where the JSON value should be replaced
     * @param value Actual value to <i>replace</i> in patched object
     *
     * @see DefaultSchema
     */
    public Replace(Path path, T value) {
        this.path = path;
        this.value = value;
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
        Replace<T> replace = (Replace<T>) o;
        return Objects.equals(path, replace.path) &&
                Objects.equals(value, replace.value);
    }

    @Override
    @Generated("IntelliJ IDEA")
    public int hashCode() {
        return Objects.hash(path, value);
    }
}

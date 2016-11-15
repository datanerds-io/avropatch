package io.datanerds.avropatch.operation;

import org.apache.avro.reflect.AvroIgnore;
import org.apache.avro.reflect.AvroSchema;

/**
 * This class represents the "test" operation of RFC 6902 'JavaScript Object Notation (JSON) Patch'.
 *
 * @see <a href="https://tools.ietf.org/html/rfc6902#section-4.6">https://tools.ietf.org/html/rfc6902#section-4.6</a>
 */
public final class Test<T> implements Operation {
    @AvroIgnore
    public static final String op = "test";
    public final Path path;
    @AvroSchema(DefaultSchema.VALUE)
    public final T value;

    private Test() {
        this.path = null;
        this.value = null;
    }

    /**
     *
     * @param path Path pointing out which JSON value should be tested against
     * @param value Actual value to <i>test</i> against
     *
     * @see DefaultSchema
     */
    public Test(Path path, T value) {
        this.path = path;
        this.value = value;
    }
}

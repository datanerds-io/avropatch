package io.datanerds.avropatch.operation;

import io.datanerds.avropatch.exception.InvalidPathException;
import org.apache.avro.reflect.AvroSchema;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Path {

    public static final String SLASH = "/";
    public static final Path ROOT = new Path();

    private static final Pattern VALID_PATTERN = Pattern.compile("[0-9a-zA-Z][0-9a-zA-Z_-]*");

    @AvroSchema("{\"type\": \"array\", \"items\": \"string\"}")
    private final List<String> parts;

    private Path() {
        this(Collections.emptyList());
    }

    private Path(final List<String> parts) {
        this.parts = Collections.unmodifiableList(parts);
    }

    public static Path of(final String... parts) {
        List<String> paths = Arrays.asList(parts);
        for (String path : paths) {
            if (!VALID_PATTERN.matcher(path).matches()) {
                throw new InvalidPathException(String.format("%s is not a valid JSON path", path));
            }
        }
        return new Path(paths);
    }

    public static Path parse(final String path) {
        verifyParsable(path);
        if (SLASH.equals(path)) {
            return ROOT;
        }
        return of(path.substring(1).split(SLASH));
    }

    private static void verifyParsable(String path) {
        Objects.nonNull(path);
        if (!path.startsWith(SLASH)) {
            throw new InvalidPathException("JSON Path has to start with a slash.");
        }
    }

    public Path parent() {
        if (parts.size() == 0) {
            return ROOT;
        }
        return new Path(parts.subList(0, parts.size() - 1));
    }

    public List<String> parts() {
        return parts;
    }

    @Override
    public String toString() {
        if (parts.isEmpty()) {
            return SLASH;
        }

        StringBuilder sb = new StringBuilder();
        parts.forEach(part -> {
            sb.append(SLASH);
            sb.append(part);
        });
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Path path = (Path) o;
        return Objects.equals(parts, path.parts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parts);
    }
}

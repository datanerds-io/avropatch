package io.datanerds.avropatch.operation;

import io.datanerds.avropatch.exception.InvalidReferenceTokenException;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class represents a JavaScript Object Notation (JSON) Pointer object as defined in RFC 6901. {@link Path}
 * instances identify a specific value within a JSON document by maintaining a sequence of zero or more reference
 * tokens, each prefixed by a {@code '/'} character.
 * <p>
 * Assume the following JSON document:
 * <blockquote><pre>
 * {
 *    "employees" : [
 *       {
 *          "firstName" : "John",
 *          "lastName"  : "Doe",
 *          "age"       : 42,
 *          "address"   : {
 *             "street" : "1234 Main Street",
 *             "city"   : "San Francisco",
 *             "zip"    : "94114"
 *          }
 *       },
 *       {
 *          "firstName" : "Jane",
 *          "lastName"  : "Doe",
 *          "age"       : 42,
 *          "address"   : {
 *             "street" : "4321 Main Street",
 *             "city"   : "New York",
 *             "zip"    : "10001"
 *          }
 *       }
 *    ]
 * }
 * </pre></blockquote>
 * <p>
 * The JSON {@link Path} for addressing value "John" would be {@code "/employees/0/firstName"}, for the Jane's entire
 * record {@code "/employees/1"} and to specify the street within the JSON blob representing her address
 * {@code "/employees/1/address"}.
 * @see <a href="https://tools.ietf.org/html/rfc6901">RFC 6901: JavaScript Object Notation (JSON) Pointer</a>
 */
public class Path {
    public static final String SLASH = "/";
    public static final Path ROOT = new Path();
    private static final Pattern VALID_PATTERN = Pattern.compile("[0-9a-zA-Z][0-9a-zA-Z_-]*");
    private final List<String> parts;

    private Path() {
        this(Collections.emptyList());
    }

    private Path(List<String> parts) {
        this.parts = Collections.unmodifiableList(parts);
    }

    /**
     * Convenient method for retrieving root path {@code "/"}.
     * @return {@link #ROOT}
     */
    public static Path of() {
        return ROOT;
    }

    /**
     * Constructs a {@link Path} object for given reference tokens.
     * @param referenceTokens {@link String} representation of all reference tokens for {@link Path} without prefix
     * {@code "/"}
     * @return JSON path assembled via given reference tokens
     */
    public static Path of(String... referenceTokens) {
        return new Path(validatedReferenceTokens(referenceTokens).collect(Collectors.toList()));
    }

    /**
     * Constructs a {@link Path} object by concatenating given paths.
     * @param subpaths paths to be concatenated for new {@link Path} object
     * @return JSON path assembled by concatenating given paths
     */
    public static Path of(Path... subpaths) {
        List<String> paths = Stream.of(subpaths)
                .flatMap(Path.partsStream())
                .collect(Collectors.toList());
        return new Path(paths);
    }

    /**
     * Parses a {@link String} representation of a JSON path (e.g. {@code "/employees/0/firstName"},
     * {@code "/"} or {@code "/employees/1/address"}).
     * @param path valid {@link String} representation of a JSON path
     * @return JSON path instance
     */
    public static Path parse(String path) {
        verifyParsable(path);
        if (SLASH.equals(path)) {
            return ROOT;
        }
        return of(path.substring(1).split(SLASH));
    }

    /**
     * Constructs a new {@link Path} object by appending the given subpaths to the JSON path represented by this object.
     * @param subpaths {@link Path} objects to be appended
     * @return concatenated {@link Path}
     */
    public Path append(Path... subpaths) {
        List<String> paths = Stream.concat(this.parts().stream(), Stream.of(subpaths).flatMap(partsStream()))
                .collect(Collectors.toList());
        return new Path(paths);
    }

    /**
     * Constructs a new {@link Path} object by appending the JSON path specified by the given reference tokens to the
     * path represented by this instance.
     * @param referenceTokens {@link String} representation of all reference tokens for {@link Path} without prefix
     * {@code "/"}
     * @return concatenated {@link Path}
     */
    public Path append(String... referenceTokens) {
        List<String> paths = Stream.concat(this.parts().stream(), validatedReferenceTokens(referenceTokens))
                .collect(Collectors.toList());
        return new Path(paths);
    }

    public Path parent() {
        if (parts.size() == 0) {
            return ROOT;
        }
        return new Path(parts.subList(0, parts.size() - 1));
    }

    public Path leaf() {
        if (parts.size() == 0) {
            return ROOT;
        }
        return new Path(parts.subList(parts.size() - 1, parts.size()));
    }

    public List<String> parts() {
        return parts;
    }

    public List<Path> subPaths() {
        if (parts.isEmpty()) {
            return Collections.emptyList();
        }

        List<Path> subPaths = new ArrayList<>();
        for (int i = 1; i <= parts.size(); i++) {
            subPaths.add(new Path(parts.subList(0, i)));
        }

        return subPaths;
    }

    private static Function<Path, Stream<String>> partsStream() {
        return path -> path.parts().stream();
    }

    private static Stream<String> validatedReferenceTokens(String[] referenceTokens) {
        return Stream.of(referenceTokens).map(Path::validateReferenceToken);
    }

    private static String validateReferenceToken(String path) {
        if (!VALID_PATTERN.matcher(path).matches()) {
            throw new InvalidReferenceTokenException(String.format("%s is not a valid JSON path", path));
        }
        return path;
    }

    private static void verifyParsable(String path) {
        Objects.nonNull(path);
        if (!path.startsWith(SLASH)) {
            throw new InvalidReferenceTokenException("JSON Path has to start with a slash.");
        }
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
    @Generated("IntelliJ IDEA")
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
    @Generated("IntelliJ IDEA")
    public int hashCode() {
        return Objects.hash(parts);
    }
}

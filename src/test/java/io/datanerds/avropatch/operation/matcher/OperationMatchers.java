package io.datanerds.avropatch.operation.matcher;

import com.google.common.base.Joiner;
import io.datanerds.avropatch.operation.*;
import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsCollectionContaining;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.hamcrest.core.AllOf.allOf;

public final class OperationMatchers {

    private static Map<Class<? extends Operation>, Function<Operation, Matcher<? extends Operation>>> matchers =
            new HashMap<>();

    static {
        matchers.put(Add.class, expected -> Matchers.equalTo((Add) expected));
        matchers.put(Copy.class, expected -> Matchers.equalTo((Copy) expected));
        matchers.put(Move.class, expected -> Matchers.equalTo((Move) expected));
        matchers.put(Remove.class, expected -> Matchers.equalTo((Remove) expected));
        matchers.put(Replace.class, expected -> Matchers.equalTo((Replace) expected));
        matchers.put(Test.class, expected -> Matchers.equalTo((Test) expected));
    }

    private OperationMatchers() {
    }

    public static <T extends Operation> Matcher<T> equalTo(T expected) {
        Objects.nonNull(expected);
        return (Matcher<T>) Optional.ofNullable(matchers.get(expected.getClass()))
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Matcher for operation of type %s does not exist", expected.getClass())))
                .apply(expected);
    }

    public static <T extends Operation> Matcher<Iterable<T>> hasItem(T item) {
        return new IsCollectionContaining(equalTo(item));
    }

    public static <T extends Operation> Matcher<Iterable<T>> hasItems(T... items) {
        return hasItems(Arrays.asList(items));
    }

    public static <T extends Operation> Matcher<Iterable<T>> hasItems(List<T> items) {
        return allOf(items.stream()
                .map(item -> hasItem(item))
                .collect(Collectors.toList()));
    }

    public static <T extends Operation> Matcher<List<T>> hasItemsOrdered(List<T> expected) {
        return new CustomTypeSafeMatcher<List<T>>(Joiner.on(",").join(expected)) {
            @Override
            protected boolean matchesSafely(List<T> value) {
                if (expected.size() != value.size()) {
                    return false;
                }
                for (int i = 0; i < expected.size(); i++) {
                    if (!equalTo(expected.get(i)).matches(value.get(i))) {
                        return false;
                    }
                }
                return true;
            }

        };
    }

    private static class Matchers {

        public static Matcher<Add> equalTo(Add expected) {
            return new CustomTypeSafeMatcher<Add>(expected.toString()) {

                @Override
                protected boolean matchesSafely(Add item) {
                    return item.path.equals(expected.path)
                            && compareNullable(expected.value, item.value);
                }
            };
        }

        public static Matcher<Copy> equalTo(Copy expected) {
            return new CustomTypeSafeMatcher<Copy>(expected.toString()) {

                @Override
                protected boolean matchesSafely(Copy item) {
                    return item.from.equals(expected.from)
                            && item.path.equals(expected.path);
                }
            };
        }

        public static Matcher<Move> equalTo(Move expected) {
            return new CustomTypeSafeMatcher<Move>(expected.toString()) {

                @Override
                protected boolean matchesSafely(Move item) {
                    return item.from.equals(expected.from)
                            && item.path.equals(expected.path);
                }
            };
        }

        public static Matcher<Remove> equalTo(Remove expected) {
            return new CustomTypeSafeMatcher<Remove>(expected.toString()) {

                @Override
                protected boolean matchesSafely(Remove item) {
                    return item.path.equals(expected.path);
                }
            };
        }

        public static Matcher<Replace> equalTo(Replace expected) {
            return new CustomTypeSafeMatcher<Replace>(expected.toString()) {

                @Override
                protected boolean matchesSafely(Replace item) {
                    return item.path.equals(expected.path)
                            && compareNullable(expected.value, item.value);
                }
            };
        }

        public static Matcher<Test> equalTo(Test expected) {
            return new CustomTypeSafeMatcher<Test>(expected.toString()) {

                @Override
                protected boolean matchesSafely(Test item) {
                    return item.path.equals(expected.path)
                            && compareNullable(expected.value, item.value);
                }
            };
        }

        private static <T, V> boolean compareNullable(T expected, V item) {
            if (item == null) {
                return expected == null;
            }
            return item.equals(expected);
        }
    }

}

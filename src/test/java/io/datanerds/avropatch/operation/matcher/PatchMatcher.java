package io.datanerds.avropatch.operation.matcher;

import com.google.common.base.Joiner;
import io.datanerds.avropatch.Patch;
import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Matcher;

import static io.datanerds.avropatch.operation.matcher.OperationMatchers.hasItemsOrdered;
import static org.hamcrest.core.AllOf.allOf;

public class PatchMatcher {

    public static Matcher<Patch> equalTo(Patch items) {
        return allOf(operationsEqualTo(items), headersEqualTo(items));
    }

    private static Matcher<Patch> operationsEqualTo(Patch expected) {
        return new CustomTypeSafeMatcher<Patch>(Joiner.on(",").join(expected.getOperations())) {
            @Override
            protected boolean matchesSafely(Patch item) {
                return hasItemsOrdered(item.getOperations()).matches(expected.getOperations());
            }
        };
    }

    private static Matcher<Patch> headersEqualTo(Patch expected) {
        return new CustomTypeSafeMatcher<Patch>(Joiner.on(",").join(expected.getHeaders().keySet())) {
            @Override
            protected boolean matchesSafely(Patch item) {
                if (expected.getHeaders().size() != item.getHeaders().size()) {
                    return false;
                }

                for (String key : item.getHeaders().keySet()) {
                    if (!item.getHeaders().get(key).equals(expected.getHeaders().get(key))) {
                        return false;
                    }
                }

                return true;
            }
        };
    }
}

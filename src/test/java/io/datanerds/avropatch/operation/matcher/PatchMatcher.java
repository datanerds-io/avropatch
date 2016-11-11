package io.datanerds.avropatch.operation.matcher;

import io.datanerds.avropatch.Patch;
import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Matcher;

import static io.datanerds.avropatch.operation.matcher.OperationMatchers.hasItems;

public class PatchMatcher {

    public static Matcher<Patch> equalTo(Patch expected) {
        return new CustomTypeSafeMatcher<Patch>(expected.toString()) {
            @Override
            protected boolean matchesSafely(Patch item) {
                return hasItems(item.getOperations()).matches(expected.getOperations());
            }
        };
    }
}

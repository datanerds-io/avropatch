package io.datanerds.avropatch.operation.matcher;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.datanerds.avropatch.Patch;
import io.datanerds.avropatch.operation.Copy;
import io.datanerds.avropatch.operation.Move;
import io.datanerds.avropatch.operation.Operation;
import io.datanerds.avropatch.operation.Path;
import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Matcher;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

import static io.datanerds.avropatch.operation.matcher.OperationMatchers.hasItemsOrdered;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
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

    @Test
    public void headers() throws IOException {
        UUID uuid = UUID.randomUUID();
        Patch patch = new Patch(Collections.EMPTY_LIST, ImmutableMap.of("header 1", uuid));
        Patch patch2 = new Patch(Collections.EMPTY_LIST, ImmutableMap.of("header 1", uuid));
        Patch patch3 = new Patch(Collections.EMPTY_LIST, ImmutableMap.of("header 2", uuid));
        Patch patch4 = new Patch(Collections.EMPTY_LIST, ImmutableMap.of("header 1", UUID.randomUUID()));

        assertThat(patch, is(equalTo(patch2)));
        assertThat(patch2, is(equalTo(patch)));
        assertThat(patch, is(not(equalTo(patch3))));
        assertThat(patch, is(not(equalTo(patch4))));
    }

    @Test
    public void operations() throws IOException {
        Operation operation1 = new Copy(Path.of("from", "here"), Path.of("to", "there"));
        Operation operation2 = new Move(Path.of("from", "here"), Path.of("to", "there"));
        Patch patch1 = new Patch(ImmutableList.of(operation1, operation2), Collections.EMPTY_MAP);
        Patch patch2 = new Patch(ImmutableList.of(operation1, operation2), Collections.EMPTY_MAP);
        Patch patch3 = new Patch(ImmutableList.of(operation1), Collections.EMPTY_MAP);
        Patch patch4 = new Patch(ImmutableList.of(operation2, operation1), Collections.EMPTY_MAP);

        assertThat(patch1, is(equalTo(patch2)));
        assertThat(patch2, is(equalTo(patch1)));
        assertThat(patch1, is(not(equalTo(patch3))));
        assertThat(patch1, is(not(equalTo(patch4))));
    }
}

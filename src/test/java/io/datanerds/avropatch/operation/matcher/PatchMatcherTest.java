package io.datanerds.avropatch.operation.matcher;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.datanerds.avropatch.Patch;
import io.datanerds.avropatch.operation.Copy;
import io.datanerds.avropatch.operation.Move;
import io.datanerds.avropatch.operation.Operation;
import io.datanerds.avropatch.operation.Path;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

import static io.datanerds.avropatch.operation.matcher.PatchMatcher.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Enclosed.class)
public class PatchMatcherTest {

    public static class Headers {
        private final UUID uuid = UUID.randomUUID();
        private final Patch patchWithHeader = new Patch(Collections.EMPTY_LIST, ImmutableMap.of("header", uuid));
        private final Patch patchWithHeaderSibling = new Patch(Collections.EMPTY_LIST, ImmutableMap.of("header", uuid));
        private final Patch differentHeader = new Patch(Collections.EMPTY_LIST, ImmutableMap.of("header 2", uuid));
        private final Patch differentHeaderValue = new Patch(Collections.EMPTY_LIST,
                ImmutableMap.of("header", UUID.randomUUID()));

        @Test
        public void equality() throws IOException {
            assertThat(patchWithHeader, is(equalTo(patchWithHeaderSibling)));
            assertThat(patchWithHeaderSibling, is(equalTo(patchWithHeader)));
        }

        @Test
        public void inequality() throws IOException {
            assertThat(patchWithHeader, is(not(equalTo(differentHeader))));
            assertThat(patchWithHeader, is(not(equalTo(differentHeaderValue))));
        }
    }

    public static class Operations {
        private final Operation copy = new Copy(Path.of("from", "here"), Path.of("to", "there"));
        private final Operation move = new Move(Path.of("from", "here"), Path.of("to", "there"));
        private final Patch copyMovePatch = new Patch(ImmutableList.of(copy, move), Collections.EMPTY_MAP);
        private final Patch copyMovePatchSibling = new Patch(ImmutableList.of(copy, move), Collections.EMPTY_MAP);
        private final Patch withoutMoveOperation = new Patch(ImmutableList.of(copy), Collections.EMPTY_MAP);
        private final Patch reverseOperationOrder = new Patch(ImmutableList.of(move, copy), Collections.EMPTY_MAP);

        @Test
        public void equality() throws IOException {
            assertThat(copyMovePatch, is(equalTo(copyMovePatchSibling)));
            assertThat(copyMovePatchSibling, is(equalTo(copyMovePatch)));
        }

        @Test
        public void inequality() throws IOException {
            assertThat(copyMovePatch, is(not(equalTo(withoutMoveOperation))));
            assertThat(copyMovePatch, is(not(equalTo(reverseOperationOrder))));
        }
    }
}

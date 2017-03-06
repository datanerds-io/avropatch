package io.datanerds.avropatch;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.testing.EqualsTester;
import io.datanerds.avropatch.operation.Operation;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.datanerds.avropatch.operation.OperationGenerator.randomOperation;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class PatchTest {

    private final UUID uuid = UUID.randomUUID();
    private final Map<String, ? super Object> headers = ImmutableMap.of("test 1", "foo", "test 2", uuid);
    private final List<Operation> operations = ImmutableList.of(randomOperation(), randomOperation(), randomOperation());
    private final UUID resource = UUID.randomUUID();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void equality() {
        new EqualsTester()
                .addEqualityGroup(Patch.from(resource, operations, headers), Patch.from(resource, operations, headers))
                .addEqualityGroup(Patch.from(resource, operations))
                .addEqualityGroup(Patch.from(resource, ImmutableList.of(randomOperation()),
                        ImmutableMap.of("test 1", "bar", "test 2", uuid)))
                .addEqualityGroup(Patch.from(resource, ImmutableList.of(randomOperation())))
                .addEqualityGroup(Patch.from(resource, ImmutableList.of(randomOperation())))
                .testEquals();
    }

    @Test
    public void getHeader() {
        Patch<?> patch = Patch.from(resource, operations, headers);
        String stringHeader = patch.getHeader("test 1");
        assertThat(stringHeader, is(equalTo("foo")));
        UUID uuidHeader = patch.getHeader("test 2");
        assertThat(uuidHeader, is(equalTo(uuid)));
    }

    @Test
    public void isEmpty() {
        assertTrue(Patch.from(resource).isEmpty());
        assertTrue(Patch.from(resource, headers).isEmpty());
        assertFalse(Patch.from(resource, operations, headers).isEmpty());
    }

    @Test
    public void size() {
        assertThat(Patch.from(resource).size(), is(equalTo(0)));
        assertThat(Patch.from(resource, operations, headers).size(), is(equalTo(operations.size())));
    }

    @Test
    public void stream() {
        assertThat(operations, is(equalTo(Patch.from(resource, operations).stream().collect(Collectors.toList()))));
    }

    @Test
    public void getter() {
        Date timestamp = new Date();
        Patch<UUID> patch = Patch.from(resource, operations, headers, timestamp);
        assertThat(patch.getResource(), is(equalTo(resource)));
        assertThat(patch.getTimestamp(), is(equalTo(timestamp)));
        for (int i = 0; i < operations.size(); i++) {
            assertThat(patch.getOperation(i), is(equalTo(operations.get(i))));
        }
    }

    @Test
    public void getNotExistentHeader() {
        String value = Patch.from(resource, operations, headers).getHeader("test 7");
        assertNull(value);
    }

    @Test
    public void getHeaderWithIncompatibleType() {
        Patch<UUID> patch =  Patch.from(resource, operations, headers);
        assertTrue(patch.hasHeader("test 1"));

        exception.expect(ClassCastException.class);
        @SuppressWarnings("unused")
        UUID value = patch.getHeader("test 1");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void exceptionWhenRemovingImmutableHeaders() {
        Patch.from(resource, operations, headers).getHeaders().remove("test 1");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void exceptionWhenEditingImmutableHeaders() {
        Patch.from(resource, operations, headers).getHeaders().put("foo", 22);
    }
}

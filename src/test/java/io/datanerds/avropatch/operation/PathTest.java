package io.datanerds.avropatch.operation;

import com.google.common.testing.EqualsTester;
import com.google.common.testing.NullPointerTester;
import io.datanerds.avropatch.exception.InvalidPathException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static io.datanerds.avropatch.operation.Path.ROOT;
import static io.datanerds.avropatch.operation.Path.SLASH;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertThat;

public class PathTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void equality() {
        new EqualsTester()
                .addEqualityGroup(Path.of("hello"), Path.of("hello"), Path.parse("/hello"), Path.parse("/hello/"))
                .addEqualityGroup(Path.of("hello", "world"), Path.of("hello", "world"), Path.parse("/hello/world"))
                .addEqualityGroup(Path.of(), Path.parse(SLASH), ROOT)
                .testEquals();
    }

    @Test
    public void npe() {
        new NullPointerTester().testAllPublicStaticMethods(Path.class);
    }

    @Test
    public void numbersAndUnderscores() {
        Path path = Path.of("hello_", "123world-456");
        assertThat(path.toString(), is(equalTo("/hello_/123world-456")));
    }

    @Test
    public void noLeadingUnderscores() {
        expectedException.expect(InvalidPathException.class);
        Path.of("_hello_");
    }

    @Test
    public void noLeadingDash() {
        expectedException.expect(InvalidPathException.class);
        Path.of("-hello");
    }

    @Test
    public void invalidCharacter() {
        expectedException.expect(InvalidPathException.class);
        Path.of("hel!lo");
    }

    @Test
    public void emptyPath() {
        expectedException.expect(InvalidPathException.class);
        Path.of("");
    }

    @Test
    public void parse() {
        assertThat(Path.parse("/hello").toString(), is(equalTo("/hello")));
        assertThat(Path.parse("/hello/").toString(), is(equalTo("/hello")));
        assertThat(Path.parse("/hello/world/1234"), is(equalTo(Path.of("hello", "world", "1234"))));
    }

    @Test
    public void trailingSlash() {
        assertThat(Path.parse("/hello/"), is(equalTo(Path.of("hello"))));
    }

    @Test
    public void parseWithoutSlash() {
        expectedException.expect(InvalidPathException.class);
        expectedException.expectMessage("JSON Path has to start with a slash.");
        Path.parse("hello/");
    }

    @Test
    public void parent() {
        Path path = Path.of("hello", "world");
        assertThat(path.parent(), is(equalTo(Path.of("hello"))));
        assertThat(ROOT.parent(), is(equalTo(ROOT)));
    }

    @Test
    public void parts() {
        Path path = Path.of("hello", "world");
        assertThat(path.parts(), hasItems("hello", "world"));
        assertThat(path.parts().size(), is(equalTo(2)));
    }

    @Test
    public void partsIsUnmodifiable() {
        Path path = Path.of("hello", "world");
        expectedException.expect(UnsupportedOperationException.class);
        path.parts().add("hello");
    }

    @Test
    public void asString() {
        Path path = Path.of("hello", "world");
        assertThat(path.toString(), is(equalTo("/hello/world")));
        assertThat(ROOT.toString(), is(equalTo("/")));
    }

}
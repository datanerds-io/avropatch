package io.datanerds.avropatch.operation;

import com.google.common.testing.EqualsTester;
import com.google.common.testing.NullPointerTester;
import io.datanerds.avropatch.exception.InvalidReferenceTokenException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static io.datanerds.avropatch.operation.Path.ROOT;
import static io.datanerds.avropatch.operation.Path.SLASH;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.emptyIterableOf;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertThat;

public class PathTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void equality() {
        new EqualsTester()
                .addEqualityGroup(Path.of("hello"), Path.of("hello"), Path.parse("/hello"), Path.parse("/hello/"))
                .addEqualityGroup(Path.of("hello", "world"), Path.of("hello", "world"), Path.parse("/hello/world"),
                        Path.of("hello").append("world"), Path.of("hello").append(Path.of("world")),
                        Path.of(Path.of("hello"), Path.of("world")))
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
        expectedException.expect(InvalidReferenceTokenException.class);
        Path.of("_hello_");
    }

    @Test
    public void noLeadingDash() {
        expectedException.expect(InvalidReferenceTokenException.class);
        Path.of("-hello");
    }

    @Test
    public void invalidCharacter() {
        expectedException.expect(InvalidReferenceTokenException.class);
        Path.of("hel!lo");
    }

    @Test
    public void emptyPath() {
        expectedException.expect(InvalidReferenceTokenException.class);
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
        expectedException.expect(InvalidReferenceTokenException.class);
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
    public void leaf() {
        Path path = Path.of("hello", "world");
        assertThat(path.leaf(), is(equalTo(Path.of("world"))));
        assertThat(ROOT.leaf(), is(equalTo(ROOT)));
    }

    @Test
    public void parts() {
        Path path = Path.of("hello", "world");
        assertThat(path.parts(), allOf(
                iterableWithSize(2),
                hasItems("hello", "world")));
    }

    @Test
    public void subPaths() {
        Path path = Path.of("hello", "world");
        assertThat(path.subPaths(), allOf(
                iterableWithSize(2),
                hasItems(Path.of("hello"), Path.parse("/hello/world"))));
    }

    @Test
    public void subPathsOfRoot() {
        assertThat(ROOT.subPaths(), emptyIterableOf(Path.class));
    }

    @Test
    public void partsIsUnmodifiable() {
        Path path = Path.of("hello", "world");
        expectedException.expect(UnsupportedOperationException.class);
        path.parts().add("hello");
    }

    @Test
    public void pathAsString() {
        assertThat(Path.of("hello", "world").toString(), is(equalTo("/hello/world")));
    }

    @Test
    public void rootAsString() {
        assertThat(ROOT.toString(), is(equalTo("/")));
    }

    @Test(expected = InvalidReferenceTokenException.class)
    public void appendValidates() {
        ROOT.append(".'!#(^((*(_$,-");
    }
}
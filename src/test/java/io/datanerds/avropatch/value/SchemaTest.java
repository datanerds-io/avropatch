package io.datanerds.avropatch.value;

import avro.shaded.com.google.common.collect.ImmutableList;
import io.datanerds.avropatch.value.conversion.BigIntegerConversion;
import io.datanerds.avropatch.value.conversion.DateConversion;
import io.datanerds.avropatch.value.conversion.UUIDConversion;
import org.apache.avro.Conversion;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.reflect.ReflectDatumReader;
import org.apache.avro.reflect.ReflectDatumWriter;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

import static io.datanerds.avropatch.value.ValueSchema.SCHEMA;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SchemaTest {

    @Test
    public void serializesNull() throws IOException {
        SerializationTester.reserializeAndAssert(null);
    }

    @Test
    public void serializesBigInteger() throws IOException {
        SerializationTester.reserializeAndAssert(new BigInteger("364039216459034658923475606490812364908234653"));
        SerializationTester.reserializeAndAssert(
                new BigInteger[]{new BigInteger("364039216459034658923475606490812364908234653"),
                        new BigInteger("56729476543655265"), new BigInteger("987591856718456723489")});
    }

    static final class SerializationTester {

        public static final List<Conversion<?>> CONVERTERS =
                ImmutableList.of(new BigIntegerConversion(), new DateConversion(), new UUIDConversion());

        private SerializationTester() {
        }

        public static <T> void reserializeAndAssert(T value) throws IOException {
            byte[] bytes = toBytes(value);
            T object = toObject(bytes);
            assertEquality(object, value);
        }

        private static <T> void assertEquality(T value, T object) {
            if (value instanceof Collection) {
                // Avro serializes collections as arrays
                Collection collection = (Collection) value;
                assertThat(collection.toArray(), is(equalTo(object)));
            } else {
                assertThat(object, is(equalTo(value)));
            }
        }

        private static <T> byte[] toBytes(T value) throws IOException {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Encoder binaryEncoder = EncoderFactory.get().directBinaryEncoder(outputStream, null);

            GenericDatumWriter<T> writer = new ReflectDatumWriter<>(SCHEMA);
            CONVERTERS.forEach(writer.getData()::addLogicalTypeConversion);
            writer.write(value, binaryEncoder);
            return outputStream.toByteArray();
        }

        private static <T> T toObject(byte[] bytes) throws IOException {
            GenericDatumReader<T> reader = new ReflectDatumReader<>(SCHEMA);
            CONVERTERS.forEach(reader.getData()::addLogicalTypeConversion);
            return reader.read(null, DecoderFactory.get().binaryDecoder(bytes, null));
        }
    }
}

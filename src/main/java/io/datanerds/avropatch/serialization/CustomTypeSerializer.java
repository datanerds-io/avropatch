package io.datanerds.avropatch.serialization;

import io.datanerds.avropatch.Patch;
import io.datanerds.avropatch.schema.CustomTypes;
import io.datanerds.avropatch.value.conversion.AvroData;
import org.apache.avro.Schema;
import org.apache.avro.io.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class CustomTypeSerializer {

    private final DatumWriter<Patch> writer;
    private final DatumReader<Patch> reader;

    private CustomTypeSerializer(Schema schema) {
        Objects.nonNull(schema);
        writer = AvroData.get().createDatumWriter(schema);
        reader = AvroData.get().createDatumReader(schema);
    }

    public byte[] toBytes(Patch value) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Encoder binaryEncoder = EncoderFactory.get().directBinaryEncoder(outputStream, null);
        writer.write(value, binaryEncoder);
        return outputStream.toByteArray();
    }

    public Patch toObject(byte[] bytes) throws IOException {
        return reader.read(null, DecoderFactory.get().binaryDecoder(bytes, null));
    }

    public static class Builder {
        private final List<Schema> valueTypes = new ArrayList<>();

        public Builder withBigDecimal() {
            valueTypes.add(CustomTypes.BigDecimalType.SCHEMA);
            return this;
        }

        public Builder with(Type type) {
            valueTypes.add(AvroData.get().getSchema(type));
            return this;
        }

        public Builder with(Schema schema) {
            valueTypes.add(schema);
            return this;
        }

        public CustomTypeSerializer build() {
            Schema schema = CustomTypes.PatchType.create(Schema.createUnion(valueTypes));
            return new CustomTypeSerializer(schema);
        }
    }
}

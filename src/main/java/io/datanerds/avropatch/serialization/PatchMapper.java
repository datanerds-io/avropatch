package io.datanerds.avropatch.serialization;

import io.datanerds.avropatch.Patch;
import io.datanerds.avropatch.value.AvroData;
import io.datanerds.avropatch.value.type.BigDecimalType;
import io.datanerds.avropatch.value.type.BigIntegerType;
import io.datanerds.avropatch.value.type.TimestampType;
import io.datanerds.avropatch.value.type.UuidType;
import org.apache.avro.Schema;
import org.apache.avro.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.concurrent.ThreadSafe;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.datanerds.avropatch.value.type.PrimitiveType.*;

@ThreadSafe
public final class PatchMapper {

    private static Logger logger = LoggerFactory.getLogger(PatchMapper.class);

    private final DatumWriter<Patch> writer;
    private final DatumReader<Patch> reader;

    public PatchMapper() {
        this(AvroData.get().getSchema(Patch.class));
    }

    private PatchMapper(Schema schema) {
        this.writer = AvroData.get().createDatumWriter(schema);
        this.reader = AvroData.get().createDatumReader(schema);
    }

    public byte[] toBytes(Patch value) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Encoder binaryEncoder = EncoderFactory.get().directBinaryEncoder(outputStream, null);
        writer.write(value, binaryEncoder);
        return outputStream.toByteArray();
    }

    public Patch toPatch(byte[] bytes) throws IOException {
        return reader.read(null, DecoderFactory.get().binaryDecoder(bytes, null));
    }

    public static class Builder {
        protected final List<Schema> types;

        public Builder() {
            this.types = new ArrayList<>();
        }

        public Builder nullable() {
            types.add(NULL.getSchema());
            return this;
        }

        public Builder withPrimitives() {
            types.addAll(Arrays.asList(BOOLEAN.getSchema(), DOUBLE.getSchema(), FLOAT.getSchema(), INTEGER.getSchema(),
                    LONG.getSchema(), STRING.getSchema()));
            return this;
        }

        public Builder withCustomTypes() {
            types.addAll(Arrays.asList(BigDecimalType.SCHEMA, BigIntegerType.SCHEMA, TimestampType.SCHEMA, UuidType.SCHEMA));
            return this;
        }

        public Builder withType(Type type) {
            types.add(AvroData.get().getSchema(type));
            return this;
        }

        public Builder with(Schema schema) {
            types.add(schema);
            return this;
        }

        public PatchMapper build() {
            Schema schema = makeSchema();
            logger.debug("Created serializer for schema {}", schema);
            return new PatchMapper(schema);
        }

        private Schema makeSchema() {
            if (types.size() == 1) {
                return PatchType.create(types.get(0));
            } else {
                return PatchType.create(Schema.createUnion(types));
            }
        }

        private Builder endSubBuilder(Schema schema) {
            types.add(schema);
            return this;
        }

        public Builder.ArrayBuilder withArray() {
            return new Builder.ArrayBuilder(this);
        }

        public class ArrayBuilder extends Builder {
            private final Builder parentBuilder;

            ArrayBuilder(Builder parentBuilder) {
                this.parentBuilder = parentBuilder;
            }

            public Builder endArray() {
                return parentBuilder.endSubBuilder(Schema.createArray(Schema.createUnion(types)));
            }

            @Override
            public Builder.ArrayBuilder nullable() {
                super.nullable();
                return this;
            }

            @Override
            public Builder.ArrayBuilder withPrimitives() {
                super.withPrimitives();
                return this;
            }

            @Override
            public Builder.ArrayBuilder withCustomTypes() {
                super.withCustomTypes();
                return this;
            }

            @Override
            public Builder.ArrayBuilder withType(Type type) {
                super.withType(type);
                return this;
            }

            @Override
            public Builder.ArrayBuilder with(Schema schema) {
                super.with(schema);
                return this;
            }
        }
    }
}
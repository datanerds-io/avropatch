package io.datanerds.avropatch.value;

import io.datanerds.avropatch.value.conversion.BigDecimalConversion;
import io.datanerds.avropatch.value.conversion.BigIntegerConversion;
import io.datanerds.avropatch.value.conversion.DateConversion;
import io.datanerds.avropatch.value.conversion.UUIDConversion;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Type;

import static org.apache.avro.Schema.*;

public class ValueSchema {

    public static final Schema BOOLEAN = create(Type.BOOLEAN);
    public static final Schema DOUBLE = create(Type.DOUBLE);
    public static final Schema FLOAT = create(Type.FLOAT);
    public static final Schema INTEGER = create(Type.INT);
    public static final Schema LONG = create(Type.LONG);
    public static final Schema NULL = create(Type.NULL);
    public static final Schema STRING = create(Type.STRING);

    public static final Schema BIG_DECIMAL = BigDecimalConversion.SCHEMA;
    public static final Schema BIG_INTEGER = BigIntegerConversion.SCHEMA;
    public static final Schema DATE = DateConversion.SCHEMA;
    public static final Schema UUID = UUIDConversion.SCHEMA;

    public static final Schema SCHEMA =
            createUnion(BIG_DECIMAL, BIG_INTEGER, BOOLEAN, DATE, DOUBLE, FLOAT, INTEGER, /*LONG,*/ NULL, STRING, UUID,
                    createArray(createUnion(BIG_DECIMAL, BIG_INTEGER, BOOLEAN, DATE, DOUBLE, FLOAT, INTEGER, /*LONG,*/ NULL, STRING, UUID)),
                    createMap(createUnion(BIG_DECIMAL, BIG_INTEGER, BOOLEAN, DATE, DOUBLE, FLOAT, INTEGER, /*LONG,*/ NULL, STRING, UUID)));
}

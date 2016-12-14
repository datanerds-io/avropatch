package io.datanerds.avropatch.serialization;

import io.datanerds.avropatch.operation.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

enum OperationGenerator {

    @SuppressWarnings("unchecked")
    ADD(() -> new Add(generatePath(), generateValue())),
    COPY(() -> new Copy(generatePath(), generatePath())),
    MOVE(() -> new Move(generatePath(), generatePath())),
    REMOVE(() -> new Remove(generatePath())),
    REPLACE(() -> new Replace<>(generatePath(), generateValue())),
    @SuppressWarnings("unchecked")
    TEST(() -> new io.datanerds.avropatch.operation.Test(generatePath(), generateValue()));

    private static final Random random = new Random();
    private static final int MAX_LIST_SIZE = 50;
    private final Supplier<Operation> operationSupplier;

    OperationGenerator(Supplier<Operation> operationSupplier) {
        this.operationSupplier = operationSupplier;
    }

    public static Operation generate() {
        OperationGenerator operationType = values()[random.nextInt(values().length)];
        return operationType.generateOperation();
    }

    private static Object generateValue() { return ValueType.generateObject(); }

    private static Path generatePath() {
        return Path.of("hello", "world");
    }

    private Operation generateOperation() {
        return operationSupplier.get();
    }

    enum ValueType {
        BOOLEAN(random::nextBoolean),
        DOUBLE(random::nextDouble),
        FLOAT(random::nextFloat),
        INTEGER(random::nextInt),
        LONG(random::nextLong),
        NULL(() -> null),
        STRING(ValueType::randomString),
        BIG_INTEGER(() -> new BigInteger(random.nextInt(256), random)),
        BIG_DECIMAL(ValueType::randomBigDecimal),
        UUID(java.util.UUID::randomUUID),
        DATE(() -> new Date(random.nextLong())),
        BIMMEL(ValueType::randomBimmel);

        private final Supplier<Object> valueSupplier;

        ValueType(Supplier<Object> valueSupplier) {
            this.valueSupplier = valueSupplier;
        }

        public static Object generateObject() {
            ValueType type = ValueType.values()[random.nextInt(ValueType.values().length)];
            if (random.nextBoolean()) {
                return type.generate();
            } else {
                return generateList(type);
            }
        }

        private static List<?> generateList(ValueType type) {
            List<Object> data = new ArrayList<>();
            for (int i = 0; i < MAX_LIST_SIZE; i++) {
                data.add(type.generate());
            }
            return data;
        }

        private Object generate() {
            return valueSupplier.get();
        }

        private static String randomString() {
            return java.util.UUID.randomUUID().toString();
        }

        private static BigDecimal randomBigDecimal() {
            BigInteger unscaledValue= new BigInteger(random.nextInt(256), random);
            int numberOfDigits = getDigitCount(unscaledValue);
            int scale = random.nextInt(numberOfDigits + 1);
            return new BigDecimal(unscaledValue, scale);
        }

        private static int getDigitCount(BigInteger number) {
            double factor = Math.log(2) / Math.log(10);
            int digitCount = (int) (factor * number.bitLength() + 1);
            if (BigInteger.TEN.pow(digitCount - 1).compareTo(number) > 0) {
                return digitCount - 1;
            }
            return digitCount;
        }

        private static Bimmel randomBimmel() {
            return new Bimmel(
                    (String) STRING.generate(),
                    (int) INTEGER.generate(),
                    (java.util.UUID) UUID.generate(),
                    new Bimmel.Bommel(randomString()));
        }
    }
}
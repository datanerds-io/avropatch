package io.datanerds.avropatch.operation;

import io.datanerds.avropatch.value.Bimmel;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public enum OperationGenerator {

    ADD(() -> new Add<>(generatePath(), generateValue())),
    COPY(() -> new Copy(generatePath(), generatePath())),
    MOVE(() -> new Move(generatePath(), generatePath())),
    REMOVE(() -> new Remove(generatePath())),
    REPLACE(() -> new Replace<>(generatePath(), generateValue())),
    TEST(() -> new io.datanerds.avropatch.operation.Test<>(generatePath(), generateValue()));

    private static final Random random = new Random();
    private static final int MAX_LIST_SIZE = 50;
    private final Supplier<Operation> operationSupplier;

    OperationGenerator(Supplier<Operation> operationSupplier) {
        this.operationSupplier = operationSupplier;
    }

    public static Operation randomOperation() {
        return values()[random.nextInt(values().length)].getOperation();
    }

    public static List<Operation> createSomeOperations(Function<Object, Operation> operationFunction) {
        List<Operation> operations = new ArrayList<>();
        operations.add(createOperation(operationFunction, "hello world"));
        operations.add(createOperation(operationFunction, 42));
        operations.add(createOperation(operationFunction, 42L));
        operations.add(createOperation(operationFunction, 123.456d));
        operations.add(createOperation(operationFunction, 123.456f));
        operations.add(createOperation(operationFunction, new BigInteger("8364789684563949576378945698056348956")));
        operations.add(createOperation(operationFunction, new BigDecimal("956740578902345.56734895627895")));
        operations.add(createOperation(operationFunction, UUID.randomUUID()));
        operations.add(createOperation(operationFunction, new Date()));

        return operations;
    }

    public static <T> Add<T> add(T value) {
        return new Add<>(Path.of("hello", "world"), value);
    }

    public static <T> Replace<T> replace(T value) {
        return new Replace<>(Path.of("hello", "world"), value);
    }

    public static <T> io.datanerds.avropatch.operation.Test<T> test(T value) {
        return new io.datanerds.avropatch.operation.Test<>(Path.of("hello", "world"), value);
    }

    private static <T> Operation createOperation(Function<T, Operation> operationFunction, T value) {
        return operationFunction.apply(value);
    }

    private static Object generateValue() { return ValueType.generateObject(); }

    private static Path generatePath() {
        return Path.of("hello", "world");
    }

    private Operation getOperation() {
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
            if (random.nextBoolean()) {
                return generateList(randomValueType());
            }
            return randomValueType().generate();
        }

        private Object generate() {
            return valueSupplier.get();
        }

        private static ValueType randomValueType() {
            return values()[random.nextInt(ValueType.values().length)];
        }

        private static List<?> generateList(ValueType type) {
            return IntStream
                    .range(0, MAX_LIST_SIZE)
                    .mapToObj(i -> type.generate())
                    .collect(Collectors.toList());
        }

        private static String randomString() {
            return java.util.UUID.randomUUID().toString();
        }

        private static BigDecimal randomBigDecimal() {
            BigInteger unscaledValue = new BigInteger(random.nextInt(256), random);
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
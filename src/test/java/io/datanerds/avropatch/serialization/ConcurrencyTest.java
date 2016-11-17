package io.datanerds.avropatch.serialization;

import com.google.common.collect.ImmutableList;
import io.datanerds.avropatch.Patch;
import io.datanerds.avropatch.operation.*;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.*;

import static io.datanerds.avropatch.operation.matcher.PatchMatcher.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConcurrencyTest {

    private static Logger logger = LoggerFactory.getLogger(ConcurrencyTest.class);
    private static final int NUMBER_OF_THREADS = 200;
    private static final int NUMBER_OF_PATCHES = 100;
    private static final int MAX_PATCH_SIZE = 50;
    private static final List<Patch> PATCHES = Collections.unmodifiableList(generateData());

    private static final CustomTypeSerializer SERIALIZER = new CustomTypeSerializer.Builder()
            .withArray()
                .withPrimitives()
                .withCustomTypes()
                .nullable()
                .with(Bimmel.class)
            .endArray()
                .withPrimitives()
                .withCustomTypes()
                .with(Bimmel.class)
                .nullable()
            .build();

    @Test
    public void customTypeSerializer() throws InterruptedException {
        CountDownLatch startThreadsLatch = new CountDownLatch(1);
        CountDownLatch threadsFinishedLatch = new CountDownLatch(NUMBER_OF_THREADS - 1);
        ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

        List<Future<List<Patch>>> futures = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            futures.add(executorService.submit(() -> {
                List<Patch> output = new ArrayList<>();
                List<Patch> input = ImmutableList.copyOf(PATCHES);
                try {
                    startThreadsLatch.await();
                    for (Patch patch : input) {
                        byte[] bytes = SERIALIZER.toBytes(patch);
                        output.add(SERIALIZER.toObject(bytes));
                    }
                } catch (Exception ex) {
                    logger.warn("Serialization thread failed.", ex);
                } finally {
                    threadsFinishedLatch.countDown();
                }
                return output;
            }));
        }

        logger.info("Releasing {} serialization threads", NUMBER_OF_THREADS);
        startThreadsLatch.countDown();
        threadsFinishedLatch.await();
        executorService.shutdownNow();


        logger.info("All threads finished, verifying output...");
        futures.stream().forEach(future -> {
            try {
                verify(PATCHES, future.get());
            } catch (InterruptedException ex) {
                logger.warn("Serialization thread failed.", ex);
            } catch (ExecutionException ex) {
                logger.warn("Serialization thread failed.", ex);
            }
        });
    }

    private static List<Patch> generateData() {
        OperationGenerator operationGenerator = new OperationGenerator();
        Random random = new Random();
        List<Patch> patches = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_PATCHES; i++) {
            Patch patch = new Patch();
            for (int j = 0; j < random.nextInt(MAX_PATCH_SIZE); j++) {
                patch.addOperation(operationGenerator.generateOperation());
            }
            patches.add(patch);
        }
        return patches;
    }

    private void verify(List<Patch> input, List<Patch> output) {
        assertThat(input, notNullValue());
        assertThat(output, notNullValue());
        assertThat(input.size(), is(CoreMatchers.equalTo(output.size())));
        for (int i = 0; i < input.size(); i++) {
            assertThat(input.get(i), is(equalTo(output.get(i))));
        }
    }

    private static class OperationGenerator {

        enum Type {ADD, COPY, MOVE, REMOVE, REPLACE, TEST}

        private final ValueGenerator valueGenerator = new ValueGenerator();
        private final Random random = new Random();

        private Path generatePath() {
            return Path.of("hello", "world");
        }

        public Operation generateOperation() {
            Type type = Type.values()[random.nextInt(Type.values().length)];
            return generateOperation(type);
        }

        public Operation generateOperation(Type type) {
            switch (type) {
                case ADD:
                    return new Add(generatePath(), valueGenerator.generate());
                case COPY:
                    return new Copy(generatePath(), generatePath());
                case MOVE:
                    return new Move(generatePath(), generatePath());
                case REMOVE:
                    return new Remove(generatePath());
                case REPLACE:
                    return new Replace<>(generatePath(), valueGenerator.generate());
                case TEST:
                    return new io.datanerds.avropatch.operation.Test(generatePath(), valueGenerator.generate());
                default:
                    throw new IllegalArgumentException("Unknown type");
            }
        }

    }


    private static class ValueGenerator {
        enum Type {BOOLEAN, DOUBLE, FLOAT, INTEGER, LONG, NULL, STRING, BIG_INTEGER, BIG_DECIMAL, UUID, DATE, BIMMEL}

        private final Random random = new Random();

        private static int getDigitCount(BigInteger number) {
            double factor = Math.log(2) / Math.log(10);
            int digitCount = (int) (factor * number.bitLength() + 1);
            if (BigInteger.TEN.pow(digitCount - 1).compareTo(number) > 0) {
                return digitCount - 1;
            }
            return digitCount;
        }

        public Object generate() {
            Type type = Type.values()[random.nextInt(Type.values().length)];
            if (random.nextBoolean()) {
                return generate(type);
            } else {
                return generateList(type);
            }
        }

        public List<?> generateList(Type type) {
            List<Object> data = new ArrayList<>();
            for (int i = 0; i < MAX_PATCH_SIZE; i++) {
                data.add(generate(type));
            }
            return data;
        }

        public Object generate(Type type) {
            switch (type) {
                case BOOLEAN:
                    return random.nextBoolean();
                case DOUBLE:
                    return random.nextDouble();
                case FLOAT:
                    return random.nextFloat();
                case INTEGER:
                    return random.nextInt();
                case LONG:
                    return random.nextLong();
                case NULL:
                    return null;
                case STRING:
                    return UUID.randomUUID().toString();
                case BIG_INTEGER:
                    return new BigInteger(random.nextInt(256), random);
                case BIG_DECIMAL:
                    BigInteger unscaledValue= new BigInteger(random.nextInt(256), random);
                    int numberOfDigits = getDigitCount(unscaledValue);
                    int scale = random.nextInt(numberOfDigits + 1);
                    return new BigDecimal(unscaledValue, scale);
                case UUID:
                    return UUID.randomUUID();
                case DATE:
                    return new Date(random.nextLong());
                case BIMMEL:
                    return new Bimmel((String)generate(Type.STRING), (int)generate(Type.INTEGER), UUID.randomUUID(),
                            new Bimmel.Bommel((String)generate(Type.STRING)));
                default:
                    throw new IllegalArgumentException(String.format("Unknown type %s", type));
            }
        }
    }
}
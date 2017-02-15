package io.datanerds.avropatch.serialization;

import com.google.common.collect.ImmutableList;
import io.datanerds.avropatch.Patch;
import io.datanerds.avropatch.operation.Operation;
import io.datanerds.avropatch.operation.OperationGenerator;
import io.datanerds.avropatch.value.Bimmel;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

import static io.datanerds.avropatch.serialization.PatchMapper.arrayBuilder;
import static io.datanerds.avropatch.serialization.PatchMapper.builder;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConcurrencyTest {

    private static Logger logger = LoggerFactory.getLogger(ConcurrencyTest.class);
    private static final int NUMBER_OF_THREADS = 200;
    private static final int NUMBER_OF_PATCHES = 100;
    private static final int MAX_PATCH_SIZE = 50;
    private static final List<Patch> PATCHES = Collections.unmodifiableList(generateData());

    private static final PatchMapper SERIALIZER = builder()
            .with(arrayBuilder()
                    .withAvroPrimitives()
                    .withCustomTypes()
                    .nullable()
                    .withType(Bimmel.class)
                    .build())
            .withAvroPrimitives()
            .withCustomTypes()
            .withType(Bimmel.class)
            .nullable()
            .build();

    @Test
    public void customTypeSerializer() throws InterruptedException {
        CountDownLatch startThreadsLatch = new CountDownLatch(1);
        CountDownLatch threadsFinishedLatch = new CountDownLatch(NUMBER_OF_THREADS - 1);
        ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

        List<Future<List<Patch>>> futures = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            futures.add(executorService.submit(() -> reserializePatches(startThreadsLatch, threadsFinishedLatch)));
        }

        logger.info("Releasing {} serialization threads", NUMBER_OF_THREADS);
        startThreadsLatch.countDown();
        threadsFinishedLatch.await();
        executorService.shutdownNow();


        logger.info("All threads finished, verifying output...");
        futures.stream().forEach(future -> {
            try {
                verify(PATCHES, future.get());
            } catch (InterruptedException | ExecutionException ex) {
                logger.warn("Serialization thread failed.", ex);
            }
        });
    }

    private List<Patch> reserializePatches(CountDownLatch startThreadsLatch, CountDownLatch threadsFinishedLatch) {
        List<Patch> output = new ArrayList<>();
        List<Patch> input = ImmutableList.copyOf(PATCHES);
        try {
            startThreadsLatch.await();
            for (Patch patch : input) {
                byte[] bytes = SERIALIZER.toBytes(patch);
                output.add(SERIALIZER.toPatch(bytes));
            }
        } catch (Exception ex) {
            logger.warn("Serialization thread failed.", ex);
        } finally {
            threadsFinishedLatch.countDown();
        }
        return output;

    }

    private static List<Patch> generateData() {
        Random random = new Random();
        List<Patch> patches = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_PATCHES; i++) {
            List<Operation> operations = new ArrayList<>();
            for (int j = 0; j < random.nextInt(MAX_PATCH_SIZE); j++) {
                operations.add(OperationGenerator.randomOperation());
            }
            patches.add(new Patch<>(UUID.randomUUID(), operations));
        }
        return patches;
    }

    private void verify(List<Patch> input, List<Patch> output) {
        assertThat(input, notNullValue());
        assertThat(output, notNullValue());
        assertThat(input.size(), is(equalTo(output.size())));
        for (int i = 0; i < input.size(); i++) {
            assertThat(input.get(i), is(equalTo(output.get(i))));
        }
    }
}
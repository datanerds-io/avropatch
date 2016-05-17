package io.datanerds.avropatch.operation;

import org.apache.avro.reflect.Union;

@Union({Add.class, Copy.class, Move.class, Remove.class, Replace.class, Test.class})
public interface Operation {
}

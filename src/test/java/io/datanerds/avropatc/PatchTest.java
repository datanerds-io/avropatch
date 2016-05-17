package io.datanerds.avropatc;

import io.datanerds.avropatch.Patch;
import org.apache.avro.Schema;
import org.apache.avro.reflect.ReflectData;
import org.junit.Test;

public class PatchTest {

    @Test
    public void debug() {
        Schema schema = ReflectData.get().getSchema(Patch.class);
        System.out.println(schema.toString());
    }

}
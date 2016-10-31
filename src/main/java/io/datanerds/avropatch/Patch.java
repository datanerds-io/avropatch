package io.datanerds.avropatch;

import io.datanerds.avropatch.operation.Operation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Patch {

    private final List<Operation> operations;

    public Patch() {
        operations = new ArrayList<>();
    }

    public Patch(List<Operation> operations) {
        this.operations = new ArrayList<>(operations);
    }

    public boolean addOperation(Operation operation) {
        return operations.add(operation);
    }

    public List<Operation> getOperations() {
        return Collections.unmodifiableList(operations);
    }
}

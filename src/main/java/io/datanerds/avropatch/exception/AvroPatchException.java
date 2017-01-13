package io.datanerds.avropatch.exception;

public abstract class AvroPatchException extends RuntimeException {

    AvroPatchException(String message) {
        super(message);
    }
}

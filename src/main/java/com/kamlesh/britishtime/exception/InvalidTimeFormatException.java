package com.kamlesh.britishtime.exception;

/**
 * Unchecked exception for invalid time input.
 */
public class InvalidTimeFormatException extends RuntimeException {
    public InvalidTimeFormatException(String message) {
        super(message);
    }
}

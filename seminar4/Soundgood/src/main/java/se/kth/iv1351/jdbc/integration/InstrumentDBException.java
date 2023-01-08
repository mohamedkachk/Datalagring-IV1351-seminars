package se.kth.iv1351.jdbc.integration;

public class InstrumentDBException extends Exception {

    /**
     * Create a new instance thrown because of the specified reason.
     *
     * @param reason Why the exception was thrown.
     */
    public InstrumentDBException(String reason) {
        super(reason);
    }

    /**
     * Create a new instance thrown because of the specified reason and exception.
     *
     * @param reason Why the exception was thrown.
     * @param rootCause The exception that caused this exception to be thrown.
     */
    public InstrumentDBException(String reason, Throwable rootCause) {
        super(reason, rootCause);
    }
}
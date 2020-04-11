package com.walmart.labs.ads.keyword.exception;

/**
 * General serialization error in Keyword Bid Service.
 */
public class SerializationException extends RuntimeException {
    public SerializationException(String msg) {
        super(msg);
    }

    public SerializationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

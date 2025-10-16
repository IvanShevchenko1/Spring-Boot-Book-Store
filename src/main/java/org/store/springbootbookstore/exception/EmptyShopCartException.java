package org.store.springbootbookstore.exception;

public class EmptyShopCartException extends RuntimeException {
    public EmptyShopCartException(String message) {
        super(message);
    }
}

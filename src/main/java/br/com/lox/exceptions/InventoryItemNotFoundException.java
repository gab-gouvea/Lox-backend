package br.com.lox.exceptions;

public class InventoryItemNotFoundException extends RuntimeException {
    public InventoryItemNotFoundException(String message) {
        super(message);
    }
}

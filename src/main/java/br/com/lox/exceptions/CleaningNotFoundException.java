package br.com.lox.exceptions;

public class CleaningNotFoundException extends RuntimeException {
    public CleaningNotFoundException(String message) {
        super(message);
    }
}

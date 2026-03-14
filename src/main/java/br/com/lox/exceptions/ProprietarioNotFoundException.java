package br.com.lox.exceptions;

public class ProprietarioNotFoundException extends RuntimeException {
    public ProprietarioNotFoundException(String message) {
        super(message);
    }
}

package br.com.lox.exceptions;

public class LocacaoNotFoundException extends RuntimeException {
    public LocacaoNotFoundException(String message) {
        super(message);
    }
}

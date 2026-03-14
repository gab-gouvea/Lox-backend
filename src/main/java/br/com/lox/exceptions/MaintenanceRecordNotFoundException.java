package br.com.lox.exceptions;

public class MaintenanceRecordNotFoundException extends RuntimeException {
    public MaintenanceRecordNotFoundException(String message) {
        super(message);
    }
}

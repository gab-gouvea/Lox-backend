package br.com.lox.exceptions;

public class ScheduledMaintenanceNotFoundException extends RuntimeException {
    public ScheduledMaintenanceNotFoundException(String message) {
        super(message);
    }
}

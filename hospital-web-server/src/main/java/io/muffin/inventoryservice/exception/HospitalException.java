package io.muffin.inventoryservice.exception;

public class HospitalException extends RuntimeException{

    public HospitalException() {}
    public HospitalException(String message) { super(message); }
    public HospitalException(String message, Throwable cause) { super(message, cause); }
}

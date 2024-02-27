package com.watcher.custom_exception;

public class AddressNotFoundException extends RuntimeException{
	public AddressNotFoundException(String message) {
        super(message);
    }
}

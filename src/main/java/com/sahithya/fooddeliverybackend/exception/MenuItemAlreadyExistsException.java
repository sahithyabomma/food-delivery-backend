package com.sahithya.fooddeliverybackend.exception;

public class MenuItemAlreadyExistsException extends RuntimeException {

    public MenuItemAlreadyExistsException(String name) {
        super("Menu item '" + name + "' already exists in this restaurant");
    }
}
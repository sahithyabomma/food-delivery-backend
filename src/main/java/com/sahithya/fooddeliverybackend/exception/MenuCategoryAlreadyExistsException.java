package com.sahithya.fooddeliverybackend.exception;

public class MenuCategoryAlreadyExistsException
        extends RuntimeException {

    public MenuCategoryAlreadyExistsException(String name) {
        super("Menu category '" + name + "' already exists");
    }
}
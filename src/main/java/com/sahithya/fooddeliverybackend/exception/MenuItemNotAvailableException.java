package com.sahithya.fooddeliverybackend.exception;

import java.util.UUID;

public class MenuItemNotAvailableException extends RuntimeException{
    public MenuItemNotAvailableException(UUID uuid) {
        super("menu item is not available");
    }
}

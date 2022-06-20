package com.progressoft.tools.exception;

public class DestinationDirectoryNotFoundException extends IllegalArgumentException {
    public DestinationDirectoryNotFoundException(){
        super("Destination directory not found");
    }
}

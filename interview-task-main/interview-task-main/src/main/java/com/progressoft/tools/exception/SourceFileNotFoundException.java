package com.progressoft.tools.exception;

public class SourceFileNotFoundException extends IllegalArgumentException{
    public SourceFileNotFoundException(){
        super("source file not found");
    }
}

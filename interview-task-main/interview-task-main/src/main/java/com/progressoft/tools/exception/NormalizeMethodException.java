package com.progressoft.tools.exception;

public class NormalizeMethodException extends IllegalArgumentException{
    public NormalizeMethodException(String name){
        super("Normalized Method " + name +" not matched");
    }
}

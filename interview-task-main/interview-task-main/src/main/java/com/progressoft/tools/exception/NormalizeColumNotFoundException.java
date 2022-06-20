package com.progressoft.tools.exception;

public class NormalizeColumNotFoundException extends IllegalArgumentException{
    public NormalizeColumNotFoundException(String colum){
        super("column " + colum + " not found");
    }
}

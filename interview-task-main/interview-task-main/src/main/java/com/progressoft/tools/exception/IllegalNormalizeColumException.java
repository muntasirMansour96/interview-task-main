package com.progressoft.tools.exception;

public class IllegalNormalizeColumException extends NumberFormatException{
    public IllegalNormalizeColumException(String colum){
        super("cannot make normalize to colum " + colum);
    }
}

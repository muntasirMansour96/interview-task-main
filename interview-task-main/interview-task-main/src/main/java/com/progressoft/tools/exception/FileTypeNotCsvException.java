package com.progressoft.tools.exception;

public class FileTypeNotCsvException extends IllegalArgumentException{
    public FileTypeNotCsvException(String kind){
        super(kind + " file not csv exception");
    }
}

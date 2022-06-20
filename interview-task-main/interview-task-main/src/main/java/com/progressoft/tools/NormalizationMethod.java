package com.progressoft.tools;

import com.progressoft.tools.exception.NormalizeMethodException;

public enum NormalizationMethod {
    Z_SCORE("z-score"),
    MIN_MAX("min-max");

    public final String name;
    NormalizationMethod(String name){
        this.name = name;
    }

    public static NormalizationMethod getNormalizeMethodByName(String name){
        switch (name){
            case "z-score":{
                return NormalizationMethod.Z_SCORE;
            }
            case "min-max":{
                return NormalizationMethod.MIN_MAX;
            }
            default:{
                throw new NormalizeMethodException(name);
            }
        }
    }

}

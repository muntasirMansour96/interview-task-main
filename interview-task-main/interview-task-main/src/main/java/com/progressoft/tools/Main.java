package com.progressoft.tools;

import com.progressoft.tools.exception.NormalizeMethodException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private static final Normalizer normalizer = new NormalizerImplementation();

    public static void main(String[] args) {
        final Path SOURCE_PATH = Paths.get(args[0]);
        final Path DEST_PATH = Paths.get(args[1]);
        final String COL_TO_NORMALIZE = args[2];
        final NormalizationMethod METHOD;
        final ScoringSummary SCORING_SUMMARY;
        try {
            METHOD = NormalizationMethod.getNormalizeMethodByName(args[3]);
        }catch (NormalizeMethodException e){
            e.printStackTrace();
            return;
        }
        switch (METHOD){
            case Z_SCORE:{
                SCORING_SUMMARY = normalizer.zscore(SOURCE_PATH,DEST_PATH,COL_TO_NORMALIZE);
                printResult(SCORING_SUMMARY);
                break;
            }
            case MIN_MAX:{
                SCORING_SUMMARY = normalizer.minMaxScaling(SOURCE_PATH,DEST_PATH,COL_TO_NORMALIZE);
                printResult(SCORING_SUMMARY);
                break;
            }
        }
    }

    private static void printResult(final ScoringSummary SCORING_SUMMARY){
        System.out.println("************************************\tRESULTS\t******************************************************");
        System.out.println("mean = "+SCORING_SUMMARY.mean());
        System.out.println("standard deviation = "+SCORING_SUMMARY.standardDeviation());
        System.out.println("variance = "+SCORING_SUMMARY.variance());
        System.out.println("median = "+SCORING_SUMMARY.median());
        System.out.println("min = "+SCORING_SUMMARY.min());
        System.out.println("max = "+SCORING_SUMMARY.max());
        System.out.println("************************************\tEND\t******************************************************");
    }

}

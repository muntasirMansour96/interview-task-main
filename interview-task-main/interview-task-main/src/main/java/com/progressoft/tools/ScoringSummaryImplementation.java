package com.progressoft.tools;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ScoringSummaryImplementation implements ScoringSummary{

    private List<BigDecimal> dataToNormalize;

    public ScoringSummaryImplementation(List<BigDecimal> dataToNormalize) {
        this.dataToNormalize = dataToNormalize;
    }

    @Override
    public BigDecimal mean() {
          Double mean = dataToNormalize.stream()
                  .mapToDouble(BigDecimal::doubleValue)
                  .average()
                  .getAsDouble();
          return new BigDecimal(mean)
                  .setScale(0, RoundingMode.HALF_EVEN).setScale(2);
    }

    @Override
    public BigDecimal standardDeviation() {
        double standardDeviation = 0.0;
        List<Double> valueAsDouble = dataToNormalize.stream()
                .map(BigDecimal::doubleValue)
                .collect(Collectors.toList());
        for(double num: valueAsDouble){
            standardDeviation += Math.pow(num - mean().doubleValue(), 2);}
        double result = Math
                .sqrt(standardDeviation/dataToNormalize.size());
        return new BigDecimal(result)
                .setScale(2,RoundingMode.UP).setScale(2);
    }

    @Override
    public BigDecimal variance() {
        double summation = dataToNormalize.stream()
                .mapToDouble(BigDecimal::doubleValue)
                .map(val -> val - mean().doubleValue())
                .map(val -> Math.pow(val,2))
                .sum();
        return new BigDecimal(summation/dataToNormalize.size())
                .setScale(0, RoundingMode.HALF_EVEN).setScale(2);
    }

    @Override
    public BigDecimal median() {
        List<BigDecimal> sortedData = dataToNormalize.stream()
                .sorted()
                .collect(Collectors.toList());
            return sortedData.get(sortedData.size()/2)
                    .setScale(0, RoundingMode.HALF_EVEN).setScale(2);
    }

    @Override
    public BigDecimal min() {
        return dataToNormalize.stream()
                .min(Comparator.comparing(a->a)).get()
                .setScale(0, RoundingMode.HALF_EVEN).setScale(2);
    }

    @Override
    public BigDecimal max() {
        return dataToNormalize.stream()
                .max(Comparator.comparing(a->a)).get()
                .setScale(0,RoundingMode.HALF_EVEN).setScale(2);
    }
}

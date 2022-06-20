package com.progressoft.tools;

import com.progressoft.tools.exception.*;
import com.progressoft.tools.fileHandling.CsvFileHandling;
import com.progressoft.tools.fileHandling.CsvFileHandlingImplementation;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class NormalizerImplementation implements Normalizer{

    private final CsvFileHandling csvFileHandling;

    public NormalizerImplementation() {
        this.csvFileHandling = new CsvFileHandlingImplementation();
    }

    @Override
    public ScoringSummary zscore(Path csvPath, Path destPath, String colToStandardize) {
        ScoringSummary scoringSummary = null;
        if(checkSourcePath(csvPath) && checkDestinationPath(destPath)){
            List<String> sourceData = csvFileHandling.getDataFromCsvFile(csvPath);
            if(checkNormalizationColum(sourceData,colToStandardize)){
                List<BigDecimal> dataToNormalize = csvFileHandling.getDataOfColToNormalization(sourceData, colToStandardize);
                scoringSummary = new ScoringSummaryImplementation(dataToNormalize);
                List<BigDecimal> zScoreData = calculateZScore(dataToNormalize, scoringSummary.mean().doubleValue(), scoringSummary.standardDeviation().doubleValue());
                List<String> destinationFileData = csvFileHandling.prepareDestinationFileData(zScoreData, sourceData, colToStandardize, "z");
                csvFileHandling.insertDataToDestinationCsvFile(destinationFileData, destPath);
            }
        }
        return scoringSummary;
    }

    @Override
    public ScoringSummary minMaxScaling(Path csvPath, Path destPath, String colToNormalize) {
        ScoringSummary scoringSummary = null;
        if(checkSourcePath(csvPath) && checkDestinationPath(destPath)){
            List<String> sourceData = csvFileHandling.getDataFromCsvFile(csvPath);
            if(checkNormalizationColum(sourceData,colToNormalize)){
                List<BigDecimal> dataToNormalize = csvFileHandling.getDataOfColToNormalization(sourceData, colToNormalize);
                scoringSummary = new ScoringSummaryImplementation(dataToNormalize);
                List<BigDecimal> minMaxData = calculateMinMaxScale(dataToNormalize, scoringSummary.min().doubleValue(), scoringSummary.max().doubleValue());
                List<String> destinationFileData = csvFileHandling.prepareDestinationFileData(minMaxData, sourceData, colToNormalize, "mm");
                csvFileHandling.insertDataToDestinationCsvFile(destinationFileData, destPath);
            }
        }
        return scoringSummary;
    }

    private List<BigDecimal> calculateZScore(List<BigDecimal> dataToNormalize , double mean , double standardDeviation){
        return dataToNormalize.stream()
                .map(BigDecimal::doubleValue)
                .map(
                        val->(val-mean)/standardDeviation
                )
                .map(val->new BigDecimal(val)
                        .setScale(2, RoundingMode.HALF_EVEN)
                )
                .collect(Collectors.toList());
    }

    private List<BigDecimal> calculateMinMaxScale(List<BigDecimal> dataToNormalize , double min , double max){
        return dataToNormalize.stream()
                .map(BigDecimal::doubleValue)
                .map(
                        val-> (val-min)/(max-min)
                )
                .map(val->new BigDecimal(val)
                        .setScale(2, RoundingMode.HALF_EVEN)
                )
                .collect(Collectors.toList());
    }

    /**
     *
     * @param sourcePath : source path
     * @return : return true if all validation success otherwise throw custom exception
     */
    private boolean checkSourcePath(Path sourcePath){
        if (!Files.exists(sourcePath)) {
            throw new SourceFileNotFoundException();
        }else if (!csvFileHandling.isFileCsvType(sourcePath)) {
            throw new FileTypeNotCsvException("Source");
        }else
            return true;
    }

    /**
     *
     * @param destPath : destination path
     * @return : return true if all validation success otherwise throw custom exception
     */
    private boolean checkDestinationPath(Path destPath){
        if (!csvFileHandling.checkDestinationDirectory(destPath)) {
            throw new DestinationDirectoryNotFoundException();
        }else if (!csvFileHandling.isFileCsvType(destPath)) {
            throw new FileTypeNotCsvException("Destination");
        }else
            return true;
    }

    /**
     *
     * @param data : all data inside CSV source file
     * @param colToNormalize : name of colum to be normalized
     * @return : return true if all validation success otherwise throw custom exception
     */
    private boolean checkNormalizationColum(List<String> data, String colToNormalize){
        if (csvFileHandling.indexOfStandardizeCol(data, colToNormalize) == -1) {
            throw new NormalizeColumNotFoundException(colToNormalize);
        } else if (!csvFileHandling.colCanNormalize(data, colToNormalize)) {
            throw new IllegalNormalizeColumException(colToNormalize);
        }else
            return true;
    }

}

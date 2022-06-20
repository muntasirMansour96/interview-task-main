package com.progressoft.tools.fileHandling;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;

public interface CsvFileHandling {

    List<String> getDataFromCsvFile(Path csvFilepath);
    List<BigDecimal> getDataOfColToNormalization(List<String> data, String colToStandardize);
    List<String> prepareDestinationFileData(List<BigDecimal> normalizeCol , List<String>data, String colToStandardize, String method);
    boolean insertDataToDestinationCsvFile(List<String> stringLineData,Path destPath);
    int indexOfStandardizeCol(List<String> data, String colToStandardize);
    boolean colCanNormalize(List<String> data, String colToStandardize);
    boolean checkDestinationDirectory(Path destPath);
    boolean isFileCsvType(Path filePath);

    }

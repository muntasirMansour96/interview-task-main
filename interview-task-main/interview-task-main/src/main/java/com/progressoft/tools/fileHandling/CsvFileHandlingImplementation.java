package com.progressoft.tools.fileHandling;

import com.progressoft.tools.exception.NormalizeColumNotFoundException;
import com.progressoft.tools.exception.SourceFileNotFoundException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvFileHandlingImplementation implements CsvFileHandling{

    @Override
    public List<String> getDataFromCsvFile(Path csvFilepath) throws SourceFileNotFoundException {
        List<String> sourceData = new ArrayList<>();
        try {
            sourceData = Files.readAllLines(csvFilepath);
            return sourceData;
        }catch (IOException e) {
            e.getMessage();
        }
        return sourceData;
    }

    @Override
    public List<BigDecimal> getDataOfColToNormalization(List<String> data, String colToStandardize) throws NormalizeColumNotFoundException {
        List<String> header = Arrays.asList(data.get(0).split(","));
        if(header.contains(colToStandardize)){
            int indexOfStandardizeCol = header.indexOf(colToStandardize);
            List<BigDecimal> dataToNormalize = new ArrayList<>();
            for (int i =1 ; i<data.size();i++){
                String value = data.get(i).split(",")[indexOfStandardizeCol];
                dataToNormalize.add(new BigDecimal(value));
            }
            return dataToNormalize;
        }else{
            System.out.println("column "+colToStandardize+" not found");
        }
        return null;
    }

    public List<String> prepareDestinationFileData(List<BigDecimal> normalizeCol , List<String>data, String colToStandardize, String method){
        int indexOfStandardizeCol = indexOfStandardizeCol(data,colToStandardize);
        List<String> destinationData = new ArrayList<>();
        List<String> tempLine;
        String tmpString ="";
        for(int i =0; i<data.size();i++){
           tempLine = Arrays.asList(data.get(i).split(","));
           tmpString = tempLine.get(0);
           for(int j =1; j<tempLine.size();j++){
                if(j== indexOfStandardizeCol)  {
                    if(i !=0 ){
                    tmpString = tmpString+ "," + tempLine.get(indexOfStandardizeCol) + "," + normalizeCol.get(i-1);
                    }else{
                        tmpString = tmpString+"," + tempLine.get(indexOfStandardizeCol) + ","  + colToStandardize + "_" + method;
                    }
                    continue;
                }
                tmpString = tmpString + "," + tempLine.get(j);
           }
           destinationData.add(tmpString);
        }
        return destinationData;
    }

    public int indexOfStandardizeCol(List<String> data, String colToStandardize){
        List<String> header = Arrays.asList(data.get(0).split(","));
        if(header.contains(colToStandardize)){
            return header.indexOf(colToStandardize);
        }else{
            return -1;
        }
    }

    @Override
    public boolean colCanNormalize(List<String> data, String colToStandardize) {
        int index = indexOfStandardizeCol(data,colToStandardize);
        String value = Arrays.asList(data.get(1).split(",")).get(index);
        try {
            Double.parseDouble(value);
        }catch (NumberFormatException e){
            return false;
        }
        return true;
    }

    @Override
    public boolean insertDataToDestinationCsvFile(List<String> stringLineData, Path destPath) {
            try {
                Files.deleteIfExists(destPath);
                final Path FILE_PATH = Files.createFile(destPath);
                try (BufferedWriter writer = Files.newBufferedWriter(FILE_PATH, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
                    for (String line:stringLineData){
                        writer.write(line);
                        writer.newLine();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
    }

    @Override
    public boolean checkDestinationDirectory(Path destPath){
        Path destDirectory = destPath.getParent();
        return Files.isDirectory(destDirectory);
    }

    @Override
    public boolean isFileCsvType(Path filePath){
        String fileName = filePath.getFileName().toString();
        if( fileName.length() > 4 &&
                fileName.lastIndexOf(".csv") + 4 == fileName.length() &&
                fileName.indexOf('.') == fileName.lastIndexOf(".csv")
        )
            return true;
        else
            return false;
    }

}

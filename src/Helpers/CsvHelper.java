package Helpers;

import Models.CsvItemModel;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.collections4.IteratorUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvHelper {

    public static ArrayList<CsvItemModel> initCSVItems(File inputCsvFile) {
        ArrayList csvFileData = new ArrayList<CsvItemModel>();
        if (inputCsvFile == null){
            return csvFileData;
        }
        try {
            List<String> lines = Files.readAllLines(Paths.get(inputCsvFile.getAbsolutePath()));
            StringBuilder buffer = new StringBuilder();
            for(String current : lines) {
                buffer.append(current).append(System.lineSeparator());
            }
            BufferedReader buffReader = new BufferedReader(new StringReader(buffer.toString()));
            CsvToBean<CsvItemModel> csvToBean = new CsvToBeanBuilder(buffReader)
                    .withType(CsvItemModel.class)
                    .withFieldAsNull(CSVReaderNullFieldIndicator.NEITHER)
                    .build();
            csvFileData.addAll(IteratorUtils.toList(csvToBean.iterator()));
            buffReader.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return csvFileData;
    }

    public static void saveCSVItems(File inputFile, ArrayList<CsvItemModel> csvData) {
        if (csvData == null || csvData.size() == 0) {
            return;
        }
        Writer writer;
        try {
            writer = Files.newBufferedWriter(Paths.get(inputFile.getAbsolutePath()));
            StatefulBeanToCsv<CsvItemModel> beanToCsv = new StatefulBeanToCsvBuilder(writer)
                    .withQuotechar(CSVWriter.DEFAULT_QUOTE_CHARACTER)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .build();
            beanToCsv.write(csvData);
            writer.close();
        } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            System.out.println(e.getMessage());
        }
    }
}

package Servcies;

import Models.CsvItemModel;
import Utils.DirUtils;
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

public class InputDataService {
    private static List<CsvItemModel> inputCsvModelItems;
    private static File inputDataFile;

    public InputDataService() {
        if (inputCsvModelItems == null) {
            inputCsvModelItems = new ArrayList<>();
        }
    }

    public File getInputDataFile() {
        return inputDataFile;
    }

    public List<CsvItemModel> getInputCsvModelItems() {
        return inputCsvModelItems;
    }

    public void initInputFile(File filePath) {
        if (DirUtils.isFileOk(filePath, "csv")) {
            inputDataFile = filePath;
        }
    }

    public void initInputFileData() {
        if (inputDataFile == null){
            return;
        }
        try {
            List<String> lines = Files.readAllLines(Paths.get(inputDataFile.getAbsolutePath()));
            StringBuilder buffer = new StringBuilder();
            for(String current : lines) {
                buffer.append(current).append(System.lineSeparator());
            }
            BufferedReader buffReader = new BufferedReader(new StringReader(buffer.toString()));
            CsvToBean<CsvItemModel> csvToBean = new CsvToBeanBuilder<CsvItemModel>(buffReader)
                    .withType(CsvItemModel.class)
                    .withFieldAsNull(CSVReaderNullFieldIndicator.NEITHER)
                    .build();
            inputCsvModelItems = new ArrayList<>(IteratorUtils.toList(csvToBean.iterator()));
            buffReader.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void updateResultCsvItems() {
        if (inputCsvModelItems == null || inputCsvModelItems.size() == 0) {
            return;
        }
        try {
            Writer writer = Files.newBufferedWriter(Paths.get(inputDataFile.getAbsolutePath()));
            StatefulBeanToCsv<CsvItemModel> beanToCsv = new StatefulBeanToCsvBuilder<CsvItemModel>(writer)
                    .withQuotechar(CSVWriter.DEFAULT_QUOTE_CHARACTER)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .build();
            beanToCsv.write(inputCsvModelItems);
            writer.close();
        } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            System.out.println(e.getMessage());
        }
    }

    public void clearInputDataFile() {
        inputDataFile = null;
    }
}

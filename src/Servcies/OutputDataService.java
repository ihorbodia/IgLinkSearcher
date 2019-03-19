package Servcies;

import Models.CsvItemModel;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.tinylog.Logger;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class OutputDataService {

    private File outputFile;
    public OutputDataService() {
    }

    private void createEmptyCSVFile(File outputFile, String[] columns) {
        FileWriter mFileWriter;
        try {
            mFileWriter = new FileWriter(outputFile.getAbsoluteFile());
            CSVWriter mCsvWriter = new CSVWriter(mFileWriter);
            mCsvWriter.writeNext(columns);
            mCsvWriter.close();
            mFileWriter.close();
        } catch (IOException e) {
            Logger.tag("SYSTEM").error(e, "Cannot create empty output multiple searching results file");
        }
    }

    public static void saveResultCsvItems(File inputFile, ArrayList<CsvItemModel> csvData) {
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

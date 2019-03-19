package Servcies;

import Models.CsvItemModel;
import Utils.DirUtils;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;
import org.apache.commons.collections4.IteratorUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
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
        ArrayList csvFileData = new ArrayList<CsvItemModel>();
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
            CsvToBean<CsvItemModel> csvToBean = new CsvToBeanBuilder(buffReader)
                    .withType(CsvItemModel.class)
                    .withFieldAsNull(CSVReaderNullFieldIndicator.NEITHER)
                    .build();
            csvFileData.addAll(IteratorUtils.toList(csvToBean.iterator()));
            buffReader.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void clearInputDataFile() {
        inputDataFile = null;
    }
}

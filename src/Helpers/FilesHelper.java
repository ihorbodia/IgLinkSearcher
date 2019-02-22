package Helpers;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

class FilesHelper {

    static File setUpInputFile(String inputFilePath) {
        if (!FilenameUtils.getExtension(inputFilePath).equalsIgnoreCase("csv")) {
            System.out.println("Selected input file has invalid format or file not selected");
            return null;
        }
        File inFile = new File(inputFilePath);
        if (StringUtils.isEmpty(inputFilePath) && !inFile.exists()) {
            return null;
        }
        return inFile;
    }
}

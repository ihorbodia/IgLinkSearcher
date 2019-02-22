package Helpers;

import java.io.*;
import java.util.Properties;

public class PropertiesHelper {

    private File propertiesFile;
    private Properties properties;

    private String indexProperty = "index";
    private String isIgSearchProperty = "igSearch";
    private String isTwitterSearchProperty = "twitterSearch";
    private String isWorkProperty = "isWorked";
    private String selectedInputFileProperty = "selectedCsvInputFile";

    public PropertiesHelper() {
        properties = new Properties();
    }

    public void saveIndex(int index) {
        saveProperty(indexProperty, String.valueOf(index));
    }
    public void saveIsIgSearch(boolean isIgSearch) {
        saveProperty(isIgSearchProperty, String.valueOf(isIgSearch));
    }
    public void saveIsTwitterSearch(boolean isTwitterSearch) {
        saveProperty(isTwitterSearchProperty, String.valueOf(isTwitterSearch));
    }
    public void saveIsWork(boolean isWorked) {
        saveProperty(isWorkProperty, String.valueOf(isWorked));
    }
    public void saveSelectedInputFile(String filePath) {
        saveProperty(selectedInputFileProperty, filePath);
    }

    public int getIndex() {
        return Integer.parseInt(restoreProperty(indexProperty));
    }
    public boolean getIsIgSearch() {
        return Boolean.valueOf(restoreProperty(isIgSearchProperty));
    }
    public boolean getIsTwitterSearch() {
        return Boolean.valueOf(restoreProperty(isTwitterSearchProperty));
    }
    public boolean getIsWork() {
        return Boolean.valueOf(restoreProperty(isWorkProperty));
    }
    public String getSelectedInputFile() {
        return restoreProperty(selectedInputFileProperty);
    }

    private void saveProperty(String propertyName, String value) {
        OutputStream output = null;
        try {
            output = new FileOutputStream(propertiesFile.getAbsoluteFile());
            properties.setProperty(propertyName, value);
            properties.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
            System.out.println(io.getMessage());
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
    private void createNewFile() {
        OutputStream output = null;
        try {
            File propertiesFileTemp = File.createTempFile("configIS", ".properties");
            String propPath = propertiesFileTemp.getAbsolutePath().substring(0, propertiesFileTemp.getAbsolutePath().lastIndexOf(File.separator)) + File.separator + "configIS.properties";
            File f = new File(propPath);
            if (f.exists() && !f.isDirectory()) {
                propertiesFile = f;
            } else {
                propertiesFile = f;
                f.createNewFile();
                output = new FileOutputStream(propertiesFile.getAbsoluteFile());
                properties.setProperty("index", "0");
                properties.setProperty("isWorked", "false");
                properties.setProperty("selectedCsvInputFile", "");
                properties.store(output, null);
            }
            propertiesFileTemp.delete();
        } catch (IOException io) {
            io.printStackTrace();
            System.out.println(io.getMessage());
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
    private String restoreProperty(String propertyName) {
        String result = "0";
        InputStream input = null;
        try {
            createNewFile();
            input = new FileInputStream(propertiesFile.getAbsoluteFile());
            properties.load(input);
            if (properties.get(propertyName) != null) {
                result = properties.get(propertyName).toString();
            }
        } catch (IOException ex) {
            System.out.println(ex.getStackTrace());
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println(e.getStackTrace());
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}

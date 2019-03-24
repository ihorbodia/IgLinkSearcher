package Servcies;

import org.tinylog.Logger;
import java.io.*;
import java.util.Properties;

public class PropertiesService {

    private File propertiesFile;
    private Properties properties;

    private String indexProperty = "index";
    private String isIgSearchProperty = "igSearch";
    private String isTwitterSearchProperty = "twitterSearch";
    private String isWorkProperty = "isWorked";
    private String selectedInputFileProperty = "selectedCsvInputFile";

    public PropertiesService() {
        properties = new Properties();
        createNewFileIfNotExists();
    }

    private synchronized void saveProperty(String propertyName, String value) {
        OutputStream output = null;
        try {
            output = new FileOutputStream(propertiesFile.getAbsoluteFile());
            properties.setProperty(propertyName, value);
            properties.store(output, null);
        } catch (IOException io) {
            Logger.tag("SYSTEM").error(io);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    Logger.tag("SYSTEM").error(e);
                }
            }
        }
    }

    public void saveIndex(int index) {
        saveProperty(indexProperty, String.valueOf(index));
    }
    public void saveIsIgSearch(boolean isIgSearch) { saveProperty(isIgSearchProperty, String.valueOf(isIgSearch)); }
    public void saveIsTwitterSearch(boolean isTwitterSearch) { saveProperty(isTwitterSearchProperty, String.valueOf(isTwitterSearch)); }
    public void saveIsWork(boolean isWorked) {
        saveProperty(isWorkProperty, String.valueOf(isWorked));
    }
    public void saveSelectedInputFile(String filePath) { saveProperty(selectedInputFileProperty, filePath); }

    public int getIndex() {
        return Integer.parseInt(restoreProperty(indexProperty));
    }
    public boolean getIsIgSearch() { return Boolean.valueOf(restoreProperty(isIgSearchProperty)); }
    public boolean getIsTwitterSearch() { return Boolean.valueOf(restoreProperty(isTwitterSearchProperty)); }
    public boolean getIsWork() { return Boolean.valueOf(restoreProperty(isWorkProperty)); }
    public File getSelectedInputFile() {
        return new File(restoreProperty(selectedInputFileProperty));
    }

    private synchronized void createNewFileIfNotExists() {
        OutputStream output = null;
        try {
            File file = new File("configIS.properties");
            if (file.exists() && !file.isDirectory()) {
                propertiesFile = file;
            } else {
                propertiesFile = file;
                if (file.createNewFile()) {
                    output = new FileOutputStream(propertiesFile.getAbsoluteFile());
                    properties.setProperty(indexProperty, "0");
                    properties.setProperty(isWorkProperty, "false");
                    properties.setProperty(selectedInputFileProperty, "");
                    properties.setProperty(isIgSearchProperty, "false");
                    properties.setProperty(isTwitterSearchProperty, "false");
                    properties.store(output, null);
                }
            }
        } catch (IOException io) {
            Logger.tag("SYSTEM").error(io);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    Logger.tag("SYSTEM").error(e);
                }
            }
        }
    }

    private synchronized String restoreProperty(String propertyName) {
        String result = "";
        InputStream input = null;
        try {
            input = new FileInputStream(propertiesFile.getAbsoluteFile());
            properties.load(input);
            if (properties.get(propertyName) != null) {
                result = properties.get(propertyName).toString();
            }
        } catch (IOException ex) {
            Logger.error(ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    Logger.error(e);
                }
            }
        }
        return result;
    }
}

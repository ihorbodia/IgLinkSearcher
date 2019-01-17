import java.io.*;
import java.util.Properties;

public class PropertiesHelper {

    File propertiesFile;
    Properties properties;

    public PropertiesHelper() {
        properties = new Properties();
    }

    public void saveProperties() {
        OutputStream output = null;
        try {
            output = new FileOutputStream(propertiesFile.getAbsoluteFile());
         //   properties.setProperty("index", String.valueOf(currentIndex));
        //    properties.setProperty("isWorked", String.valueOf(isWorked));
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
            File propertiesFileTemp = File.createTempFile("configMS", ".properties");
            String propPath = propertiesFileTemp.getAbsolutePath().substring(0, propertiesFileTemp.getAbsolutePath().lastIndexOf(File.separator)) + File.separator + "configMS.properties";
            File f = new File(propPath);
            if (f.exists() && !f.isDirectory()) {
                propertiesFile = f;
            } else {
                propertiesFile = f;
                f.createNewFile();
                output = new FileOutputStream(propertiesFile.getAbsoluteFile());
                properties.setProperty("index", "1");
                properties.setProperty("isWorked", "false");
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

    public void restoreProperties() {
        InputStream input = null;
        try {
            createNewFile();
            input = new FileInputStream(propertiesFile.getAbsoluteFile());
            properties.load(input);

            if (properties.get("index") != null) {
                String currentIndexStr = properties.get("index").toString();
                //currentIndex = Integer.parseInt(currentIndexStr);
            }

            if (properties.get("isWorked") != null) {
                String isWorkStr = properties.get("isWorked").toString();
                //isWorked = Boolean.parseBoolean(isWorkStr);
            }

           // if (isWorked) {
            //    Run();
           // }
            else {
               // labelStatusData.setText("press Run to start");
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

}

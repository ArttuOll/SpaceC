package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {
    Properties properties = new Properties();

    public PropertiesReader(String path) {
        try (InputStream input = new FileInputStream(path)) {
            properties.load(input);
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.printf("Could not find file: %s%n", fileNotFoundException);
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            System.out.printf("Reading file failed: %s%n", ioException);
            ioException.printStackTrace();
        }
    }

    public String getString(String key) {
        return properties.getProperty(key);
    }
}

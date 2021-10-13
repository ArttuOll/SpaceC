package utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileToStringConverter {

    public static String convert(String path) throws IOException {
        byte[] data = Files.readAllBytes(Paths.get(path));
        return new String(data, Charset.defaultCharset());
    }
}

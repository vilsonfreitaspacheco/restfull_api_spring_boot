package br.com.restapi.utils;

import java.io.File;
import java.io.IOException;

import org.springframework.util.ResourceUtils;

public class FileUtils {
    public static File readDictionaryAsJson(String filename) throws IOException {
        File fileContent;
        try {
            File file = ResourceUtils.getFile("classpath:" + filename);
           // Path path = file.toPath();
            //var lines = Files.lines(path);
            //fileContent = lines.collect(Collectors.joining("\n"));
            fileContent = file;
        } catch (IOException ex) {
            throw ex;
        }
        return fileContent;
    }
}

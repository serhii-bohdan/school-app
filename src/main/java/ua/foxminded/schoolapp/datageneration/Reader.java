package ua.foxminded.schoolapp.datageneration;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;

public class Reader {

    public List<String> readFileAndPopulateList(String filePathInResources) {
        List<String> lines = new ArrayList<>();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePathInResources);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            lines = reader.lines()
                          .toList();
        } catch (IOException e) {
            System.err.println("Failed to read file " + filePathInResources);
            e.printStackTrace();
        }
        return lines;
    }

    public String readSqlScriptFrom(String filePathInResources) {
        File scriptFile = new File("src/main/resources/" + filePathInResources);
        String script = null;

        try {
            script = FileUtils.readFileToString(scriptFile, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Failed to read file " + filePathInResources);
            e.printStackTrace();
        }
        return script;
    }

}

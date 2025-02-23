package org.verizon.hffv.bdp.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class JSONUtil {
    private static final String USER_DIR = System.getProperty("user.dir");
    private static final String DIR_LOCATION = USER_DIR + "/src/main/resources/__files/";
    public JSONObject jsonObject;
    private static final ObjectMapper objectMapper = new ObjectMapper();


    public JSONObject getJSON() {
        return jsonObject;
    }

    public static JSONObject parseFile(String filename) {
        File file = new File(DIR_LOCATION + filename);
        try (InputStream inputStream = new FileInputStream(file)) {
            JSONTokener jsonTokener = new JSONTokener(inputStream);
            return new JSONObject(jsonTokener);
        } catch (IOException e) {
            throw new RuntimeException("Unable to map file to JSON.");
        }
    }

    public static String toJson(SchemaValidationTransformer.ValidationError object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    }
}

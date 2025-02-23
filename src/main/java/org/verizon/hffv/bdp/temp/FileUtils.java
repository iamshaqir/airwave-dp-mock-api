package org.verizon.hffv.bdp.temp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {

    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    public static String readFile(String path) {
        log.debug("Using file from path: {}", path);
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (Exception e) {
            log.error("Unable to read file: {}", path);
            throw new RuntimeException("Error reading file: " + path, e);
        }
    }
}

package utils;

import java.io.File;

public class FileUtils {

    private static final String TEMPLATES = "src/main/resources/templates";
    private static final String STATIC = "src/main/resources/static";

    public static  String findFilePath(String path) {
        File staticDir = new File(STATIC + path);
        File templateDir = new File(TEMPLATES + path);

        if (templateDir.exists()) {
            return TEMPLATES + path;
        } else if (staticDir.exists()) {
            return STATIC + path;
        } else {
            return null;
        }
    }
}

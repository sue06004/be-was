package utils;

import java.io.File;

public class FileUtils {

    private static final String TEMPLATES = "src/main/resources/templates";
    private static final String STATIC = "src/main/resources/static";

    public static  String findFilePath(String filePath) {
        File staticFile = new File(STATIC + filePath);
        File templateFile = new File(TEMPLATES + filePath);

        if (templateFile.exists()) {
            return TEMPLATES + filePath;
        } else if (staticFile.exists()) {
            return STATIC + filePath;
        } else {
            return null;
        }
    }

    public static boolean existFile(String filePath){
        File staticFile = new File(STATIC + filePath);
        File templateFile = new File(TEMPLATES + filePath);
        return staticFile.exists() || templateFile.exists();
    }
}

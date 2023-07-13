package util;

public class HttpRequestUtils {

    public static String getUrl(String line) {
        return line.split(" ")[1];
    }
}

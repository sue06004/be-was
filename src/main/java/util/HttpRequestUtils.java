package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequestUtils {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public static String getUrl(BufferedReader bufferedReader) throws IOException {
        String line = bufferedReader.readLine();
        return line.split(" ")[1];
    }

    //테스트 용도
    public static String getUrl(String line) {
        return line.split(" ")[1];
    }

    public static void DebugBufferedReader(BufferedReader bufferedReader) {
        try {
            String line = bufferedReader.readLine();
            logger.debug("request line: {}", line);
            while (!line.equals("")) {
                line = bufferedReader.readLine();
                logger.debug("header : {}", line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

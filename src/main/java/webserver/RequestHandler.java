package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = HttpRequest.createRequest(in);
            String url = request.getUrl();
            logRequest(request);

            HttpResponse response = HttpResponse.createResponse(out, url);
            response.send();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public static void logRequest(HttpRequest request) {
        logger.debug("request line: {} {} {}", request.getMethod(), request.getUrl(), request.getVersion());

        Map<String, String> headers = request.getHeaders();
        for (String header : headers.keySet()) {
            logger.debug("header : {}: {}", header, headers.get(header));
        }
    }

}

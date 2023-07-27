package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controller.FrontController;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.RequestHeader;
import webserver.view.View;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

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
            HttpResponse response = HttpResponse.createResponse();
            logRequest(request);

            FrontController frontController = new FrontController(); //todo: 싱글톤으로 구현하는게 어떤가
            frontController.service(request, response);

            DataOutputStream dos = new DataOutputStream(out);
            responseHeader(dos, response);
            responseBody(dos, response);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private static void responseHeader(DataOutputStream dos, HttpResponse response) throws Exception {
        dos.writeBytes(response.getResponseHead());
    }

    private static void responseBody(DataOutputStream dos, HttpResponse response) throws Exception {
        byte[] body = response.getBody();
        dos.write(body, 0, body.length);
        dos.flush();
    }

    public static void logRequest(HttpRequest request) {
        logger.debug("request line: {} {} {}", request.getMethod(), request.getUrl(), request.getVersion());

        RequestHeader headers = request.getHeaders();
        for (String header : headers.keySet()) {
            logger.debug("header : {}: {}", header, headers.get(header));
        }
    }

}

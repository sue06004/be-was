package webserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public class StaticFileController {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public String handleFileNotFound(HttpRequest request, HttpResponse response) {
        return null;
    }

    public String handleFileFound(HttpRequest request, HttpResponse response, String filePath) {
        String path = request.getPath();
        return path;
    }

}

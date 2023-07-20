package webserver.controller;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static webserver.http.HttpStateCode.NOT_FOUND;
import static webserver.http.HttpStateCode.OK;

public class BasicController {

    public void handleFileNotFound(HttpRequest request, HttpResponse response) {
        response.setStateCode(NOT_FOUND);
    }

    public void handleFileFound(HttpRequest request, HttpResponse response, String filePath) throws IOException {
        String path = request.getPath();
        byte[] body = Files.readAllBytes(new File(filePath).toPath());

        response.setBody(body);
        response.setContentType(path);
        response.setStateCode(OK);
    }
}

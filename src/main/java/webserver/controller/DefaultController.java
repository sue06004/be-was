package webserver.controller;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static utils.FindFilePath.findFilePath;
import static webserver.http.HttpStateCode.OK;

public class DefaultController implements Controller {

    @Override
    public void process(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();
        String filePath = findFilePath(path);
        byte[] body = Files.readAllBytes(new File(filePath).toPath());

        response.setBody(body);
        response.setContentType(path);
        response.setStateCode(OK);
    }


}

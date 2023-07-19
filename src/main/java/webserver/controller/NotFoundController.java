package webserver.controller;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.IOException;

import static webserver.http.HttpStateCode.NOT_FOUND;

public class NotFoundController implements Controller{

    @Override
    public void process(HttpRequest request, HttpResponse response) throws IOException {
        response.setStateCode(NOT_FOUND);
    }
}

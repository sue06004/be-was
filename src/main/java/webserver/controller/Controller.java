package webserver.controller;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.IOException;

public interface Controller {

    public void process(HttpRequest request, HttpResponse response) throws IOException;
}

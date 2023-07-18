package webserver.controller;

import webserver.http.HttpRequest;

public interface Controller {

    public String process(HttpRequest request);
}

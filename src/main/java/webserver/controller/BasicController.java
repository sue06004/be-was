package webserver.controller;

import webserver.http.HttpRequest;

public class BasicController implements Controller{

    @Override
    public String process(HttpRequest request) {
        return request.getUrl();
    }
}

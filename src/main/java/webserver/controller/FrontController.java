package webserver.controller;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;


public class FrontController {

    public void service(HttpRequest request, HttpResponse response) throws Exception {
        String path = request.getPath();
        Controller controller = ControllerMapper.getController(path);
        controller.process(request, response);
    }

}

package webserver.controller;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class FrontController {

    private static final String TEMPLATES = "src/main/resources/templates";
    private Map<String, Controller> controllerMap = new HashMap<>();

    public FrontController() {
        controllerMap.put("/user/create", new CreateUserController());
        controllerMap.put("/user/form.html", new BasicController());
        controllerMap.put("/index.html", new BasicController());
        controllerMap.put("/user/login.html", new BasicController());
        controllerMap.put("/qna/form.html", new BasicController());
    }

    public byte[] service(HttpRequest request, HttpResponse response) throws Exception {
        String url = request.getUrl();
        Controller controller = controllerMap.get(url);

        if (controller == null) {
            response.setStateCode("404 Not Found");
            return new byte[0];
        } else if (controller instanceof BasicController) {
            response.setStateCode("200 OK");
        } else {
            response.setStateCode("302 Found ");
            controller.process(request);
            response.setLocation("http://localhost:8080/index.html");
            return new byte[0];
        }
        String viewPath = TEMPLATES + controller.process(request);
        return Files.readAllBytes(new File(viewPath).toPath());
    }

}

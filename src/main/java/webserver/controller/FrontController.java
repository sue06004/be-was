package webserver.controller;

import webserver.http.HttpRequest;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class FrontController {

    private static final String TEMPLATES = "src/main/resources/templates";
    private Map<String, Controller> controllerMap = new HashMap<>();

    public FrontController() {
        controllerMap.put("/user/create", new CreateUserController());
    }

    public byte[] service(HttpRequest request) throws Exception {
        String url = request.getUrl();
        Controller controller;
        if(request.getQueryMap().isEmpty()){
            controller = new BasicController();
        } else {
            System.out.println(url);
            controller = controllerMap.get(url);
        }
        String viewPath = TEMPLATES + controller.process(request);
        System.out.println(viewPath);
        return Files.readAllBytes(new File(viewPath).toPath());
    }

}

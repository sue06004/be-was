package webserver.controller;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static webserver.http.HttpStateCode.*;


public class FrontController {

    private static final String TEMPLATES = "src/main/resources/templates";
    private Map<String, Controller> controllerMap = new HashMap<>();

    public FrontController() {
        controllerMap.put("/user/create", new SignUpController());

        controllerMap.put("/index.html", new DefaultController());
        controllerMap.put("/user/form.html", new DefaultController());
        controllerMap.put("/user/login.html", new DefaultController());
        controllerMap.put("/qna/form.html", new DefaultController());
    }

    public byte[] service(HttpRequest request, HttpResponse response) throws Exception {
        String url = request.getPath();
        Controller controller = controllerMap.get(url);

        if (controller == null) {
            response.setStateCode(NOT_FOUND); //404 Not Found
            return new byte[0];
        } else if (controller instanceof DefaultController) { //200 OK
            response.setStateCode(OK);
            String viewPath = TEMPLATES + controller.process(request);
            return Files.readAllBytes(new File(viewPath).toPath());
        } else {
            response.setStateCode(REDIRECT); // 302 Found
            String redirectPath = controller.process(request);
            response.setLocation("http://localhost:8080" + redirectPath);
            return new byte[0];
        }
    }

}

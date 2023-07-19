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

//        controllerMap.put("/index.html", new DefaultController());
//        controllerMap.put("/user/form.html", new DefaultController());
//        controllerMap.put("/user/login.html", new DefaultController());
//        controllerMap.put("/qna/form.html", new DefaultController());
    }

    public byte[] service(HttpRequest request, HttpResponse response) throws Exception {
        String path = request.getPath();
        Controller controller = controllerMap.get(path);
        response.setContentType(path);
        if (controller == null) {
            File staticDir = new File("src/main/resources/static" + path);
            File templateDir = new File("src/main/resources/templates" + path);
            if (templateDir.exists()) {
                controller = new DefaultController();
                String viewPath = TEMPLATES + controller.process(request);
                response.setStateCode(OK);
                return Files.readAllBytes(new File(viewPath).toPath());
            } else if (staticDir.exists()){
                controller = new DefaultController();
                String viewPath = "src/main/resources/static" + controller.process(request);
                response.setStateCode(OK);
                return Files.readAllBytes(new File(viewPath).toPath());
            } else{
                response.setStateCode(NOT_FOUND); //404 Not Found
                return new byte[0];
            }
        } else {
            response.setStateCode(REDIRECT); // 302 Found
            String redirectPath = controller.process(request);
            response.setLocation("http://localhost:8080" + redirectPath);
            return new byte[0];
        }
    }

}

package webserver.controller;

import annotation.RequestMapping;
import utils.FileUtils;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ControllerHandler {
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        String path = request.getPath();

        findControllerAndInvoke(request, response, path);

    }

    private void findControllerAndInvoke(HttpRequest request, HttpResponse response, String path)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, IOException {
        Class<UserController> userController = UserController.class;
        Method[] declaredMethod = userController.getDeclaredMethods();
        for (Method method : declaredMethod) {
            RequestMapping requestMappingAnnotation = method.getAnnotation(RequestMapping.class);
            String url = requestMappingAnnotation.value();
            if (url.equals(path)) {
                method.invoke(userController.newInstance(), request, response);
                return;
            }
        }
        handleBasicController(request, response, path);
    }

    private void handleBasicController(HttpRequest request, HttpResponse response, String path) throws IOException {
        String filePath = FileUtils.findFilePath(path);
        BasicController controller = new BasicController();
        if (filePath == null) {
            controller.handleFileNotFound(request, response);
            return;
        }
        controller.handleFileFound(request, response, filePath);

    }
}

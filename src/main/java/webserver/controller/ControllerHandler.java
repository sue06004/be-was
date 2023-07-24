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
            if (requestMappingAnnotation != null && checkAnnotation(request, requestMappingAnnotation)) {
                method.invoke(userController.newInstance(), request, response);
                return;
            }
        }
        handleStaticFile(request, response, path);
    }

    private boolean checkAnnotation(HttpRequest request, RequestMapping requestMapping) {
        String requestPath = request.getPath();
        String annotationUrl = requestMapping.value();
        String requestMethod = request.getMethod();
        String annotationMethod = requestMapping.method();
        return requestPath.equals(annotationUrl) && requestMethod.equals(annotationMethod);
    }

    private void handleStaticFile(HttpRequest request, HttpResponse response, String path) throws IOException {
        String filePath = FileUtils.findFilePath(path);
        StaticFileController controller = new StaticFileController();
        if (filePath == null) {
            controller.handleFileNotFound(request, response);
            return;
        }
        controller.handleFileFound(request, response, filePath);

    }
}

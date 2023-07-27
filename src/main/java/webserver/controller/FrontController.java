package webserver.controller;

import annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileUtils;
import webserver.Model;
import webserver.RequestHandler;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStateCode;
import webserver.view.View;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FrontController {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public void service(HttpRequest request, HttpResponse response) throws Exception {
        String path = request.getPath();
        Model model = new Model();

        String path2 = findControllerAndInvoke(request, response, path, model); //todo: 변수 이름 변경
        if (path2 == null) {
            response.setStateCode(HttpStateCode.NOT_FOUND);
        } else if (path2.contains("redirect:")) {
            response.setStateCode(HttpStateCode.REDIRECT);
            response.setLocation(path2.substring(path2.indexOf(":") + 1));
        } else {
            response.setStateCode(HttpStateCode.OK);
            response.setContentType(path2);
            response.setBody(View.render(model, path2));
        }
        String sessionId = (String) model.getAttribute("sid");
        if (sessionId != null) {
            response.setCookie("sid=" + sessionId + "; path=/");
        }
    }

    private String findControllerAndInvoke(HttpRequest request, HttpResponse response, String path, Model model)
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class<UserController> userController = UserController.class;
        Method[] declaredMethod = userController.getDeclaredMethods();

        for (Method method : declaredMethod) {
            RequestMapping requestMappingAnnotation = method.getAnnotation(RequestMapping.class);
            if (requestMappingAnnotation != null && checkAnnotation(request, requestMappingAnnotation)) {
                return (String) method.invoke(userController.getDeclaredConstructor().newInstance(), request, model);
                // clazz.newInstance()는 더 이상 안쓰고 위와 같이 사용한다.
                // 사용안하는 이유: http://errorprone.info/bugpattern/ClassNewInstance
                // clazz.newInstance()는 생성자가 던지는 예외를 우회할 수 있는데 위 방법을 사용하면 생성자에서 던져지는 예외를 InvocationTargetException 으로 묶어서 처리한다. (아마)
            }
        }
        return handleStaticFile(request, response, path);
    }

    private boolean checkAnnotation(HttpRequest request, RequestMapping requestMapping) {
        String requestPath = request.getPath();
        String annotationUrl = requestMapping.value();
        String requestMethod = request.getMethod();
        String annotationMethod = requestMapping.method();
        return requestPath.equals(annotationUrl) && requestMethod.equals(annotationMethod);
    }

    private String handleStaticFile(HttpRequest request, HttpResponse response, String path) {
        String filePath = FileUtils.findFilePath(path);
        StaticFileController controller = new StaticFileController();
        if (filePath == null) {
            return controller.handleFileNotFound(request, response);
        }
        return controller.handleFileFound(request, response, filePath);

    }
}

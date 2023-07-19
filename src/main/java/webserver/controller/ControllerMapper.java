package webserver.controller;

import java.util.HashMap;
import java.util.Map;

import static utils.FindFilePath.findFilePath;

public class ControllerMapper {
    private static Map<String, Controller> controllerMap = new HashMap<>();
    static {
        controllerMap.put("/user/create", new SignUpController());
    }

    public static Controller getController(String path) {
        String filePath = findFilePath(path);
        Controller controller = controllerMap.get(path);
        if (controller == null) {
            if (filePath == null) {
                return new NotFoundController();
            } else {
                return new DefaultController();
            }
        } else {
            return controllerMap.get(path);
        }
    }

}
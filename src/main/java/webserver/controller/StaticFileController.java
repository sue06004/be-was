package webserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileUtils;
import webserver.model.Model;
import webserver.RequestHandler;
import webserver.http.HttpRequest;

public class StaticFileController {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public String handleStaticFile(HttpRequest request, Model model) {
        String path = request.getPath();
        String sessionId = request.getSessionId();
        if (sessionId != null) {
            model.setModelLoginStatus(sessionId);
        }
        if (FileUtils.existFile(path)) {
            return path;
        }
        return null;
    }

}

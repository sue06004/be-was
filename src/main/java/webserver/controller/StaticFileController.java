package webserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileUtils;
import webserver.RequestHandler;

public class StaticFileController {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public String handleStaticFile(String path) {
        if (FileUtils.existFile(path)) {
            return path;
        }
        return null;
    }
}

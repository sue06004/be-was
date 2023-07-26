package webserver.view;

import utils.FileUtils;
import webserver.Model;

public class ViewResolver {

    private Model model;
    private String filePath;

    public ViewResolver(Model model, String path) {
        this.model = model;
        this.filePath = FileUtils.findFilePath(path);
    }

}

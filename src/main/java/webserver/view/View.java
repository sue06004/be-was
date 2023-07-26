package webserver.view;

import db.Database;
import db.SessionDatabase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileUtils;
import webserver.Model;
import webserver.RequestHandler;
import webserver.http.HttpResponse;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

public class View {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public static byte[] render(Model model, String path) throws IOException {

        String filePath = FileUtils.findFilePath(path);
        if(!filePath.endsWith("html")){
            return Files.readAllBytes(new File(filePath).toPath());
        }

        StringBuilder stringBuilder = new StringBuilder();
        File file = new File(filePath);
        String line;
        BufferedReader fileReader = new BufferedReader(new FileReader(file));
        while((line=fileReader.readLine()) != null){
            if(filePath.endsWith("html") && line.contains("{%")){
                logger.debug(line);
                String modelKey = line.substring(line.indexOf("{%")+2,line.indexOf("}"));
                Object modelValue = model.getAttribute(modelKey);
                if(modelValue instanceof List){
                    logger.debug("list");
                    for(Object value :(List)model.getAttribute(modelKey)){
                        logger.debug("for");
                        stringBuilder.append((String)value);
                        logger.debug("list : {}", (String)value);
                    }
                } else {
                    line = line.replace("{%"+modelKey+"}",(String)modelValue);
                    logger.debug("line = {}",line);
                    stringBuilder.append(line);
                }
            }else {
                stringBuilder.append(line);
            }
        }
        return stringBuilder.toString().getBytes();
    }

}

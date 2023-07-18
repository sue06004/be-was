package webserver.controller;

import db.Database;
import model.User;
import webserver.http.HttpRequest;

import java.util.Map;

public class CreateUserController implements Controller {

    @Override
    public String process(HttpRequest request) {

        System.out.println(1);
        Map<String, String> queryMap = request.getQueryMap();
        String userId = queryMap.get("userId");
        String password = queryMap.get("password");
        String name = queryMap.get("name");
        String email = queryMap.get("email");
        System.out.println(4);
        Database.addUser(new User(userId, password, name, email));

        return "/index.html";
    }
}

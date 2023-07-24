package webserver.controller;

import annotation.RequestMapping;
import db.Database;
import model.User;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.QueryParam;

import static webserver.http.HttpStateCode.REDIRECT;

public class UserController {


    @RequestMapping(value = "/user/create", method = "POST")
    public void signUp(HttpRequest request, HttpResponse response) {
        QueryParam queryParam = request.getQueryParam();
        String userId = queryParam.get("userId");
        String password = queryParam.get("password");
        String name = queryParam.get("name");
        String email = queryParam.get("email");
        Database.addUser(new User(userId, password, name, email));

        response.setContentType(request.getPath());
        response.setStateCode(REDIRECT);
        response.setLocation("/index.html");

    }

    @RequestMapping(value = "/user/login", method = "POST")
    public void login(HttpRequest request, HttpResponse response) {
        QueryParam queryParam = request.getQueryParam();
        String userId = queryParam.get("userId");
        String password = queryParam.get("password");

        response.setContentType(request.getPath());
        response.setStateCode(REDIRECT);
        response.setLocation("/index.html");
    }
}

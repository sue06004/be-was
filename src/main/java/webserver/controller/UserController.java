package webserver.controller;

import annotation.RequestMapping;
import db.Database;
import db.SessionDatabase;
import model.User;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.QueryParam;
import webserver.http.Session;

import static webserver.http.HttpStateCode.REDIRECT;

public class UserController {

    @RequestMapping(value = "/user/create", method = "POST")
    public void signUp(HttpRequest request, HttpResponse response) {
        QueryParam queryParam = request.getQueryParam();
        String userId = queryParam.get("userId");
        String password = queryParam.get("password");
        String name = queryParam.get("name");
        String email = queryParam.get("email");

        User findUser = Database.findUserById(userId);
        if(findUser != null){
            response.setStateCode(REDIRECT);
            response.setContentType(request.getPath());
            response.setLocation("/user/form_failed.html");
            return;
        }
        Database.addUser(new User(userId, password, name, email));
        response.setStateCode(REDIRECT);
        response.setLocation("/index.html");

    }

    @RequestMapping(value = "/user/login", method = "POST")
    public void login(HttpRequest request, HttpResponse response) {
        QueryParam queryParam = request.getQueryParam();
        String userId = queryParam.get("userId");
        String password = queryParam.get("password");

        User findUser = Database.findUserById(userId);
        if(findUser == null || !findUser.getPassword().equals(password)){ //로그인 정보가 잘못됬으면
            response.setStateCode(REDIRECT);
            response.setContentType(request.getPath());
            response.setLocation("/user/login_failed.html");
            return;
        }

        response.setContentType(request.getPath());
        response.setStateCode(REDIRECT);
        response.setLocation("/index.html");
        //쿠키 설정
        String requestCookie = request.getHeaders().get("Cookie");
        if(requestCookie == null || !requestCookie.contains("sid")){
            String sessionId = Session.createSessionId();
            SessionDatabase.add(sessionId, userId);
            response.setCookie("sid="+sessionId+"; path=/");
        }
    }
}

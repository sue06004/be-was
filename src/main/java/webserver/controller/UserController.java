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

    private static String USERID = "userId";
    private static String PASSWORD = "password";
    private static String NAME = "name";
    private static String EMAIL = "email";

    @RequestMapping(value = "/user/create", method = "POST")
    public void signUp(HttpRequest request, HttpResponse response) {
        QueryParam queryParam = request.getQueryParam();
        String userId = queryParam.get(USERID);
        String password = queryParam.get(PASSWORD);
        String name = queryParam.get(NAME);
        String email = queryParam.get(EMAIL);

        User findUser = Database.findUserById(userId);
        if (findUser != null) { //이미 존재하는 id일 경우
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
        String userId = queryParam.get(USERID);
        String password = queryParam.get(PASSWORD);

        User findUser = Database.findUserById(userId);
        if (findUser == null || !findUser.getPassword().equals(password)) { //로그인 정보가 잘못됬으면
            response.setStateCode(REDIRECT);
            response.setContentType(request.getPath());
            response.setLocation("/user/login_failed.html");
            return;
        }

        response.setContentType(request.getPath());
        setResponseCookie(request, response, userId); //쿠키 설정
        response.setStateCode(REDIRECT);
        response.setLocation("/index.html");

    }

    private void setResponseCookie(HttpRequest request, HttpResponse response, String userId) {
        String requestCookie = request.getHeaders().get("Cookie");
        if (requestCookie == null || !requestCookie.contains("sid")) {
            String sessionId = Session.createSessionId();
            SessionDatabase.add(sessionId, userId);
            response.setCookie("sid=" + sessionId + "; path=/");
        }
    }
}

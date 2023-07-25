package webserver.controller;

import annotation.RequestMapping;
import db.Database;
import db.SessionDatabase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;
import webserver.http.*;

import javax.xml.crypto.Data;
import java.io.*;
import java.nio.file.Files;
import java.util.StringTokenizer;

import static webserver.http.HttpStateCode.OK;
import static webserver.http.HttpStateCode.REDIRECT;

public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

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

    @RequestMapping(value = "/user/list")
    public void userList(HttpRequest request, HttpResponse response) throws IOException {
        String sessionId = getSessionFromHeader(request.getHeaders());
        if (sessionId == null) {
            response.setStateCode(REDIRECT);
            response.setContentType("/user/login.html");
            response.setLocation("/user/login.html");
            return;
        }
        String filePath = "src/main/resources/templates/usr/list.html";
        String userId = SessionDatabase.get(sessionId);
        User user = Database.findUserById(userId);
        StringBuilder indexBuilder = new StringBuilder();
        File indexFile = new File(filePath);
        String line;
        BufferedReader index = new BufferedReader(new FileReader(indexFile));
        while ((line = index.readLine()) != null) {
            if (line.contains("")) {

            } else if (!line.contains("{login}") && !line.contains("{signup}")) {
                indexBuilder.append(line);
            }
        }
    }

    @RequestMapping("/index.html")
    public void indexHtml(HttpRequest request, HttpResponse response) throws IOException {
        String sessionId = getSessionFromHeader(request.getHeaders());
        String filePath = "src/main/resources/templates/index.html";
        if (sessionId == null) {
            byte[] body = Files.readAllBytes(new File(filePath).toPath());
            setResponse(request, response, body);
            return;
        }

        byte[] body = createDynamicIndexBody(sessionId, filePath);
        setResponse(request, response, body);
    }

    private byte[] createDynamicIndexBody(String sessionId, String filePath) throws IOException {
        String userId = SessionDatabase.get(sessionId);
        User user = Database.findUserById(userId);
        StringBuilder indexBuilder = new StringBuilder();
        File indexFile = new File(filePath);
        String line;
        BufferedReader index = new BufferedReader(new FileReader(indexFile));
        while ((line = index.readLine()) != null) {
            if (line.contains("{userName}")) {
                indexBuilder.append("<span id=\"userName\" style=\"margin-left:50px;\">안녕하세요 ")
                        .append(user.getName())
                        .append("님</span>");
            } else if (!line.contains("{login}") && !line.contains("{signup}")) {
                indexBuilder.append(line);
            }
        }
        return indexBuilder.toString().getBytes();
    }

    private String getSessionFromHeader(RequestHeader headers) {
        String cookies = headers.get("Cookie");
        String sessionId = null;
        if (cookies != null) {
            StringTokenizer cookieToken = new StringTokenizer(cookies, "=");
            sessionId = getSessionFromCookie(cookies);
        }
        return sessionId;
    }

    private String getSessionFromCookie(String cookies) {
        StringTokenizer cookieToken = new StringTokenizer(cookies, "=");
        while (cookies != null && cookieToken.hasMoreTokens()) {
            String cookie = cookieToken.nextToken();
            if (cookie.equals("sid")) {
                return cookieToken.nextToken();
            }
        }
        return null;
    }

    private void setResponse(HttpRequest request, HttpResponse response, byte[] body) {
        response.setBody(body);
        response.setContentType(request.getPath());
        response.setStateCode(OK);
    }

}

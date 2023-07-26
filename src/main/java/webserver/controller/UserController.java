package webserver.controller;

import annotation.RequestMapping;
import db.Database;
import db.SessionDatabase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;
import webserver.http.*;

import java.io.*;
import java.nio.file.Files;
import java.util.Collection;
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
        Parameter parameter = request.getQueryParam();
        String userId = parameter.get(USERID);
        String password = parameter.get(PASSWORD);
        String name = parameter.get(NAME);
        String email = parameter.get(EMAIL);

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
        Parameter parameter = request.getQueryParam();
        String userId = parameter.get(USERID);
        String password = parameter.get(PASSWORD);

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
        String filePath = "src/main/resources/templates/user/list.html";
        StringBuilder listBuilder = new StringBuilder();
        File indexFile = new File(filePath);
        String line;
        BufferedReader list = new BufferedReader(new FileReader(indexFile));
        int num=1;
        while ((line = list.readLine()) != null) {
            if (line.contains("{tbody}")) {
                listBuilder.append("<tbody>");
                Collection<User> allUsers = Database.findAll();
                for(User user : allUsers){
                    if(user==null){
                        break;
                    }
                    listBuilder.append("<tr>");
                    listBuilder.append("<th scope=\"row\">")
                            .append(num)
                            .append("</th> <td>")
                            .append(user.getUserId())
                            .append("</td> <td>")
                            .append(user.getName())
                            .append("</td> <td>")
                            .append(user.getEmail())
                            .append("</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>");
                    listBuilder.append("</tr>");
                    num++;
                }
                listBuilder.append("</tbody>");
                while(!list.readLine().contains("</tbody>"));
            } else {
                listBuilder.append(line);
            }
        }
        byte[] body = listBuilder.toString().getBytes();
        setResponse(request, response, body);

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
            sessionId = getSessionFromCookie(cookies);
        }
        logger.debug("session = {}", sessionId);
        return sessionId;
    }

    private String getSessionFromCookie(String cookies) {
        StringTokenizer cookieToken = new StringTokenizer(cookies, "=");
        while (cookieToken.hasMoreTokens()) {
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

package webserver.controller;

import annotation.RequestMapping;
import db.Database;
import db.SessionDatabase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.model.Model;
import webserver.RequestHandler;
import webserver.http.HttpRequest;
import webserver.http.Parameter;
import webserver.http.Session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private static String USERID = "userId";
    private static String PASSWORD = "password";
    private static String NAME = "name";
    private static String EMAIL = "email";

    @RequestMapping(value = "/user/create", method = "POST")
    public String signUp(HttpRequest request, Model model) {
        Parameter parameter = request.getParam();
        String userId = parameter.get(USERID);
        String password = parameter.get(PASSWORD);
        String name = parameter.get(NAME);
        String email = parameter.get(EMAIL);

        User findUser = Database.findUserById(userId);
        if (findUser != null) { //이미 존재하는 id일 경우
            return "redirect:/user/form_failed.html";
        }

        Database.addUser(new User(userId, password, name, email));

        return "redirect:/index.html";
    }

    @RequestMapping(value = "/user/login", method = "POST")
    public String login(HttpRequest request, Model model) {
        Parameter parameter = request.getParam();
        String userId = parameter.get(USERID);
        String password = parameter.get(PASSWORD);

        User findUser = Database.findUserById(userId);
        if (findUser == null || !findUser.getPassword().equals(password)) { //로그인 정보가 잘못됬으면
            return "redirect:/user/login_failed.html";
        }
        model.setAttribute("sid", setResponseCookie(request, userId));//쿠키 설정
        model.setAttribute("sessionAge", "3600");
        model.setAttribute("logout","<li><a href=\"/user/logout\" role=\"button\">로그아웃</a></li>");
        return "redirect:/index.html";
    }

    private String setResponseCookie(HttpRequest request, String userId) {
        String requestCookie = request.getHeaders().get("Cookie");
        if (requestCookie == null || !requestCookie.contains("sid")) {
            String sessionId = Session.createSessionId();
            SessionDatabase.add(sessionId, userId);
            return sessionId;
        }
        return null;
    }

    @RequestMapping(value = "/user/list.html")
    public String userList(HttpRequest request, Model model) {
        String sessionId = request.getSessionId();
        if (sessionId == null) {
            return "redirect:/user/login.html";
        }
        List<String> userList = new ArrayList<>();
        createUserList(userList);

        model.setAttribute("userList", userList);
        model.setModelLoginStatus(sessionId);

        return "/user/list.html";
    }

    private void createUserList(List<String> userList) {
        Collection<User> allUser = Database.findAll();
        int userNum = 0;
        for (User user : allUser) {
            if (user == null) {
                break;
            }
            userNum++;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<tr>");
            stringBuilder.append("<th scope=\"row\">")
                    .append(userNum)
                    .append("</th> <td>")
                    .append(user.getUserId())
                    .append("</td> <td>")
                    .append(user.getName())
                    .append("</td> <td>")
                    .append(user.getEmail())
                    .append("</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>");
            stringBuilder.append("</tr>");
            userList.add(stringBuilder.toString());
        }
    }

    @RequestMapping("/index.html")
    public String indexHtml(HttpRequest request, Model model) {
        String sessionId = request.getSessionId();
        if (sessionId != null) {
            model.setModelLoginStatus(sessionId);
        }
        return "/index.html";
    }

    @RequestMapping("/user/logout")
    public String logOut(HttpRequest request, Model model){
        String sessionId = request.getSessionId();
        if(sessionId !=null){
            model.setAttribute("sid",sessionId);
            model.setAttribute("sessionAge","0");
            model.setAttribute("logout","");
        }
        return "redirect:/index.html";
    }
}

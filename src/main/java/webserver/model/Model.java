package webserver.model;

import db.Database;
import db.SessionDatabase;
import model.User;

import java.util.HashMap;
import java.util.Map;

public class Model {

    private Map<Object, Object> model = new HashMap<>();

    public Model(){
        model.put("signup", "<li ><a href=\"/user/form.html\" role=\"button\">회원가입</a></li>");
        model.put("login", "<li><a href=\"/user/login.html\" role=\"button\">로그인</a></li>");
        model.put("userName", "");
        model.put("logout", "");
    }

    public void setAttribute(Object key, Object value){
        model.put(key, value);
    }

    public Object getAttribute(Object key){
        return model.get(key);
    }

    public void setModelLoginStatus(String sessionId){ //로그인 상태일 때 항상 model에 적용되야 하는 값
        String userId = SessionDatabase.get(sessionId);
        User user = Database.findUserById(userId);
        this.setAttribute("userName", user.getName() + "님 안녕하세요");
        this.setAttribute("login", "");
        this.setAttribute("signup", "");
        this.setAttribute("logout", "<li><a href=\"/user/logout\" role=\"button\">로그아웃</a></li>\n");
    }
}

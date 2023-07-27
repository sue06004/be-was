package webserver;

import java.util.HashMap;
import java.util.Map;

public class Model {

    private Map<Object, Object> model = new HashMap<>();

    public Model(){
        model.put("signup", "<li ><a href=\"user/form.html\" role=\"button\">회원가입</a></li>");
        model.put("login", "<li><a href=\"user/login.html\" role=\"button\">로그인</a></li>");
        model.put("userName", "");
        model.put("logout", "");
    }

    public void setAttribute(Object key, Object value){
        model.put(key, value);
    }

    public Object getAttribute(Object key){
        return model.get(key);
    }
}

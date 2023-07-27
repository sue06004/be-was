import org.junit.jupiter.api.Test;
import webserver.model.Model;
import webserver.view.View;

import java.io.IOException;
import java.util.*;

public class test {

    @Test
    void test() {
        List<String> list = new ArrayList();

        list.add("string");
        list.add("str");

        Map<Object, Object> map = new HashMap<>();
        map.put("sss",list);
        Object obj = map.get("sss");
        for(Object ss : (List)obj){
            System.out.println((String)ss);
        }
    }

    @Test
    void render() throws IOException {
        Model model = new Model();
        model.setAttribute("sid","1lfjefjeffl");
        model.setAttribute("userName","김우혁");
        List<String> list = new ArrayList<>();
        list.add("<spna> afljlfe</span>");
        list.add("lafjejfleffa");
        model.setAttribute("list",list);
        byte[] body = View.render(model,"/index.html");
        System.out.println(body.toString());
    }

    @Test
    void test1(){
        String str = "{%sfdsf}";
        System.out.println(str.indexOf("{%"));
    }


}

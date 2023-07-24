import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class test {

    @Test
    void test() {
        Map<String, String> map = new HashMap<>();
        map.put("str1", "1");
        map.put("str2", "2");
        String st = map.get("str3");
        System.out.println(st);
    }


}

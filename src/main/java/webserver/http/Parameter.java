package webserver.http;

import java.util.HashMap;
import java.util.Map;

public class Parameter {

    private Map<String, String> paramMap = new HashMap<>();

    public void put(String key, String value) {
        paramMap.put(key, value);
    }

    public String get(String key) {
        return paramMap.get(key);
    }

}

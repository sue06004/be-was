package webserver.http;

import java.util.HashMap;
import java.util.Map;

public class QueryParam {

    private Map<String, String> queryMap = new HashMap<>();

    public void put(String key, String value) {
        queryMap.put(key, value);
    }

    public String get(String key) {
        return queryMap.get(key);
    }

}

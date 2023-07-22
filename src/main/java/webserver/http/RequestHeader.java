package webserver.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RequestHeader {

    private final Map<String, String> headers = new HashMap<>();

    public void put(String key, String value){
        headers.put(key, value);
    }

    public String get(String key){
        return headers.get(key);
    }

    public Set<String> keySet(){
        return headers.keySet();
    }

    public int size(){
        return headers.size();
    }
}

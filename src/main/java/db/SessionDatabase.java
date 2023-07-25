package db;

import com.google.common.collect.Maps;

import java.util.Map;

public class SessionDatabase {

    private static Map<String, String>  sessionMap = Maps.newHashMap();

    private SessionDatabase(){

    }

    public static void add(String sessionId, String userId){
        sessionMap.put(sessionId, userId);
    }

    public static String get(String sessionId){
        return sessionMap.get(sessionId);
    }
}

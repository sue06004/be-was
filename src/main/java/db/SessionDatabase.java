package db;

import com.google.common.collect.Maps;

import java.util.Map;

public class SessionDatabase {

    //todo: guava를 사용하면 뭐가 더 좋은지 공부
    private static Map<String, String> sessionMap = Maps.newConcurrentMap();
    private SessionDatabase(){

    }

    public static void add(String sessionId, String userId){
        sessionMap.put(sessionId, userId);
    }

    public static String get(String sessionId){
        return sessionMap.get(sessionId);
    }

    public static void remove(String sessionId){
        sessionMap.remove(sessionId);
    }
}

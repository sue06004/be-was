package webserver.http;

import org.junit.jupiter.api.Test;

import java.util.StringTokenizer;

public class test {

    @Test
    void test1(){
        String path = "/user/index.html";
        String path2 = "/user/path";

        StringTokenizer st = new StringTokenizer(path2, ".");
        System.out.println(st.nextToken());

        int length = path2.split("\\.").length;
        System.out.println(length);

    }
}

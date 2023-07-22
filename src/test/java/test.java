import org.junit.jupiter.api.Test;

public class test {

    @Test
    void test(){
        String a = "abcde=sfs";
        String[] b = a.split("&");
        System.out.println(b[0]);
    }
}

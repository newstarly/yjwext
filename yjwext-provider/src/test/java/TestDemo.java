import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 随机分配考官-->教室(考官和教室个数相同)
 */
public class TestDemo {
    private final static int EXAMINERTIMES = 50;
    private final static int ROOMTIMES = 50;

    public static void main(String args[]) {
        String slideNumber = "881459-A1";
        if (slideNumber != null && slideNumber.contains("-")) {
            String charDit = slideNumber.split("-")[1];
            String sub = charDit.substring(1);
            Integer value1 = Integer.parseInt(sub);
        }
    }
}
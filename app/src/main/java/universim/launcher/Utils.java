package universim.launcher;

import java.util.concurrent.TimeUnit;

public class Utils {
    public static void sleep(int secTimeOut) {
        try {
            TimeUnit.SECONDS.sleep(secTimeOut);
        } catch (Exception e) {
        }
    }
}

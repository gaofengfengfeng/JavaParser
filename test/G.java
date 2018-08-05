import com.java.parse.test.A;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @Author: gaofeng
 * @Date: 2018-07-24
 * @Description:
 */
public class G {
    public String a = "test";
    public String[] strings = {"a", "b", "c"};
    public Integer i = null;
    public Integer j;
    public int d = 10;
    private A b;

    public void c() {
        for (int i = 0; i < 10; i++) {
            d--;
        }
        for (int i = 0, j = 0; i < 10 && j < 2; i++, j++) {
            d++;
        }
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                d--;
                d = d + 1;
            }
        }
        while (d < 10) {
            d++;
            continue;
        }
        for (String str : strings) {
            System.out.println(str);
        }
        switch (d) {
            case 0:
                System.out.println(strings[0]);
                break;
            case 1:
                System.out.println(strings[1]);
                break;
            default:
                System.out.println(strings);
                break;
        }
        try {
            File file = new File(a);
            FileWriter fileWriter = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (d < 0) {
            if (strings.length > 10) {
                System.out.println(strings);
            }
        } else if (d == 0) {
            d++;
        } else {
            d--;
        }

    }
}

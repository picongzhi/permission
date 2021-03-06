package com.pcz.permission.util;

import java.util.Date;
import java.util.Random;

/**
 * @author picongzhi
 */
public class PasswordUtil {
    public final static String[] words = {
            "a", "b", "c", "d", "e", "f", "g",
            "h", "j", "k", "m", "n",
            "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G",
            "H", "J", "K", "M", "N",
            "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"
    };

    public final static String[] numbers = {
            "2", "3", "4", "5", "6", "7", "8", "9"
    };

    public static String randomPassword() {
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random(System.currentTimeMillis());

        boolean flag = false;
        int length = random.nextInt(3) + 8;
        for (int i = 0; i < length; i++) {
            stringBuffer.append(flag ?
                    numbers[random.nextInt(numbers.length)] :
                    words[random.nextInt(words.length)]);
            flag = !flag;
        }

        return stringBuffer.toString();
    }

    public static void main(String[] args) {
        System.out.println(randomPassword());
        System.out.println(randomPassword());
        System.out.println(randomPassword());
    }
}

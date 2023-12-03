package lab13.qifan.group2.a2.utilities;

import java.util.Random;

public class IdGenerator {

    public static String generateId() {
        long currentTimeMillis = System.currentTimeMillis();
        int randomInt = new Random().nextInt(1000); // Random number between 0 and 999
        return "ID" + currentTimeMillis + "-" + randomInt;
    }
}
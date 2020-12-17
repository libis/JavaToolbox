package be.libis.toolbox;

import org.junit.Test;

public class RandomStringTest {
    @Test
    public void createRandomString() {
        RandomString randomString = new RandomString(10);
        System.out.println(randomString.nextString());
    }
}

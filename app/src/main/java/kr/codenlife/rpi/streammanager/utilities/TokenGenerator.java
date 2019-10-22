package kr.codenlife.rpi.streammanager.utilities;

public class TokenGenerator {

    public static String generate(int len) {
        String s = "abcdefghijkpmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; ++i) {
            int index = (int) (s.length() * Math.random());
            sb.append(s.charAt(index));
        }

        return sb.toString();
    }
}

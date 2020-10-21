package utils;

import java.util.regex.Pattern;

public class UrlUtil {
    static Pattern pattern = Pattern.compile("(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");

    public static boolean isValidUrl(String url) {
        if (url == null) return false;
        return pattern.matcher(url).matches();
    }

    public static void main(String[] args) {
        String url1 = "http://hao123.com";
        String url2 = "www.hao123.com";
        System.out.println(pattern.matcher(url1).matches());
        System.out.println(pattern.matcher(url2).matches());
    }
}

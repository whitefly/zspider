package response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import us.codecraft.xsoup.XElements;
import us.codecraft.xsoup.Xsoup;

import java.io.*;

public class Body {

    private final InputStream inputStream; //通过下载器生成,需要时初始化
    private String charset;  //网页字符码
    private String bodyString;  //存放完整的网页内容

    public Body(InputStream inputStream, String charset) {
        this.inputStream = inputStream;
        this.charset = charset;
    }

    public String text() {
        if (bodyString != null) return bodyString;
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charset));) {
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            bodyString = sb.toString();
        } catch (IOException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
        }
        return bodyString;
    }

    public Body setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public Elements css(String css) {
        return Jsoup.parse(this.text()).select(css);
    }

    public XElements xpath(String xpath) {
        return Xsoup.compile(xpath).evaluate(Jsoup.parse(this.text()));
    }

    public static void main(String[] args) throws FileNotFoundException {
        String htmlName = "/Users/zhouang/IdeaProjects/Zspider/src/main/resources/testFile/douban.html";
        InputStream fileInputStream = new FileInputStream(htmlName);

        Body body = new Body(fileInputStream, "UTF-8");

        //测试css规则
        Elements css = body.css("#celebrities > ul > li > div > span.name > a");
        for (Element item : css) {
            System.out.println(item.toString());
        }
        System.out.println();

        //测试xpath规则
        XElements xpath = body.xpath("//*[@id=\"celebrities\"]/ul/li/div/span[1]/a");
        for (Element item : xpath.getElements()) {
            System.out.println(item.toString());
        }

    }
}

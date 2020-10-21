package request;

import lombok.Getter;
import spider.Spider;

public class Request<T> {
    //request需要存放一个url需要的全部信息  解析器+spider+url

    @Getter
    String url;

    @Getter
    String method = "Get";

    @Getter
    String charset = "UTF-8"; //编码格式

    @Getter
    Parser<T> parser;

    @Getter
    Spider spider;

    public Request(String url, Spider spider, Parser<T> parser) {
        this.url = url;
        this.spider = spider;
        this.parser = parser;
    }
}

package simples;

import engine.SpiderApp;
import lombok.extern.slf4j.Slf4j;
import response.Response;
import response.Result;
import spider.Spider;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class TestSpider extends Spider {


    public TestSpider(String name) {
        super(name);
    }

    @Override
    public void onStart() {
        this.addPipeLines((item, request) -> System.out.println(item));
        setStartUrls("https://fuliba2020.net/");
    }

    @Override
    public Result parse(Response response) {
        Result result = new Result();

        //获取详情页url,传入回调函数
        List<String> items = response.getBody().css("body > section > div.content-wrap > div > article.excerpt > header > h2 > a").stream().map(element -> element.attr("href")).collect(Collectors.toList());
        for (String detailUrl : items) {
            result.addRequest(detailUrl, this::parseDetail);
        }

        //获取下一页的列表(不传入parse的话,默认使用parse解析)
        List<String> nextPage = response.getBody().css("body > section > div.content-wrap > div > div.pagination > ul > li > a ").stream().map(element -> element.attr("href")).collect(Collectors.toList());
        result.setUrls(nextPage);
        return result;
    }

    public Result parseDetail(Response response) {
        String url = response.getBody().css("body > section > div.content-wrap > div > header > h1 > a").get(0).text();
        String context = response.getBody().css("body > section > div.content-wrap > div > article").get(0).html();

        PageItem pageItem = new PageItem(url, context);
        Result result = new Result(pageItem);
        return result;
    }

    public static void main(String[] args) throws InterruptedException {
        //启动
        TestSpider testSpider = new TestSpider("test-spider");
        new SpiderApp(testSpider).start();
    }
}

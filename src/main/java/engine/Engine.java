package engine;

import download.Downloader;
import lombok.extern.slf4j.Slf4j;
import pipeline.PipeLine;
import request.Parser;
import request.Request;
import response.Response;
import response.Result;
import scheduler.Scheduler;
import spider.Spider;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
public class Engine {
    //整个爬虫的引擎基本实现思如,控制数据流.
    //①启动存在的全部spider
    //②从startUrl生成初始request
    //③定时取出Response,回调其中的parse方法


    List<Spider> spiders;
    Scheduler scheduler;
    ExecutorService executors;
    boolean isRunning = true;

    public Engine(SpiderApp app) {
        this.spiders = app.spiders;
        this.scheduler = new Scheduler();
        this.executors = Executors.newFixedThreadPool(5);
    }

    public void start() throws InterruptedException {
        spiders.forEach(spider -> {
            // //将每个爬虫任务startUrl转化为request;
            List<String> startUrls = spider.getStartUrls();
            List<Request> initRequests = startUrls.stream().map(spider::urlToRequest).collect(Collectors.toList());
            scheduler.addAllRequest(initRequests);

            log.info(spider.getName() + ": 初始url已经加入调度队列");
        });


        //开一个守护线程,用来消息request,生成respose
        Thread downloadThread = new Thread(() -> {
            while (isRunning) {
                //这里只负责提交任务,不应该是阻塞的,除了下载,还应该提交给scheduler,这是一个任务
                Request request = scheduler.nextRequest();
                executors.submit(new Downloader(request, scheduler));
            }
        });
        downloadThread.setDaemon(true);
        downloadThread.start();
        downloadThread.setName("download-thread");

        //处理response
        processResponse();
    }

    private void processResponse() throws InterruptedException {
        while (isRunning) {
            Response response = scheduler.nextResponse();

            try {
                Parser parser = response.getRequest().getParser();
                List<PipeLine> pipeLines = response.getRequest().getSpider().getPipeLines();
                Spider spider = response.getRequest().getSpider();
                if (parser != null) {
                    //调用默认Parser处理
                    Result result = parser.parse(response);

                    //新urls,默认parse
                    List<String> extraUrls = result.getUrls();
                    if (extraUrls != null && extraUrls.size() != 0) {
                        List<Request> initRequests = extraUrls.stream().map(spider::urlToRequest).collect(Collectors.toList());
                        scheduler.addAllRequest(initRequests);
                    }

                    //新url,自定义parse
                    Map<String, Parser> urlMap = result.getNewUrlsMap();
                    for (Map.Entry<String, Parser> kv : urlMap.entrySet()) {
                        Request request = spider.urlToRequest(kv.getKey(), kv.getValue());
                        scheduler.addRequest(request);
                    }

                    log.info("extract success:" + response.getRequest().getUrl());
                    //item走管道处理
                    if (result.getItem() != null) {
                        Object item = result.getItem();
                        pipeLines.forEach(x -> x.process(item, response.getRequest()));
                    }
                }
            } catch (Exception e) {
                log.error("解析出错", e);
            }
        }
    }
}

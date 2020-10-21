package engine;

import engine.Engine;
import spider.Spider;

import java.util.ArrayList;
import java.util.List;

public class SpiderApp {
    List<Spider> spiders = new ArrayList<Spider>();

    public SpiderApp(Spider spider) {
        spiders.add(spider);
    }


    public void start() throws InterruptedException {
        new Engine(this).start();
    }

}

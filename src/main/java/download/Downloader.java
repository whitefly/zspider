package download;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import request.Request;
import response.Response;
import scheduler.Scheduler;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class Downloader implements Runnable {
    //封装一个httpclient,专门处理下载任务

    Scheduler scheduler;
    Request request;

    static CloseableHttpClient httpclient = HttpClients.createDefault();

    public Downloader(Request request, Scheduler scheduler) {
        this.scheduler = scheduler;
        this.request = request;
    }

    @Override
    public void run() {
        String url = request.getUrl();
        if (request.getMethod().equals("Get")) {
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = null;
            try {
                response = httpclient.execute(httpGet);
                if (response.getStatusLine().getStatusCode() == 200) {
                    //流在用户parse后,会自动关闭
                    //todo 用户可能在parse时,并没有调用xpath或者选择器,导致流没有正常关闭
                    InputStream inputStream = response.getEntity().getContent();

                    //放入Response队列中
                    Response resp = new Response(inputStream, request);
                    log.info("download success:" + url);
                    scheduler.addResponse(resp);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

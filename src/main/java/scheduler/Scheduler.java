package scheduler;

import lombok.extern.slf4j.Slf4j;
import request.Request;
import response.Response;
import utils.UrlUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class Scheduler {

    //用来存放需要下载的request 和 已经下载好的 Response

    private final BlockingQueue<Request> requests = new LinkedBlockingQueue<Request>();

    private final BlockingQueue<Response> responses = new LinkedBlockingQueue<Response>();

    private final Set<String> finished = new HashSet<>();


    public Response nextResponse() {
        try {
            return responses.take();
        } catch (InterruptedException e) {
            log.error("Scheduler取出Response失败", e);
            return null;
        }
    }

    public Request nextRequest() {
        try {
            return requests.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addRequest(Request request) {
        if (isRepeat(request)) return;
        if (!UrlUtil.isValidUrl(request.getUrl())) {
            log.warn("url地址不合法,加入任务队列失败:{}", request.getUrl());
            return;
        }
        requests.add(request);
        finished.add(request.getUrl());
    }

    public void addResponse(Response response) {
        responses.add(response);
    }

    public void addAllRequest(List<Request> requests) {
        for (Request request : requests) {
            addRequest(request);
        }
    }

    public void addAllResponse(List<Response> responses) {
        responses.addAll(responses);
    }

    private boolean isRepeat(Request request) {
        //判定是否已经下载
        String url = request.getUrl();
        return finished.contains(url);
    }
}

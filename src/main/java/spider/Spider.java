package spider;

import lombok.Getter;
import pipeline.PipeLine;
import request.Parser;
import request.Request;
import response.Response;
import response.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Spider {

    @Getter
    List<String> startUrls;

    @Getter
    String name;

    @Getter
    List<PipeLine> pipeLines;

    public Spider(String name) {
        this.name = name;
        pipeLines = new ArrayList<>();
        startUrls = new ArrayList<>();
        onStart();
    }

    public abstract void onStart();

    public void addPipeLines(PipeLine pipeLine) {
        pipeLines.add(pipeLine);
    }

    public void setStartUrls(String... urls) {
        if (urls != null && urls.length != 0) {
            startUrls.addAll(Arrays.asList(urls));
        }
    }

    public abstract Result parse(Response response);


    public Request urlToRequest(String url) {
        return new Request(url, this, this::parse);
    }

    public Request urlToRequest(String url, Parser parser) {
        return new Request(url, this, parser);
    }
}

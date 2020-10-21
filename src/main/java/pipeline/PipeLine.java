package pipeline;

import request.Request;

public interface PipeLine<T> {
    //传入item,进行处理(比如持久化任务)

    void process(T item, Request request);

}

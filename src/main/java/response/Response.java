package response;

import lombok.Data;
import lombok.Getter;
import request.Request;

import java.io.InputStream;

@Data
public class Response {
    //存放body 和 request, body负责解析

    @Getter
    Body body;

    @Getter
    Request request;

    public Response(InputStream inputStream, Request request) {
        this.body = new Body(inputStream, request.getCharset());
        this.request = request;
    }
}

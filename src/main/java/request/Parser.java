package request;

import response.Response;
import response.Result;

public interface Parser<T> {

    Result<T> parse(Response response);
}

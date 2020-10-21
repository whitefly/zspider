package response;

import lombok.Data;
import request.Parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Result<T> {
    //用来存放parse后的结果,item+新的连接
    //为了解析2级页面,需要新加一个变量,绑定url+解析函数


    public Result(T item) {
        Item = item;
    }

    public Result() {

    }

    public void addRequest(String url, Parser parser) {
        newUrlsMap.put(url, parser);
    }

    T Item;
    List<String> urls;
    Map<String, Parser> newUrlsMap = new HashMap<>();
}

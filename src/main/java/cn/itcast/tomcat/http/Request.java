package cn.itcast.tomcat.http;


import cn.itcast.tomcat.util.ParamUtil;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;

import java.util.Map;

/**
 * @description: 自定义的request，封装FullHttpRequest，提取数据
 * @author: fengx
 * @create: 2021-05-29 17:00
 */
public class Request {

    private Map<String, Object> param;
    private HttpMethod httpMethod;
    private String url;


    public Request(FullHttpRequest request) {
        httpMethod = request.method();
        if ( httpMethod == HttpMethod.GET) {
            param = ParamUtil.getGetParamsFromChannel(request);
        }else if (httpMethod == HttpMethod.POST) {
            param = ParamUtil.getPostParamsFromChannel(request);
        }
        url = handleUri(request.uri());
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 处理get请求中/xxxx?xxx=xxx
     * @param uri
     * @return /xxxx
     */
    private String handleUri(String uri) {

        if (uri.contains("?")) {
            return uri.substring(0, uri.indexOf("?"));
        }

        return uri;
    }

}

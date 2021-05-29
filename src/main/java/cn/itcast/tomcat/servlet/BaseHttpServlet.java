package cn.itcast.tomcat.servlet;

import cn.itcast.tomcat.http.Request;
import cn.itcast.tomcat.http.Response;
import io.netty.handler.codec.http.HttpMethod;


/**
 * @description: BaseHttpServlet 分发请求
 * @author: fengx
 * @create: 2021-05-29 15:44
 */
public abstract class BaseHttpServlet {


    public Response service(Request request) {
        if (request.getHttpMethod() == HttpMethod.GET) {
            return doGet(request);
        }else if (request.getHttpMethod() == HttpMethod.POST) {
            return doPost(request);
        }
        return null;
    }


    public abstract Response doPost(Request request);
    public abstract Response doGet(Request request);


}

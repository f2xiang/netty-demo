package cn.itcast.tomcat.servlet;

import cn.itcast.tomcat.http.Request;

/**
 * @description: 测试
 * @author: fengx
 * @create: 2021-05-29 15:44
 */
public class HelloServlet extends BaseHttpServlet {


    @Override
    public String doPost(Request request) {

        return "[post] hello!!"+request.getParam().get("name");
    }

    @Override
    public String doGet(Request request) {
        return "[get] hello!!"+request.getParam().get("name");
    }
}

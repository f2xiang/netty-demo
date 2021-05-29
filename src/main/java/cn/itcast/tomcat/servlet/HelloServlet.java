package cn.itcast.tomcat.servlet;

import cn.itcast.entity.User;
import cn.itcast.tomcat.http.Request;
import cn.itcast.tomcat.http.Response;
import net.sf.json.JSONObject;

/**
 * @description: 测试
 * @author: fengx
 * @create: 2021-05-29 15:44
 */
public class HelloServlet extends BaseHttpServlet {


    @Override
    public Response doPost(Request request) {
        User user = new User();
        user.setName((String) request.getParam().get("name"));
        return new Response<>(JSONObject.fromObject(user).toString());
    }

    @Override
    public Response doGet(Request request) {
        return new Response<>("[get] hello!!"+request.getParam().get("name"));    }
}

package cn.itcast.travel.web.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //完成方法分发
        //1.获取请求路径
        String uri = req.getRequestURI();// /travel/user/add
        //2.获取方法名称
        String methodName = uri.substring(uri.lastIndexOf("/") + 1);// add
        //3.获取方法对象Method,因为是UserServlet调用的BaseServlet，因此这里的this是UserServlet
        try {
            //这里使用getDeclaredMethod而不是getMethod的原因是，获取的方法的修饰符全部是protected的，忽略访问权限修饰符，但是该方法不是特好
            //Method method = this.getClass().getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            //4.执行方法
            //method.setAccessible(true);//暴力反射，在使用getDeclaredMethod时候用
            method.invoke(this, req, resp);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 直接将传入的对象序列化为json,并传递给客户端
     * @param obj
     * @param response
     * @throws IOException
     */
    public void writeValue(Object obj,HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getWriter(),obj);
    }

    /**
     * 将传递的对象序列胡为json，返回给使用者
     * @param obj
     * @return
     * @throws JsonProcessingException
     */
    public String writeValueAsString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }
}

package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    public void registerUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //验证码校验功能
        String check = request.getParameter("check");
        //从session中获取验证码
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute(checkcode_server);//以免后退时验证码不更新
        if (checkCode(response, check, checkcode_server)) return;

        //1.获取注册数据
        Map<String, String[]> map = request.getParameterMap();
        //2.封装对象
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        //3.调用业务接口完成注册
        UserService userService = new UserServiceImpl();
        boolean flag = userService.register(user);
        //4.响应结果
        ResultInfo resultInfo = new ResultInfo();
        if (flag) {
            //注册成功
            resultInfo.setFlag(true);
        } else {
            //注册失败
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("注册失败");
        }
        //5.将消息对象序列化为json
        String json = writeValueAsString(resultInfo);
        //6.将json数据写回客户端
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }

    private boolean checkCode(HttpServletResponse response, String check, String checkcode_server) throws IOException {
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)) {
            //验证码错误，提示相关信息
            ResultInfo resultInfo = new ResultInfo();
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("验证码错误");
            //将信息对象序列化为json
            String json = writeValueAsString(resultInfo);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return true;
        }
        return false;
    }

    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //验证码校验功能
        String check = request.getParameter("check");
        //从session中获取验证码
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute(checkcode_server);//以免后退时验证码不更新
        if (checkCode(response, check, checkcode_server)) return;

        //1.获取用户名和密码
        Map<String, String[]> map = request.getParameterMap();
        //2.封装User对象
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        //3.调用service查询
        UserService userService = new UserServiceImpl();
        User u = userService.login(user);
        //4.判断用户是否为null
        ResultInfo resultInfo = new ResultInfo();
        if (u == null) {
            //用户名密码错误
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("用户名或密码错误");
        }
        //5.判断用户是否激活
        if (u != null && !"Y".equals(u.getStatus())) {
            //用户尚未激活
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("用户尚未激活，请激活");
        }
        //6.登陆成功判断
        if (u != null && "Y".equals(u.getStatus())) {
            //登录成功标记,存入session
            request.getSession().setAttribute("user", u);
            //登陆成功
            resultInfo.setFlag(true);
        }
        //7.响应数据
        writeValue(resultInfo, response);

    }

    public void findUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.从session中获取登陆用户
        Object user = request.getSession().getAttribute("user");
        //2.将user写回客户端
        writeValue(user, response);
    }

    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.销毁session,目的是为了删除里面的user
        request.getSession().invalidate();
        //2.跳转到登陆页面，重定向
        response.sendRedirect(request.getContextPath() + "/login.html");
    }

    public void activeUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取激活码
        String code = request.getParameter("code");
        if (code != null) {
            //2.调用service方法完成激活
            UserService userService = new UserServiceImpl();
            boolean flag = userService.active(code);
            //3.判断标记
            String msg = null;
            if (flag) {
                //激活成功
                msg = "激活成功，请<a href='login.html'>登陆</a>";
            } else {
                //激活失败
                msg = "激活失败，请联系管理员";
            }
            //4.将消息写回客户端
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }
    }
}

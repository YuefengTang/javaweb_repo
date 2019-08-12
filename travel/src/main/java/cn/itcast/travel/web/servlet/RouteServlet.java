package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteSevice;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {

    private RouteService routeService = new RouteServiceImpl();
    private FavoriteSevice favoriteSevice = new FavoriteServiceImpl();

    /**
     * 分页查询
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.接收参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");
        String rname = request.getParameter("rname");
        if (rname != null && rname.length() > 0 && !"null".equals(rname)) {
            rname = new String(rname.getBytes("iso-8859-1"), "utf-8");//防止tomcat7搜索框rname查询乱码
        } else {
            rname = "";
        }

        //2.处理参数
        int cid = 0;
        //此处"null".equals(cidStr)判断可以防止在首页cidStr为null时，执行else
        if (cidStr != null && cidStr.length() > 0 && !"null".equals(cidStr)) {
            cid = Integer.parseInt(cidStr);
        }
        int currentPage;
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        } else {
            currentPage = 1;
        }
        int pageSize;
        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        } else {
            pageSize = 5;
        }

        //3.调用service方法查询PageBean对象
        PageBean<Route> routePageBean = routeService.pageQuery(cid, currentPage, pageSize, rname);
        //4.将对象序列化为json,返回给客户端
        writeValue(routePageBean, response);
    }

    /**
     * 根据id查询旅游线路的详情
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.获取id
        String rid = request.getParameter("rid");
        //2.调用service方法查询route对象
        Route route = routeService.findOne(rid);
        //3.转为json写回客户端
        writeValue(route, response);
    }

    /**
     * 判断当前登陆用户时候收藏过该线路
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取id
        String rid = request.getParameter("rid");
        //2.获取当前登陆的用户
        int uid;//用户id
        User user = (User) request.getSession().getAttribute("user");
        if (user ==null){
            //用户尚未登陆
            return;
        }else {
            //用户已经登陆
            uid = user.getUid();
        }
        //3.调用servlet,查询收藏
        boolean b = favoriteSevice.isFavorite(uid, rid);
        //4.写回客户端
        writeValue(b,response);
    }

    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.获取id
        String rid = request.getParameter("rid");
        //2.获取当前登陆的用户
        int uid;//用户id
        User user = (User) request.getSession().getAttribute("user");
        if (user ==null){
            //用户尚未登陆
            return;
        }else {
            //用户已经登陆
            uid = user.getUid();
        }
        //3.调用service方法查询route对象
        favoriteSevice.add(uid,rid);
    }
}

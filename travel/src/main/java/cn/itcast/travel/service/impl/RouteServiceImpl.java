package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImageDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImageDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    private RouteDao routeDao = new RouteDaoImpl();
    private RouteImageDao routeImageDao = new RouteImageDaoImpl();
    private SellerDao sellerDao = new SellerDaoImpl();
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname) {
        //封装PageBean
        PageBean<Route> pageBean = new PageBean<>();
        //设置当前页和每页显示的记录数
        pageBean.setCurrentPage(currentPage);
        pageBean.setPageSize(pageSize);
        //设置总记录数
        int totalCount = routeDao.findTotalCount(cid, rname);
        pageBean.setTotalCount(totalCount);
        //设置开始记录数
        int start = (currentPage - 1) * pageSize;
        //设置当前页显示的数据集合
        List<Route> list = routeDao.findByPage(cid, start, pageSize, rname);
        pageBean.setList(list);
        //设置总页数
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
        pageBean.setTotalPage(totalPage);
        return pageBean;
    }

    @Override
    public Route findOne(String rid) {
        //根据rid去tab_route表中查询Route对象
        Route route = routeDao.findOne(Integer.parseInt(rid));
        //根据rid去tab_route_img表中查询图片集合信息
        List<RouteImg> routeImgList = routeImageDao.findByRid(Integer.parseInt(rid));
        //将图片集合存储到route对象中
        route.setRouteImgList(routeImgList);
        //根据route的sid去tab_seller表中查询Seller对象
        Seller sid = sellerDao.findById(route.getSid());
        route.setSeller(sid);
        //查询收藏次数
        int count = favoriteDao.findCountByRid(route.getRid());
        route.setCount(count);
        return route;
    }

}

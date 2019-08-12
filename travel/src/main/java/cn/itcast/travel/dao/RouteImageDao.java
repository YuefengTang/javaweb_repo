package cn.itcast.travel.dao;

import cn.itcast.travel.domain.RouteImg;

import java.util.List;

public interface RouteImageDao {

    /**
     * 根据rid传递线路图片
     * @param rid
     * @return
     */
    List<RouteImg> findByRid(int rid);
}

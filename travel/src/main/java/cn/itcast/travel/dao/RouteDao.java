package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Route;

import java.util.List;

public interface RouteDao {

    /**
     * 查询路线的总记录数
     * @param cid
     * @param rname
     * @return
     */
    int findTotalCount(int cid, String rname);


    /**
     * 分页查询
     * @param cid
     * @param start
     * @param pageSize
     * @param rname
     * @return
     */
    List<Route> findByPage(int cid, int start, int pageSize, String rname);

    /**
     * 根据id查询线路信息
     * @param id
     * @return
     */
    Route findOne(int id);
}

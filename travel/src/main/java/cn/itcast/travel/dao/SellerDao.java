package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Seller;

public interface SellerDao {

    /**
     * 根据商家的sid查询商家对象的信息
     * @param sid
     * @return
     */
    Seller findById(int sid);
}

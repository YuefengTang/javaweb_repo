package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;

public interface FavoriteDao {


    /**
     * 根据用户和线路id查询收藏信息
     * @param uid
     * @param rid
     * @return
     */
    Favorite findByUidAndRid(int uid, int rid);

    /**
     * 根据线路id查找收藏的次数
     * @param rid
     * @return
     */
    int findCountByRid(int rid);

    /**
     * 添加收藏记录
     * @param uid
     * @param rid
     * @return
     */
    void add(int uid, int rid);
}

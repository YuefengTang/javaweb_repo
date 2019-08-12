package cn.itcast.travel.service;

public interface FavoriteSevice {

    /**
     * 根据用户和线路的id查询是否被收藏
     * @param uid
     * @param rid
     * @return
     */
    boolean isFavorite(int uid,String rid);


    /**
     * 添加收藏
     * @param uid
     * @param rid
     */
    void add(int uid, String rid);
}

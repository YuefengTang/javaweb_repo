package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.service.FavoriteSevice;

public class FavoriteServiceImpl implements FavoriteSevice {
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    @Override
    public boolean isFavorite(int uid, String rid) {
        Favorite favorite = favoriteDao.findByUidAndRid(uid, Integer.parseInt(rid));
        return favorite != null;//favorite有值则为true,没值为false
    }

    @Override
    public void add(int uid, String rid) {
        favoriteDao.add(uid, Integer.parseInt(rid));
    }
}

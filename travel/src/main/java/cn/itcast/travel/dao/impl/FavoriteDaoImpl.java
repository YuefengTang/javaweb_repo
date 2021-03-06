package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

public class FavoriteDaoImpl implements FavoriteDao {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public Favorite findByUidAndRid(int uid, int rid) {
        Favorite favorite = null;
        try {
            String sql = "select * from tab_favorite where uid = ? and rid = ?";
            favorite = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Favorite.class), uid, rid);
        } catch (DataAccessException e) {
            //e.printStackTrace();
        }
        return favorite;
    }

    @Override
    public int findCountByRid(int rid) {
        String sql = "select count(*) from tab_favorite where rid = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, rid);
    }

    @Override
    public void add(int uid, int rid) {
        String sql = "insert into tab_favorite values (?,?,?)";
        jdbcTemplate.update(sql, rid, new Date(), uid);
    }
}

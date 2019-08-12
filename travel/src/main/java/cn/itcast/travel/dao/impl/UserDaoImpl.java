package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoImpl implements UserDao {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public User findByUsername(String username) {
        User user;
        try {
            String sql = "select * from tab_user where username = ?";
            user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), username);
            return user;
        } catch (Exception e) {
            //e.printStackTrace();
            return null;//注意此处需要做异常处理，因为当不存在该用户时，不处理不会返回null对象
        }
    }

    @Override
    public void save(User user) {
        String sql = "insert into tab_user(username,password,name,birthday,sex,telephone,email,STATUS,CODE)" +
                "values(?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getName(), user.getBirthday(),
                user.getSex(), user.getTelephone(), user.getEmail(),user.getStatus(),user.getCode());
    }

    @Override
    public User findByCode(String code) {
        User user;
        try {
            String sql = "select * from tab_user where CODE = ?";
            user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), code);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return user;
    }

    @Override
    public void updateStatus(User user) {
        String sql = "update tab_user set STATUS = 'Y' where uid = ?";
        jdbcTemplate.update(sql,user.getUid());
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        User user;
        try {
            String sql = "select * from tab_user where username = ? and password = ?";
            user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), username,password);
            return user;
        } catch (Exception e) {
            //e.printStackTrace();
            return null;//注意此处需要做异常处理，因为当不存在该用时，不处理不会返回null对象
        }
    }


}

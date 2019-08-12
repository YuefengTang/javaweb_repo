package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public int findTotalCount(int cid, String rname) {
        //String sql ="select count(*) from tab_route where cid = ?";
        //定义sql模板
        String sql = "select count(*) from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);

        List params = new ArrayList();//有条件
        //判断参数是否有值
        //用来分类信息的查询
        if (cid != 0) {
            sb.append(" and cid = ? ");
            params.add(cid);//添加？d对应的值
        }

        //用来搜索框的模糊查询
        if (rname != null && rname.length() > 0) {
            sb.append(" and rname like ? ");
            params.add("%" + rname + "%");//tomcat7会出现编码问题,需要将rname在获取后设置编码，servlet中
        }

        sql = sb.toString();

        return jdbcTemplate.queryForObject(sql, Integer.class, params.toArray());
    }

    @Override
    public List<Route> findByPage(int cid, int start, int pageSize, String rname) {
        //String sql = "select * from tab_route where cid = ? limit ? , ?";
        //定义SQL模板
        String sql = "select * from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);

        List params = new ArrayList();//有条件
        //判断时候有值
        if (cid != 0) {
            sb.append(" and cid = ? ");
            params.add(cid);//添加？d对应的值
        }

        //用来搜索框的模糊查询
        if (rname != null && rname.length() > 0) {
            sb.append(" and rname like ? ");
            params.add("%" + rname + "%");
        }
        sb.append(" limit ? , ? ");//分页条件
        params.add(start);
        params.add(pageSize);

        sql = sb.toString();
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Route.class), params.toArray());
    }

    @Override
    public Route findOne(int rid) {
        String sql = "select * from tab_route where rid = ?";
        return jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper<Route>(Route.class),rid);
    }
}

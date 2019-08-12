package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {

    private CategoryDao categoryDao = new CategoryDaoImpl();

    @Override
    public List<Category> findAll() {
        //考虑到分类信息不会经常变化，因此可以使用jedis进行缓存分类数据

        //1.从redis中查询
        //1.1获取jedis的客户端
        Jedis jedis = JedisUtil.getJedis();
        //1.2使用sortedset排序查询,因为分类导航需要按照是数据库分类表每个模块的id排序
        //Set<String> categorys = jedis.zrange("category", 0, -1);
        //1.3查询sortedset的cid,cname,以确保分类信息带cid
        Set<Tuple> categorys = jedis.zrangeWithScores("category", 0, -1);

        //2.判断集合是否为空
        List<Category> all = null;
        if (categorys == null || categorys.size() == 0) {
            //3.如果为空需要从数据库中查询，再将数据存入jedis
            //3.1从数据库中查询
            System.out.println("从mysql数据库中查询");
            all = categoryDao.findAll();
            //3.2将集合存储到redis中的category的key
            for (int i = 0; i < all.size(); i++) {
                jedis.zadd("category", all.get(i).getCid(), all.get(i).getCname());
            }

        } else {
            //如果不为空,因为findAll方法返回的是List集合，我们这里categorys是Set集合，因此这里做一个转换
            System.out.println("从redis缓存中查询");
            all = new ArrayList<>();
            for (Tuple tuple : categorys) {
                Category category = new Category();
                category.setCname(tuple.getElement());
                category.setCid((int) tuple.getScore());
                all.add(category);
            }
        }
        //4.返回
        return all;
    }
}

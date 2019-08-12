package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface UserDao {
    /**
     * 根据用户名称查询用户信息
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 保存用户信息
     * @param user
     */
    void save(User user);


    /**g根据激活码查找用户对象
     * @param code
     * @return
     */
    User findByCode(String code);

    /**
     * 修改用户状态
     * @param user
     */
    void updateStatus(User user);

    /**
     * 根据用户名和密码进行查询
     * @param username
     * @param password
     * @return
     */
    User findByUsernameAndPassword(String username, String password);
}

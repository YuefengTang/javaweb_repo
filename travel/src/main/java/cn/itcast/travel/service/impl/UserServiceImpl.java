package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();

    /**
     * 注册用户
     *
     * @param user
     * @return
     */
    @Override
    public boolean register(User user) {
        //1.根据用户名查询用户对象
        User u = userDao.findByUsername(user.getUsername());
        if (u != null) {
            //用户名存在
            return false;
        }
        //2.保存用户信息
        //2.1设置激活码，要求唯一
        user.setCode(UuidUtil.getUuid());
        //2.2设置激活状态
        user.setStatus("N");//没有激活
        userDao.save(user);
        //3.发送激活邮件，设置邮件的内容
        String context = "<a href='http://localhost/travel/user/activeUser?code=" + user.getCode() + "'>点击激活【黑马旅游网】</a>";
        MailUtils.sendMail(user.getEmail(), context, "【黑马旅游网】注册激活邮件");
        return true;
    }

    @Override
    public boolean active(String code) {
        //1.根据激活码查询用户对象
        User user = userDao.findByCode(code);
        if (user != null) {
            //2.调用dao的修改用户状态的方法
            userDao.updateStatus(user);
            return true;
        }
        return false;
    }

    @Override
    public User login(User user) {
        return userDao.findByUsernameAndPassword(user.getUsername(),user.getPassword());
    }

}

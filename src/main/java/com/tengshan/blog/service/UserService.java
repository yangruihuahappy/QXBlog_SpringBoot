package com.tengshan.blog.service;

import com.tengshan.blog.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Date;

public interface UserService {

    //登陆操作  检查用户名和密码是否一致
    User checkUser(String username,String password);
    //根据用户名查找用户
    User findUserByUserName(String username);
    //注册
    void register(User user);
    //获取用户个人信息
    User getUserInfo(Long id);
    //更新用户个人信息
    User updateUser(Long id,User user);
    //分页展示所有用户信息
    Page<User> listUser(Pageable pageable);
    //根据用户id删除用户
    void deleteByUserId(Long id);
    //根据内容搜索
    Page<User> listUser(String query,Pageable pageable);
    //查询今日新增用户数量
    int countUsersByCreateTimeAfter(Date createTime);

}

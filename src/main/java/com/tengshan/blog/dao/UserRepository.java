package com.tengshan.blog.dao;

import com.tengshan.blog.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;

public interface UserRepository extends JpaRepository<User, Long> {

    //登陆操作   根据同户名和密码返回数据库的数据
    User findByUsernameAndPassword(String username,String password);
    //注册操作  查找当前用户名是否存在
    User findByUsername(String username);
    //输入用户名或昵称内容查询
    @Query("select u from User u where u.username like ?1 or u.nickname like ?1")
    Page<User> findByQuery(String query, Pageable pageable);

    //今日新增用户数量
    int countUsersByCreateTimeAfter(Date createTime);
}

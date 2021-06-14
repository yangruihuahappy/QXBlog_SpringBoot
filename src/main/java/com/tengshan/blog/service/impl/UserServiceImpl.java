package com.tengshan.blog.service.impl;

import com.tengshan.blog.dao.UserRepository;
import com.tengshan.blog.pojo.User;
import com.tengshan.blog.service.UserService;
import com.tengshan.blog.util.MyBeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    //注入dao层
    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {
        return userRepository.findByUsernameAndPassword(username,password);
    }

    @Override
    public User findUserByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void register(User user) {
        user.setAdmin(false);  //注册默认为普通用户
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        userRepository.save(user);
    }

    @Override
    public User getUserInfo(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User updateUser(Long id, User user) {
        User user1 = userRepository.findById(id).orElse(null);
        if(user1 == null){
            System.out.println("获取更新对象失败");
        }else{
            BeanUtils.copyProperties(user,user1, MyBeanUtils.getNullPropertyNames(user));
            user1.setUpdateTime(new Date());
        }
        return userRepository.save(user1);
    }

    @Override
    public Page<User> listUser(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public void deleteByUserId(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Page<User> listUser(String query, Pageable pageable) {
        return userRepository.findByQuery(query,pageable);
    }

    @Override
    public int countUsersByCreateTimeAfter(java.sql.Date createTime) {
        return userRepository.countUsersByCreateTimeAfter(createTime);
    }
}

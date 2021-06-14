package com.tengshan.blog.service;

import com.tengshan.blog.pojo.Type;

import java.util.List;

public interface TypeService {

    //查找所有type
    List<Type> listType();
    //保存分类
    void saveType(Type type);
    //根据id查询分类
    Type findById(Long id);
    //更新分类
    Type updateType(Long id,Type type);
    //根据id删除分类
    void deleteById(Long id);
}

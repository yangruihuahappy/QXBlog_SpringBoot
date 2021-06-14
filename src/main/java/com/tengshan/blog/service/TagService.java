package com.tengshan.blog.service;

import com.tengshan.blog.pojo.Tag;

import java.util.List;

public interface TagService {

    //查找所有Tag
    List<Tag> listTag();
    //查找所有Tag(新建时)
    List<Tag> listTag(String ids);
    //保存标签
    void saveTag(Tag tag);
    //根据id查询标签
    Tag findById(Long id);
    //更新分类
    Tag updateTag(Long id,Tag type);
    //根据id删除标签
    void deleteById(Long id);
    //查询所有Tag
    List<Tag> listTagTop(Integer size);
}

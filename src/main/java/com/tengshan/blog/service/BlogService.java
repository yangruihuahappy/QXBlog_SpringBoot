package com.tengshan.blog.service;

import com.tengshan.blog.pojo.Blog;
import com.tengshan.blog.pojo.Comment;
import com.tengshan.blog.pojo.Type;
import com.tengshan.blog.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.util.List;

public interface BlogService {
    //保存博客（更新更新时间）
    Blog saveBlog(Blog blog);
    //保存博客（更新评论时间）
    Blog saveBlogByCommentTime(Blog blog);
    //列出所有博客
    List<Blog> listBlog();
    //列出所有博客 7个
    List<Blog> listTopBlog();
    //获取博客跳转
    Blog getAndConvert(Long id);
    //根据id删除博客
    void deleteById(Long id);
    //根据id查询博客
    Blog findById(Long id);
    //更新用户个人信息
    Blog updateBlog(Long id,Blog blog);
    //分页列出所有博客
    Page<Blog> listBlog (Pageable pageable);
    //推荐博客
    List<Blog> recommendBlogTop();
    //评论时间排序博客
    List<Blog> commentBlogTop();
    //根据title查询博客
    List<Blog> findByTitleLike(String name);
    //根据作者id查询博客
    List<Blog> findByAuthor(Long id);
    //根据User和Author查询博客
    List<Blog> findByAuthor(String author);
    //根据标签tag查询博客信息
    List<Blog> findByTagIds(Long id);
    //根据分类id查询博客
    List<Blog> findByTypeId(Long id);
    //查询今日创建的博客数量
    int countBlogsByCreateTimeAfter(Date createTime);
}

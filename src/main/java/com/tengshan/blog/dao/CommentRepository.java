package com.tengshan.blog.dao;

import com.tengshan.blog.pojo.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

//    List<Comment> findByBlogId(Long id, Sort sort);

    //根据新闻id和父级评论是空查询
    List<Comment> findByBlogIdAndParentCommentNull(Long id,Sort sort);

    //查当前博客下有多少评论
    @Query("select count(c) from Comment c where blog_id = ?1")
    Integer countCommentsByBlogId(Long id);

    //今日新增评论数量
    int countCommentsByCreateTimeAfter(Date createTime);

    //后台最新消息界面的最新评论消息
    @Query("select c from Comment c")
    List<Comment> findByCreateTime(Pageable pageable);

    //测试查询所有父评论
}

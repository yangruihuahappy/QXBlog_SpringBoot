package com.tengshan.blog.service;

import com.tengshan.blog.pojo.Comment;

import java.sql.Date;
import java.util.List;

public interface CommentService {
    //发表评论
    Comment saveComment(Comment comment);
    //展示所有评论
    List<Comment> listCommentByBlogId(Long blogId);

    //计算所有评论数
    Integer countComment(Long id);
    //计算所有评论
    Long countSumComment();
    //计算今日新增评论数量
    int countCommentsByCreateTimeAfter(Date createTime);
    //找到最新评论时间的评论
    List<Comment> findTopComment();
}

package com.tengshan.blog.service.impl;

import com.tengshan.blog.dao.CommentRepository;
import com.tengshan.blog.pojo.Comment;
import com.tengshan.blog.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.*;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

//    @Override
//    public Comment saveComment(Comment comment) {
//        return commentRepository.save(comment);
//    }
//
//    @Override
//    public List<Comment> listCommentByBlogId(Long blogId) {
//        Sort sort =Sort.by("createTime");
//        List<Comment> commentList =commentRepository.findByBlogId(blogId,sort);
//        System.out.println("运行到listCommentByNewId"+commentList);
//        List<Comment> commentsView = new ArrayList<>();
//        for(Comment comment:commentList){
//            Comment c = new Comment();
//            BeanUtils.copyProperties(comment,c);
//            commentsView.add(c);
//        }
//        return commentsView;
//    }


    @Override
    public Comment saveComment(Comment comment) {

        if(comment.getParentComment()!=null && comment.getParentComment().getId()!=-1){  //有父级评论
            comment.setParentComment(commentRepository.findById(comment.getParentComment().getId()).orElse(null));
        }else{
            comment.setParentComment(null);
        }
        comment.setCreateTime(new java.util.Date());
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort = Sort.by("createTime");
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(blogId,sort);
        System.out.println("运行到listCommentByNewId");
        return eachComment(comments);
    }

    //循环遍历
    //循环查找所有评论
    private List<Comment> eachComment(List<Comment> comments){
        List<Comment> commentsView = new ArrayList<>();
        System.out.println("eachComment");
        for(Comment comment:comments){
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }
        //合并评论的各层子代到第一代集合
        combineChildrenComment(commentsView);
        return commentsView;
    }

    private void combineChildrenComment(List<Comment> comments){
        System.out.println("combineChildrenComment");
        for(Comment comment:comments){
            List<Comment> replies = comment.getReplyComment();
            for(Comment reply : replies){
                //循环迭代，找出子代  存放在临时存放区
                recursively(reply);
            }
            comment.setReplyComment(tempReplies);
            //清除临时存放区
            tempReplies = new ArrayList<>();
        }
    }

    //临时找出子代
    private List<Comment> tempReplies = new ArrayList<>();

    private void recursively(Comment comment){
        tempReplies.add(comment);  //顶节点添加到临时存放区
        if(comment.getReplyComment().size()>0){
            List<Comment> replies = comment.getReplyComment();
            for(Comment reply:replies){
                tempReplies.add(reply);
                if(comment.getReplyComment().size()>0){
                    recursively(reply);
                }
            }
        }
    }




    @Override
    public Integer countComment(Long id) {
        System.out.println(id);
        System.out.println(commentRepository.countCommentsByBlogId(id));
        return commentRepository.countCommentsByBlogId(id);
    }

    @Override
    public Long countSumComment() {
        return commentRepository.count();
    }

    @Override
    public int countCommentsByCreateTimeAfter(Date createTime) {
        return commentRepository.countCommentsByCreateTimeAfter(createTime);
    }

    @Override
    public List<Comment> findTopComment() {
        Sort sort =Sort.by(Sort.Direction.DESC,"createTime");
        Pageable pageable = PageRequest.of(0,8,sort);
        return commentRepository.findByCreateTime(pageable);
    }

}

package com.tengshan.blog.service.impl;

import com.tengshan.blog.dao.BlogRepository;
import com.tengshan.blog.dao.CommentRepository;
import com.tengshan.blog.pojo.Blog;
import com.tengshan.blog.pojo.Comment;
import com.tengshan.blog.pojo.Type;
import com.tengshan.blog.pojo.User;
import com.tengshan.blog.service.BlogService;
import com.tengshan.blog.util.MarkdownUtils;
import com.tengshan.blog.util.MyBeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Blog saveBlog(Blog blog) {
        blog.setUpdateTime(new Date());
        return blogRepository.save(blog);
    }

    @Override
    public Blog saveBlogByCommentTime(Blog blog) {
        blog.setCommentUpdateTime(new Date());
        return blogRepository.save(blog);
    }

    @Override
    public List<Blog> listBlog() {
        return blogRepository.findAll();
    }

    @Override
    public List<Blog> listTopBlog() {
        Sort sort =Sort.by(Sort.Direction.DESC,"createTime");
        Pageable pageable = PageRequest.of(0,7,sort);
        return blogRepository.findTopAll(pageable);
    }



    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.findById(id).orElse(null);
        if(blog==null){
            System.out.println("该博客不存在");
        }
        Blog blog1 = new Blog();
        blog1.setCountComment(commentRepository.countCommentsByBlogId(id));
        BeanUtils.copyProperties(blog,blog1);
        String content = blog1.getContent();
        blog1.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        return blog1;
    }

    @Override
    public void deleteById(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public Blog findById(Long id) {
        return blogRepository.findById(id).orElse(null);
    }

    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog blog1 = blogRepository.findById(id).orElse(null);

        if(blog1==null){
            System.out.println("获取更新对象失败");
        }else{
            blog.setUpdateTime(new Date());
            BeanUtils.copyProperties(blog,blog1, MyBeanUtils.getNullPropertyNames(blog));

        }
        return blogRepository.save(blog1);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public List<Blog> recommendBlogTop() {
        Sort sort =Sort.by(Sort.Direction.DESC,"updateTime");
        Pageable pageable = PageRequest.of(0,1,sort);
        return blogRepository.findTop(pageable);
    }

    @Override
    public List<Blog> commentBlogTop() {
        Sort sort = Sort.by(Sort.Direction.DESC,"commentUpdateTime");
        Pageable pageable = PageRequest.of(0,5,sort);
        return blogRepository.findTopByCommentUpdateTime(pageable);
    }

    @Override
    public List<Blog> findByTitleLike(String name) {
        Pageable pageable = PageRequest.of(0,5);
        return blogRepository.findByTitleLike(pageable,name);
    }

    @Override
    public List<Blog> findByAuthor(Long id) {
        System.out.println("当前用户id:"+id);
        System.out.println(blogRepository.findByAuthor(id).toString());
        return blogRepository.findByAuthor(id);
    }

    @Override
    public List<Blog> findByAuthor(String author) {
        return blogRepository.findByAuthor(author);
    }

    @Override
    public List<Blog> findByTagIds(Long id) {
        return blogRepository.findByTagIds(id);
    }

    @Override
    public List<Blog> findByTypeId(Long id) {
        return blogRepository.findByTypeId(id);
    }

    @Override
    public int countBlogsByCreateTimeAfter(java.sql.Date createTime) {
        return blogRepository.countBlogsByCreateTimeAfter(createTime);
    }


}

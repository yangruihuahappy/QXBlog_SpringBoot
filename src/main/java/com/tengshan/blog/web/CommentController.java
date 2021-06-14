package com.tengshan.blog.web;

import com.tengshan.blog.pojo.Blog;
import com.tengshan.blog.pojo.Comment;
import com.tengshan.blog.pojo.User;
import com.tengshan.blog.service.BlogService;
import com.tengshan.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@RequestMapping
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private BlogService blogService;


    //展示所有评论
    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model){
        System.out.println("运行到这里"+blogId);
        model.addAttribute("comments",commentService.listCommentByBlogId(blogId));
        return "redirect:/toShow/"+blogId;
    }

    @PostMapping("/commentAdd")
    public String post(Comment comment, HttpSession session){
//        获取当前博客id
        Blog blog = (Blog) session.getAttribute("blog");
        Long blogId = blog.getId();

        comment.setBlog(blogService.findById(blogId));

        if(comment.getId()==null) {  //为新增
            comment.setBlog(blogService.findById(blogId));
            User user = (User) session.getAttribute("user");
            if (user.getAdmin()) {    //管理员
                comment.setAdminComment(true);
            }
            comment.setImage(user.getImage());
            comment.setCreateTime(new Date());
        }
        System.out.println("-----"+comment.toString());

        commentService.saveComment(comment);
        blog.setCountComment(commentService.countComment(blogId));
        blogService.saveBlogByCommentTime(blog);
        return "redirect:/comments/"+blogId;
    }

//
//    @RequestMapping("/commentAdd")
//    public String post(Comment comment, HttpSession session, Model model){
//        System.out.println("接收到的comment信息为："+comment);
//        System.out.println(comment.toString());
//        Blog blog = (Blog) session.getAttribute("blog");
//        System.out.println("blog信息为："+blog);
//        Long blogId = blog.getId();
//        System.out.println("blogId为："+blogId);
//
//        if(comment.getId()==null) {  //为新增
//            comment.setBlog(blogService.findById(blogId));
//            User user = (User) session.getAttribute("user");
//            if (user.getAdmin()) {    //管理员
//                comment.setAdminComment(true);
//            }
//            comment.setImage(user.getImage());
//            comment.setCreateTime(new Date());
//        }
//        commentService.saveComment(comment);
//        blog.setCountComment(commentService.countComment(blogId));
//        blogService.saveBlogByCommentTime(blog);
//
//        return "redirect:/toShow/"+blogId;
//
//    }

}

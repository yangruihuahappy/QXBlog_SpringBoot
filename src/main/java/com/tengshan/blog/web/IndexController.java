package com.tengshan.blog.web;

import com.tengshan.blog.pojo.*;
import com.tengshan.blog.service.BlogService;
import com.tengshan.blog.service.CommentService;
import com.tengshan.blog.service.TagService;
import com.tengshan.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
    @Autowired
    private CommentService commentService;

    //主页
    @RequestMapping("/")
    public String index(@PageableDefault(size = 4,sort = {"updateTime"},direction = Sort.Direction.DESC)
                        Pageable pageable, Model model, HttpSession session){

        model.addAttribute("page",blogService.listBlog(pageable));
        session.setAttribute("totalBlogNum",blogService.listBlog(pageable).getTotalElements());
        model.addAttribute("types",typeService.listType());
        model.addAttribute("pageByCommentUpdateTime",blogService.commentBlogTop());
        //获取推荐状态文章
        model.addAttribute("recommendBlog",blogService.recommendBlogTop());
        return "index";
    }

    //顶部“我的博客”板块 返回包含当前作者创建的博客List形式 根据作者查询
    @RequestMapping("/myBlogs")
    public String myBlogs(Model model, HttpSession session){
        System.out.println("运行到了我的博客");
        User user = (User) session.getAttribute("user");
        model.addAttribute("tags",tagService.listTagTop(7));
        model.addAttribute("types",typeService.listType());
        model.addAttribute("pageByCommentUpdateTime",blogService.commentBlogTop());
        model.addAttribute("page",blogService.findByAuthor(user.getNickname()));
        return "myBlog";
    }

    //列表页
    @RequestMapping("/toList")
    public String list(@PageableDefault(size = 6,sort = {"updateTime"},direction = Sort.Direction.DESC)
                       Pageable pageable,Model model){
        model.addAttribute("page",blogService.listBlog(pageable));
        model.addAttribute("tags",tagService.listTagTop(7));
        model.addAttribute("types",typeService.listType());
        model.addAttribute("pageByCommentUpdateTime",blogService.commentBlogTop());
        System.out.println("11111");
//        for(int i=0;i<6;i++) {
//            System.out.println(tagService.listTagTop(6).get(i).getBlogList().size());
//        }
        return "list";
    }

    //详情页
    @RequestMapping("/toShow/{id}")
    public String show(@PathVariable Long id,Model model,HttpSession session){
        Blog blog = blogService.getAndConvert(id);

        model.addAttribute("blog",blog);
        session.setAttribute("blog",blog);
        model.addAttribute("comments",commentService.listCommentByBlogId(id));
        model.addAttribute("pageByCommentUpdateTime",blogService.commentBlogTop());

        //新建一个评论对象，传到后端装
        Comment comment = new Comment();
        User user = (User) session.getAttribute("user");
        if(user!=null) {
            comment.setImage(user.getImage());
            comment.setNickname(user.getNickname());
            comment.setPhone(user.getPhone());
        }
        model.addAttribute("commentAdd",comment);
        System.out.println(blog.getTags());
        return "show";
    }

    //前台添加博客信息  可添加博客、分类、标签
    //列表页
    @RequestMapping("/toAdd")
    public String toAdd(@PageableDefault(size = 6,sort = {"updateTime"},direction = Sort.Direction.DESC)
                               Pageable pageable, Model model){
        model.addAttribute("addType",new Type());
        model.addAttribute("addTag",new Tag());
        model.addAttribute("addBlog",new Blog());
        setTypeAndTag(model);
        return "blogAdd";
    }

    //用来在修改和新增news页面显示所有type和tag的
    public void setTypeAndTag(Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
    }

    //主页搜索功能，搜索关键字模糊查询数据库，将结果按照一个List进行展示
    @RequestMapping("/Search")
    public String Search(@RequestParam String keyword, Model model){
        model.addAttribute("tags",tagService.listTagTop(7));
        model.addAttribute("types",typeService.listType());
        model.addAttribute("pageByCommentUpdateTime",blogService.commentBlogTop());
        model.addAttribute("page",blogService.findByTitleLike(keyword));
        System.out.println("test1");
        return "searchList";
    }

    //list myBlog页面的标签云 点击标签根据标签查询信息
    @RequestMapping("/formTag/{tag}")
    public String fromTag(@PathVariable Long tag,Model model, HttpSession session){
        System.out.println("根据标签显示博客");
        model.addAttribute("tags",tagService.listTagTop(7));
        model.addAttribute("types",typeService.listType());
        model.addAttribute("pageByCommentUpdateTime",blogService.commentBlogTop());
        model.addAttribute("page",blogService.findByTagIds(tag));
        return "searchList";//借用searchList界面来显示查询后的信息
    }

    //list myBlog页面的分类 点击分类根据分类查询信息
    @RequestMapping("/formType/{id}")
    public String formType(@PathVariable Long id,Model model, HttpSession session){
        System.out.println("根据标签显示博客");
        model.addAttribute("tags",tagService.listTagTop(7));
        model.addAttribute("types",typeService.listType());
        model.addAttribute("pageByCommentUpdateTime",blogService.commentBlogTop());
        model.addAttribute("page",blogService.findByTypeId(id));
        return "searchList";//借用searchList界面来显示查询后的信息
    }

    //myBlog页面删除博客 传入博客id 返回删除后的MyBlog页面
    @RequestMapping("/toDelete/{id}")
    public String blogDelete(@PathVariable Long id, RedirectAttributes attributes){
        System.out.println("待删除的id:"+id);
        blogService.deleteById(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/myBlogs";
    }

    //myBlog页面编辑博客 传入博客id 返回修改Blog界面
    @RequestMapping("/toEdit/{id}")
    public String blogEdit(@PathVariable Long id,Model model){
        System.out.println("待编辑的id:"+id);
        //先根据id获取博客信息
        Blog blog = blogService.findById(id);
        model.addAttribute("blog",blog);
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
        return "editBlog";
    }


}

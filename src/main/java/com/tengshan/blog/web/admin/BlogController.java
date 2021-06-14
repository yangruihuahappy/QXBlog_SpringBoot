package com.tengshan.blog.web.admin;

import com.tengshan.blog.pojo.Blog;
import com.tengshan.blog.pojo.Tag;
import com.tengshan.blog.pojo.Type;
import com.tengshan.blog.pojo.User;
import com.tengshan.blog.service.BlogService;
import com.tengshan.blog.service.TagService;
import com.tengshan.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class BlogController {

    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
    @Autowired
    private BlogService blogService;

    @RequestMapping("/blogList")
    public String showBlog(Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
        model.addAttribute("blogs",blogService.listBlog());
        return "admin/blogList";
    }

    //前台添加博客信息  可添加博客、分类、标签
    //列表页
    @RequestMapping("/toAdd")
    public String list(@PageableDefault(size = 6,sort = {"updateTime"},direction = Sort.Direction.DESC)
                               Pageable pageable, Model model){
        model.addAttribute("page",blogService.listBlog(pageable));
        model.addAttribute("tags",tagService.listTagTop(7));
        model.addAttribute("pageByCommentUpdateTime",blogService.commentBlogTop());
//        for(int i=0;i<6;i++) {
//            System.out.println(tagService.listTagTop(6).get(i).getBlogList().size());
//        }
        return "blogAdd";
    }


    //后台添加博客信息  可添加博客、分类、标签
    @GetMapping("/blogAdd")
    public String Add(Model model){
        System.out.println("此时为新增");
        model.addAttribute("addType",new Type());
        model.addAttribute("addTag",new Tag());
        model.addAttribute("addBlog",new Blog());
        setTypeAndTag(model);
        return "admin/blogAdd";
    }

    //新增和编辑分类
    @PostMapping("/type/save")
    public String saveType(Type type, RedirectAttributes attributes, HttpSession session){
        System.out.println("接收到的type信息"+type);
        Type type1;
        if(type.getId()==null){ //新增
            System.out.println("新增分类");
            typeService.saveType(type);
            attributes.addFlashAttribute("message","新增操作成功!");
        }else{ //为更新
            type1 = typeService.updateType(type.getId(),type);
            session.setAttribute("type",type1);
            System.out.println("修改成功");
            attributes.addFlashAttribute("message","操作成功！");
        }

        return "redirect:/admin/blogList";
    }
    //新增和编辑分类
    @PostMapping("/tag/save")
    public String saveTag(Tag tag, RedirectAttributes attributes, HttpSession session){
        System.out.println("接收到的type信息"+tag);
        Tag tag1;
        if(tag.getId()==null){ //新增
            System.out.println("新增分类");
            tagService.saveTag(tag);
            attributes.addFlashAttribute("message","新增操作成功!");
        }else {
            //为更新
            tag1 = tagService.updateTag(tag.getId(),tag);
            session.setAttribute("tag",tag1);
            System.out.println("修改成功");
            attributes.addFlashAttribute("message","操作成功！");
        }
        return "redirect:/admin/blogList";
    }


    //用来在修改和新增blog页面显示所有type和tag的
    public void setTypeAndTag(Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
    }

    //新增博客
    @PostMapping("/blog/save")
    public String saveBlog(Blog blog,RedirectAttributes attributes,HttpSession session){
        System.out.println("接收到的blog信息为："+blog);
        System.out.println(blog.getTagIds().toString());
        Blog blog1;
        User user = (User) session.getAttribute("user");
        if(blog.getId()==null){  //为新增
            System.out.println("新增博客");

            blog.setUser(user);//设置作者
            blog.setAuthor(user.getNickname());
            blog.setCreateTime(new Date()); //设置初始创建时间
            blog.setPraiseNum(0);   //设置初始点赞数0
            blog.setCountComment(0);//
            blog.setTags(tagService.listTag(blog.getTagIds()));
            blog.setCountComment(0);
            System.out.println(blog.getTags());
            blogService.saveBlog(blog);
            attributes.addFlashAttribute("message","新增博客成功");

        }else{  //为更新
            blog.init();
            List<Tag> tags = tagService.listTag(blog.getTagIds());
            blog.setTags(tags);
            System.out.println("初始化之后的blog："+blog.toString());
            blog1 = blogService.updateBlog(blog.getId(),blog);

            session.setAttribute("blog",blog1);
            System.out.println("修改成功");
            attributes.addFlashAttribute("message","操作成功！");

        }
        if(user.getAdmin()){
            return "redirect:/admin/blogList";
        }else{
            System.out.println("此时为非管理员发布");
            return "redirect:/toList";
        }

    }

    //根据TagIds给博客添加tag


    //图片展示区域
    @RequestMapping("/gallery")
    public String showGallery(Model model){
        model.addAttribute("blogs",blogService.listBlog());
        return "admin/gallery";
    }

    //展示点击的博客
    @RequestMapping("/blog/{id}")
    public String news(@PathVariable Long id, Model model){
        Blog blog = blogService.getAndConvert(id);
        model.addAttribute("blog",blog);
        //根据当前博客的type找同类型
        Long typeId = blog.getType().getId();
        model.addAttribute("typePage",blogService.findByTypeId(typeId));
        System.out.println("------"+blog.getTags());
        return "admin/blogShow";
    }

    //删除点击的博客
    @RequestMapping("/{id}/blogDelete")
    public String blogDelete(@PathVariable Long id,RedirectAttributes attributes){
        System.out.println("待删除的id:"+id);
        blogService.deleteById(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/blogList";
    }

    //更新点击的博客
    @RequestMapping("/{id}/blogUpdate")
    public String blogUpdate(@PathVariable Long id,Model model){
        System.out.println("待更新的id："+id);
        Blog blog = blogService.findById(id);
        model.addAttribute("addType",new Type());
        model.addAttribute("addTag",new Tag());
        model.addAttribute("addBlog",blog);
        setTypeAndTag(model);
        return "admin/blogAdd";
    }

    //更新点击的分类
    @RequestMapping("/{id}/typeUpdate")
    public String typeUpdate(@PathVariable Long id,Model model){
        System.out.println("待更新的id："+id);
        Type type = typeService.findById(id);
        model.addAttribute("addType",type);
        model.addAttribute("addTag",new Tag());
        model.addAttribute("addBlog",new Blog());
        setTypeAndTag(model);
        return "admin/blogAdd";
    }

    //更新点击的分类
    @RequestMapping("/{id}/tagUpdate")
    public String tagUpdate(@PathVariable Long id,Model model){
        System.out.println("待更新的id："+id);
        Tag tag = tagService.findById(id);
        model.addAttribute("addType",new Type());
        model.addAttribute("addTag",tag);
        model.addAttribute("addBlog",new Blog());
        setTypeAndTag(model);
        return "admin/blogAdd";
    }

    //删除点击的分类
    @RequestMapping("/{id}/typeDelete")
    public String typeDelete(@PathVariable Long id,RedirectAttributes attributes){
        System.out.println("待删除的id:"+id);
        typeService.deleteById(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/blogList";
    }

    //删除点击的博客
    @RequestMapping("/{id}/tagDelete")
    public String tagDelete(@PathVariable Long id,RedirectAttributes attributes){
        System.out.println("待删除的id:"+id);
        tagService.deleteById(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/blogList";
    }
}

package com.tengshan.blog.web.admin;

import com.tengshan.blog.pojo.User;
import com.tengshan.blog.service.BlogService;
import com.tengshan.blog.service.CommentService;
import com.tengshan.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.Date;

@Controller
@RequestMapping("/admin")
public class ManageController {
    @Autowired
    private UserService userService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private CommentService commentService;

    //后台主页
    @RequestMapping("/manage")
    public String toManage(Model model,Pageable pageable,HttpSession session){
        model.addAttribute("totalUser",userService.listUser(pageable).getTotalElements());
        model.addAttribute("totalBlog",blogService.listBlog(pageable).getTotalElements());
        model.addAttribute("totalComment",commentService.countSumComment());
        //后台主页显示今日新增信息
        java.sql.Date dayDateSql = new java.sql.Date(getZero().getTime());
        model.addAttribute("todayBlogCount",blogService.countBlogsByCreateTimeAfter(dayDateSql));
        model.addAttribute("todayUserCount",userService.countUsersByCreateTimeAfter(dayDateSql));
        model.addAttribute("todayCommentCount",commentService.countCommentsByCreateTimeAfter(dayDateSql));
        //后台主页显示本周新增信息
        java.sql.Date weekDateSql = new java.sql.Date(getTimesWeekmorning().getTime());
        model.addAttribute("weekBlogCount",blogService.countBlogsByCreateTimeAfter(weekDateSql));
        model.addAttribute("weekUserCount",userService.countUsersByCreateTimeAfter(weekDateSql));
        model.addAttribute("weekCommentCount",commentService.countCommentsByCreateTimeAfter(weekDateSql));


        model.addAttribute("user",session.getAttribute("user")); //添加当前用户信息
        System.out.println(model.getAttribute("user"));
        return "admin/manageIndex";
    }

    //获取今日零点时间
    public Date getZero(){
        java.util.Date date = new java.util.Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
    //获取本周周一零点时间
    public static Date getTimesWeekmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime();
    }

    //后台最新消息界面
    @RequestMapping("/messages")
    public String showMessages(@PageableDefault(size = 4,sort = {"createTime"},direction = Sort.Direction.DESC)
                                           Pageable pageable, Model model, HttpSession session){
        System.out.println("test:"+commentService.findTopComment().toString());
        model.addAttribute("topComments",commentService.findTopComment());
        model.addAttribute("page",blogService.listTopBlog());
        return  "admin/messages";
    }

    //后台用户展示界面
    @RequestMapping("/userList")
    public String showUsers(@PageableDefault(size = 50,sort = {"updateTime"},direction = Sort.Direction.DESC)
                            Pageable pageable, Model model){
        model.addAttribute("page",userService.listUser(pageable));
        return "admin/userList";
    }

    //后台编辑用户信息
    @GetMapping("/userChange/{id}/toUpdate")
    public String toUpdate(@PathVariable Long id,Model model){
        User user = userService.getUserInfo(id);
        model.addAttribute("updateUser", user);
        return "admin/userChange";
    }

    //后台添加用户信息
    @GetMapping("/userAdd")
    public String Add(Model model){
        System.out.println("此时为新增");
        model.addAttribute("addUser",new User());
        return "admin/userAdd";
    }

    //保存修改后的用户信息
    @PostMapping("/user/save")
    public String save(User user, RedirectAttributes attributes, HttpSession session){
        System.out.println("接收到的用户信息"+user);
        User user1;
        if(user.getId()==null){ //没有找到该用户  为新增
            userService.register(user);
            System.out.println("此时执行新增用户");
            attributes.addFlashAttribute("message","新增操作成功！");
        }else{
            user1 = userService.updateUser(user.getId(),user);
            session.setAttribute("updateUser",user1);
            System.out.println("此时执行修改用户");
            attributes.addFlashAttribute("message","编辑操作成功！");
        }
        return "redirect:/admin/userList";
    }

    //后台删除用户信息
    @GetMapping("/{id}/delete")
    public String deleteById(@PathVariable Long id,RedirectAttributes attributes){
        System.out.println("待删除的id"+id);
        userService.deleteByUserId(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/userList";
    }

    //后台用户搜索功能
    @RequestMapping("/search")
    public String Search(@PageableDefault(size = 5,sort = {"updateTime"},direction = Sort.Direction.DESC)Pageable pageable,
                         @RequestParam String query,Model model){
        System.out.println(query);
        model.addAttribute("page",userService.listUser("%"+query+"%",pageable));
        System.out.println(model.getAttribute("page"));
        model.addAttribute("query",query);
        return "admin/userList";
    }

    @RequestMapping("/manageLogin")
    public String manageLogin(){
        return "admin/manageLogin";
    }


}

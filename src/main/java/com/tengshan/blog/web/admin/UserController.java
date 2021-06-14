package com.tengshan.blog.web.admin;

import com.tengshan.blog.pojo.User;
import com.tengshan.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * 用户Controller
 * 功能： 登陆   注册  注销  修改个人信息  修改密码
 */

@Controller
@RequestMapping("/admin")
public class UserController {

    //注入service层
    @Autowired
    private UserService userService;

    @GetMapping      //跳转到登陆界面
    public String loginPage(){
        return "admin/login";
    }

    //注册
    @PostMapping("/register")
    public String register(@RequestParam String username,@RequestParam String password,
                           @RequestParam String phone,@RequestParam String repassword,
                           HttpSession session,RedirectAttributes attributes){
        System.out.println("这里是注册");
        System.out.println(username+password);
        if(password.equals(repassword)){    //两次密码一致
            User user = userService.findUserByUserName(username);
            if(user == null){  //当前用户名不存在
                User user1 = new User();
                user1.setUsername(username);
                user1.setPassword(password);
                user1.setPhone(phone);
                userService.register(user1);
                attributes.addFlashAttribute("message","注册成功！");
                session.setAttribute("user",user1);   //保存到session中传到前端
                System.out.println(username+password);
                return login(username,password,session,attributes);
            }else{
                attributes.addFlashAttribute("message","用户名已存在!");

            }
        }

        return "redirect:/admin";
    }

    //登陆
    @PostMapping("/login")
    public String login(@RequestParam String username , @RequestParam String password,
                        HttpSession session, RedirectAttributes attributes) {
        System.out.println(username+password);
        User user =userService.checkUser(username,password);
        //验证
        if(user!=null){
            user.setPassword(null);
            if(user.getAdmin()){
                user.setAdmin(true);
            }
            System.out.println("登陆时user"+user);
            session.setAttribute("   ",user);   //保存到session中传到前端
            return "redirect:/";
        }else {
            attributes.addFlashAttribute("message","用户名或密码错误");
            return "redirect:/admin";
        }
    }

    //登陆
    @PostMapping("/manageLogin")
    public String manageLogin(@RequestParam String username , @RequestParam String password,
                        HttpSession session, RedirectAttributes attributes) {
        System.out.println(username+password);
        User user =userService.checkUser(username,password);
        //验证
        if(user!=null && user.getAdmin()){
            user.setPassword(null);
            if(user.getAdmin()){
                user.setAdmin(true);
            }
            System.out.println("登陆时user"+user);
            session.setAttribute("user",user);   //保存到session中传到前端
            return "redirect:/admin/manage";
        }else if(user==null) {
            attributes.addFlashAttribute("message","用户名或密码错误");
            return "redirect:/admin/manageLogin";
        }else{
            attributes.addFlashAttribute("message","您当前权限不能访问");
            return "redirect:/admin";
        }
    }

    //注销
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/admin";
    }

    //修改个人信息 1 获取数据库中个人信息
    @GetMapping("/userinfo/{id}")
    public String toUpdate(@PathVariable Long id, Model model){
        User user = userService.getUserInfo(id);
        model.addAttribute("user",user);
        return "admin/userinfo";
    }

    //保存修改用户信息界面的表单
    @PostMapping("/userUpdate")
    public String update(User user,HttpSession session,RedirectAttributes attributes){
        User user1;
        if(user.getId()==null){ //没有找到该用户
            System.out.println("用户id没有传过来");
            attributes.addFlashAttribute("message","操作失败！");
            return "login";
        }else{
            user1 = userService.updateUser(user.getId(),user);
            session.setAttribute("user",user1);
            System.out.println("修改成功");
            attributes.addFlashAttribute("message","操作成功！");
        }
        return "redirect:/";
    }

    //修改密码
    @RequestMapping("/userinfo/password")
    public String changePassword(@RequestParam String newPassword,
                                 User user,RedirectAttributes attributes,HttpSession session){

        User user2;
        if(user.getId()==null){ //没有找到该用户
            System.out.println("用户id没有传过来");
            attributes.addFlashAttribute("message","操作失败！");
        }else{
            user.setPassword(newPassword);
            user2 = userService.updateUser(user.getId(),user);
            session.setAttribute("user",user2);
            System.out.println("修改密码成功");
            attributes.addFlashAttribute("message","操作成功！");
        }
        return "admin/login";
    }
}

package com.qf.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.qf.entity.Page;
import com.qf.entity.ResultEntity;
import com.qf.entity.User;
import com.qf.service.impl.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Reference
    private IUserService userService;

    @RequestMapping(value = "/getUserPage")
    public String getUserPage(User user,Page page, Model model){
        PageInfo<User> pageInfo = userService.getUserPage(page,user);
        model.addAttribute("page",pageInfo);
        model.addAttribute("url","user/getUserPage");
        return "user/userList";
    }

    @RequestMapping(value = "/addUser")
    @ResponseBody
    public ResultEntity<String> addUser(User user){
        userService.add(user);
        return ResultEntity.successMessage("添加成功");
    }

    @RequestMapping(value = "/getUserById/{id}")
    public String  getUserById(@PathVariable Integer id, Model model){
        User user = userService.getById(id);
        model.addAttribute("user",user);
        return "user/updateUser";
    }

}

package com.qf.Controller;

import com.qf.entity.User;
import com.qf.service.impl.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/getUserList")
    public String getUserList(Model model){
        List<User> userList = userService.getUserList();
        model.addAttribute("userList",userList);
        return "userList";
    }
    @RequestMapping(value = "/getUserById/{id}")
    public String getUserById(@PathVariable Integer id,Model model){
        User user = userService.getUserById(id);
        model.addAttribute("user",user);
        return "updateUser";
    }
    @RequestMapping(value = "/updateUser")
    public String updateUser(User user){
        System.out.println(user.toString());
        userService.updateUser(user);
        return "redirect:getUserList";
    }
    @RequestMapping(value = "/addUser")
    public String addUser(){
        User user=new User();
        user.setUsername("zs");
        user.setPassword("123");
        userService.addUser(user);
        return "redirect:getUserList";
    }
    @RequestMapping(value = "/deleteById/{id}")
    public String deleteById(@PathVariable Integer id){
        userService.deleteById(id);
        return "redirect:../getUserList";
    }


}


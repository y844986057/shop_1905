package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.aop.IsLogin;
import com.qf.entity.Address;
import com.qf.entity.User;
import com.qf.service.impl.IAddressService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/address")
public class AddressController {
    @Reference
    private IAddressService addressService;

    @RequestMapping(value = "/add")
    @IsLogin(mustUser = true)
    public String add(Address address, User user){
        address.setUid(user.getId());
        addressService.addAddress(address);
        return "redirect:/order/toOrder";
    }

}

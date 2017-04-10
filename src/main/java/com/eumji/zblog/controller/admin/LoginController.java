package com.eumji.zblog.controller.admin;

import com.eumji.zblog.service.UserService;
import com.eumji.zblog.util.Md5Util;
import com.eumji.zblog.util.ResultInfo;
import com.eumji.zblog.util.ResultInfoFactory;
import com.eumji.zblog.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * FILE: com.eumji.zblog.controller.admin.LoginController.java
 * MOTTO:  不积跬步无以至千里,不积小流无以至千里
 * AUTHOR: EumJi
 * DATE: 2017/4/9
 * TIME: 15:32
 */
@RestController
public class LoginController {

    @Autowired
    private UserService userService;
    @RequestMapping("/login/auth")
    public ResultInfo loginAuth(User user){
        ResultInfo resultInfo = null;
        System.out.println(Md5Util.pwdDigest(user.getPassword()));
        System.out.println("user:"+user);
        User userInfo = userService.loadUserByUsername(user.getUsername());
        System.out.println("password:"+userInfo.getPassword());
        if (user==null){
            resultInfo =  ResultInfoFactory.getErrorRestInfo("账号不存在");
        }else{
            if (userInfo.getPassword().equals(Md5Util.pwdDigest(user.getPassword()))){
                resultInfo = ResultInfoFactory.getSuccessRestInfo();
            }else {
                resultInfo = ResultInfoFactory.getErrorRestInfo("账号或密码错误");
            }

        }
        return resultInfo;

    }
}
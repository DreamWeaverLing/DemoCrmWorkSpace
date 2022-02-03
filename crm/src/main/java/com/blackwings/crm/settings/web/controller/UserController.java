package com.blackwings.crm.settings.web.controller;

import com.blackwings.crm.exception.LoginException;
import com.blackwings.crm.settings.domain.User;
import com.blackwings.crm.settings.service.UserService;
import com.blackwings.crm.settings.service.impl.UserServiceImpl;
import com.blackwings.crm.utils.MD5Util;
import com.blackwings.crm.utils.PrintJson;
import com.blackwings.crm.utils.ServiceFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request,HttpServletResponse response){
        String servletPath = request.getServletPath();
        if ("/settings/user/login.do".equals(servletPath)){
            login(request,response);
        }
    }

    private void login(HttpServletRequest request,HttpServletResponse response) {
        System.out.println("进入登录方法");

        String loginAct = request.getParameter("username");
        String loginPwd = request.getParameter("password");
        String loginIP = request.getRemoteAddr();
        loginPwd = MD5Util.getMD5(loginPwd);

        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());

        try {
            User user = userService.login(loginAct,loginPwd,loginIP);
            request.getSession().setAttribute("user",user);
            PrintJson.printJsonFlag(response,true);
        } catch (LoginException e) {
            e.printStackTrace();
            String msg = e.getMessage();
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("success",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(response,map);
        }


    }
}

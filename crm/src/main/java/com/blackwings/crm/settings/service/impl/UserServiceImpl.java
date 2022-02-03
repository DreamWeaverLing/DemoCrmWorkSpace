package com.blackwings.crm.settings.service.impl;

import com.blackwings.crm.exception.LoginException;
import com.blackwings.crm.settings.dao.UserDao;
import com.blackwings.crm.settings.domain.User;
import com.blackwings.crm.settings.service.UserService;
import com.blackwings.crm.utils.DateTimeUtil;
import com.blackwings.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        Map<String,String> map = new HashMap<String, String>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);

        User user = userDao.login(map);
        if (user == null) {
            throw new LoginException("用户名或密码错误");
        }

        String sysTime = DateTimeUtil.getSysTime();
        if (sysTime.compareTo(user.getExpireTime()) > 0){
            throw new LoginException("账户已失效");
        }

        if ("0".equals(user.getLockState())){
            throw new LoginException("账户已锁定");
        }

        if (!user.getAllowIps().contains(ip)){
            throw new LoginException("非法IP");
        }

        return user;
    }

    public List<User> getUserList() {
        List<User> list = userDao.getUserList();
        return list;
    }
}

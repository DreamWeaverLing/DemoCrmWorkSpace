package com.blackwings.crm.settings.service;

import com.blackwings.crm.exception.LoginException;
import com.blackwings.crm.settings.domain.User;

import java.util.List;

public interface UserService {

    User login(String loginAct,String loginPwd,String ip) throws LoginException;

    List<User> getUserList();
}

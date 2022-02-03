package com.blackwings.crm.workbench.web.controller;

import com.blackwings.crm.settings.domain.User;
import com.blackwings.crm.settings.service.UserService;
import com.blackwings.crm.settings.service.impl.UserServiceImpl;
import com.blackwings.crm.utils.PrintJson;
import com.blackwings.crm.utils.ServiceFactory;
import com.blackwings.crm.workbench.dao.ContactsDao;
import com.blackwings.crm.workbench.domain.Activity;
import com.blackwings.crm.workbench.domain.Contacts;
import com.blackwings.crm.workbench.service.ActivityService;
import com.blackwings.crm.workbench.service.ContactsService;
import com.blackwings.crm.workbench.service.CustomerService;
import com.blackwings.crm.workbench.service.impl.ActivityServiceImpl;
import com.blackwings.crm.workbench.service.impl.ContactsServiceImpl;
import com.blackwings.crm.workbench.service.impl.CustomerServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class TranController  extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if ("/workbench/transaction/createNewTransaction.do".equals(servletPath)) {
            createNewTransaction(request,response);
        } else if ("/workbench/transaction/searchActivity.do".equals(servletPath)) {
            searchActivity(request,response);
        } else if ("/workbench/transaction/searchContacts.do".equals(servletPath)) {
            searchContacts(request,response);
        } else if ("/workbench/transaction/getCustomerName.do".equals(servletPath)) {
            getCustomerName(request,response);
        }
    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到获取客户姓名的方法");
        String name = request.getParameter("name");
        CustomerService customerService = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        List<String> customerName = customerService.getCustomerName(name);
        PrintJson.printJsonObj(response,customerName);
    }

    private void searchContacts(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到获取联系人列表的方法");
        String name = request.getParameter("name");
        ContactsService contactsService = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());
        List<Contacts> contactsList = contactsService.searchContacts(name);
        PrintJson.printJsonObj(response,contactsList);
    }

    private void searchActivity(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到获取市场活动列表的方法");
        String name = request.getParameter("name");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> activityList = activityService.searchActivity(name);
        PrintJson.printJsonObj(response,activityList);
    }

    private void createNewTransaction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入创建新交易方法");
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = userService.getUserList();
        request.setAttribute("userList",userList);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);
    }

}
package com.blackwings.crm.workbench.web.controller;

import com.blackwings.crm.settings.domain.DicValue;
import com.blackwings.crm.settings.domain.User;
import com.blackwings.crm.settings.service.UserService;
import com.blackwings.crm.settings.service.impl.UserServiceImpl;
import com.blackwings.crm.utils.DateTimeUtil;
import com.blackwings.crm.utils.PrintJson;
import com.blackwings.crm.utils.ServiceFactory;
import com.blackwings.crm.utils.UUIDUtil;
import com.blackwings.crm.vo.PaginationVO;
import com.blackwings.crm.workbench.domain.Activity;
import com.blackwings.crm.workbench.domain.Contacts;
import com.blackwings.crm.workbench.domain.Tran;
import com.blackwings.crm.workbench.domain.TranHistory;
import com.blackwings.crm.workbench.service.ActivityService;
import com.blackwings.crm.workbench.service.ContactsService;
import com.blackwings.crm.workbench.service.CustomerService;
import com.blackwings.crm.workbench.service.TranService;
import com.blackwings.crm.workbench.service.impl.ActivityServiceImpl;
import com.blackwings.crm.workbench.service.impl.ContactsServiceImpl;
import com.blackwings.crm.workbench.service.impl.CustomerServiceImpl;
import com.blackwings.crm.workbench.service.impl.TranServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        } else if ("/workbench/transaction/saveTransaction.do".equals(servletPath)) {
            saveTransaction(request,response);
        } else if ("/workbench/transaction/searchTran.do".equals(servletPath)) {
            searchTran(request,response);
        } else if ("/workbench/transaction/detail.do".equals(servletPath)) {
            detail(request,response);
        } else if ("/workbench/transaction/getTranHistory.do".equals(servletPath)) {
            getTranHistory(request,response);
        } else if ("/workbench/transaction/changeStage.do".equals(servletPath)) {
            changeStage(request,response);
        } else if ("/workbench/transaction/getEChartsData.do".equals(servletPath)) {
            getEChartsData(request,response);
        }
    }

    private void getEChartsData(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入获取图表数据方法");
        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());
        List list = tranService.getEChartsData();
        PrintJson.printJsonObj(response,list);
    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入改变交易阶段方法");
        String stage = request.getParameter("stage");
        String id = request.getParameter("id");
        String money = request.getParameter("money");
        String expectedDate = request.getParameter("expectedDate");
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();

        Tran tran = new Tran();
        tran.setId(id);
        tran.setStage(stage);
        tran.setMoney(money);
        tran.setExpectedDate(expectedDate);
        tran.setEditBy(editBy);
        tran.setEditTime(editTime);

        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());
        boolean success = tranService.changeStage(tran);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("success",success);
        map.put("tran",tran);
        PrintJson.printJsonObj(response,map);
    }

    private void getTranHistory(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入获取交易历史方法");
        String tranId = request.getParameter("tranId");
        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());
        List<TranHistory> tranHistoryList = tranService.getTranHistory(tranId);
        PrintJson.printJsonObj(response,tranHistoryList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到跳转详细信息页方法");
        String id = request.getParameter("id");
        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());
        List<DicValue> stageListDic = (List<DicValue>) request.getServletContext().getAttribute("stage");
        ObjectMapper om = new ObjectMapper();
        String stageList = om.writeValueAsString(stageListDic);
        Tran tran = tranService.detail(id);
        request.setAttribute("tran",tran);
        request.setAttribute("stageList",stageList);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request,response);
    }

    private void searchTran(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入搜索交易列表方法");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String customerName = request.getParameter("customerName");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String contactsName = request.getParameter("contactsName");
        int pageNo = Integer.valueOf(request.getParameter("pageNo"));
        int pageSize = Integer.valueOf(request.getParameter("pageSize"));
        int skipNo = (pageNo - 1) * pageSize;

        Map map = new HashMap();
        map.put("owner",owner);
        map.put("name",name);
        map.put("customerId",customerName);
        map.put("stage",stage);
        map.put("type",type);
        map.put("source",source);
        map.put("contactsId",contactsName);
        map.put("pageSize",pageSize);
        map.put("skipNo",skipNo);

        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());
        PaginationVO<Tran> paginationVO = tranService.searchTran(map);
        PrintJson.printJsonObj(response,paginationVO);
    }

    private void saveTransaction(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("进入到保存交易方法");
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String customerName = request.getParameter("customerName");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();

        Tran tran = new Tran();
        tran.setId(id);
        tran.setOwner(owner);
        tran.setMoney(money);
        tran.setName(name);
        tran.setExpectedDate(expectedDate);
        tran.setStage(stage);
        tran.setType(type);
        tran.setSource(source);
        tran.setActivityId(activityId);
        tran.setContactsId(contactsId);
        tran.setDescription(description);
        tran.setContactSummary(contactSummary);
        tran.setNextContactTime(nextContactTime);
        tran.setCreateBy(createBy);
        tran.setCreateTime(createTime);

        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());
        boolean success = tranService.saveTransaction(tran,customerName);
        if (success){
            response.sendRedirect(request.getContextPath()+"/workbench/transaction/index.jsp");
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
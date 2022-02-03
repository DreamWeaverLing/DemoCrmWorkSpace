package com.blackwings.crm.workbench.web.controller;

import com.blackwings.crm.settings.domain.User;
import com.blackwings.crm.settings.service.UserService;
import com.blackwings.crm.settings.service.impl.UserServiceImpl;
import com.blackwings.crm.utils.DateTimeUtil;
import com.blackwings.crm.utils.PrintJson;
import com.blackwings.crm.utils.ServiceFactory;
import com.blackwings.crm.utils.UUIDUtil;
import com.blackwings.crm.vo.PaginationVO;
import com.blackwings.crm.workbench.domain.Activity;
import com.blackwings.crm.workbench.domain.Clue;
import com.blackwings.crm.workbench.domain.ClueRemark;
import com.blackwings.crm.workbench.domain.Tran;
import com.blackwings.crm.workbench.service.ActivityService;
import com.blackwings.crm.workbench.service.ClueService;
import com.blackwings.crm.workbench.service.impl.ActivityServiceImpl;
import com.blackwings.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController  extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if ("/workbench/clue/getUserList.do".equals(servletPath)) {
            getUserList(request,response);
        } else if ("/workbench/clue/saveClue.do".equals(servletPath)) {
            saveClue(request,response);
        } else if ("/workbench/clue/searchClue.do".equals(servletPath)) {
            searchClue(request,response);
        } else if ("/workbench/clue/detail.do".equals(servletPath)) {
            detail(request,response);
        } else if ("/workbench/clue/saveRemark.do".equals(servletPath)) {
            saveRemark(request,response);
        } else if ("/workbench/clue/showRemarkList.do".equals(servletPath)) {
            showRemarkList(request,response);
        } else if ("/workbench/clue/updateRemark.do".equals(servletPath)) {
            updateRemark(request,response);
        } else if ("/workbench/clue/deleteRemark.do".equals(servletPath)) {
            deleteRemark(request,response);
        } else if ("/workbench/clue/showUnbundActivityList.do".equals(servletPath)) {
            showUnbundActivityList(request,response);
        } else if ("/workbench/clue/getRelationActivityList.do".equals(servletPath)) {
            getRelationActivityList(request,response);
        } else if ("/workbench/clue/createBundRelation.do".equals(servletPath)) {
            createBundRelation(request,response);
        } else if ("/workbench/clue/deleteRelationActivity.do".equals(servletPath)) {
            deleteRelationActivity(request,response);
        } else if ("/workbench/clue/searchActivity.do".equals(servletPath)) {
            searchActivity(request,response);
        } else if ("/workbench/clue/convert.do".equals(servletPath)) {
            convert(request,response);
        }
    }

    private void convert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("进入线索转换方法");
        String clueId = request.getParameter("clueId");
        String flag = request.getParameter("flag");

        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        Tran tran = null;

        if ("a".equals(flag)){
            tran = new Tran();
            String money = request.getParameter("money");
            String tradeName = request.getParameter("tradeName");
            String expectedDate = request.getParameter("expectedDate");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");
            String createTime = DateTimeUtil.getSysTime();
            String id = UUIDUtil.getUUID();

            tran.setMoney(money);
            tran.setName(tradeName);
            tran.setExpectedDate(expectedDate);
            tran.setStage(stage);
            tran.setActivityId(activityId);
            tran.setCreateTime(createTime);
            tran.setCreateBy(createBy);
            tran.setId(id);
        }
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean success = clueService.convert(clueId,createBy,tran);
        if (success){
            response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");
        }
    }

    private void searchActivity(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到获取市场活动列表的方法");
        String name = request.getParameter("name");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> activityList = activityService.searchActivity(name);
        PrintJson.printJsonObj(response,activityList);
    }

    private void deleteRelationActivity(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到解除关联方法");
        String id = request.getParameter("id");
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean success = clueService.deleteRelationActivity(id);
        PrintJson.printJsonFlag(response,success);
    }

    private void createBundRelation(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到创建线索与市场活动关联关系方法");
        String clueId = request.getParameter("clueId");
        String[] activityIds = request.getParameterValues("activityId");
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean success = clueService.createBundRelation(clueId,activityIds);
        PrintJson.printJsonFlag(response,success);
    }

    private void getRelationActivityList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到获取已关联市场活动列表方法");
        String clueId = request.getParameter("clueId");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> activityList = activityService.getRelationActivityList(clueId);
        PrintJson.printJsonObj(response,activityList);
    }

    private void showUnbundActivityList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到获取绑定市场活动列表方法");
        String name = request.getParameter("name");
        String clueId = request.getParameter("clueId");
        Map<String,String> map = new HashMap<String, String>();
        map.put("name",name);
        map.put("clueId",clueId);
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> activityList = activityService.showUnbundActivityList(map);
        PrintJson.printJsonObj(response,activityList);
    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到删除线索备注方法");
        String id = request.getParameter("id");
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean success = clueService.deleteRemark(id);
        PrintJson.printJsonFlag(response,success);
    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到修改线索备注方法");
        String noteContent = request.getParameter("noteContent");
        String id = request.getParameter("id");
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();
        String editFlag = "1";
        ClueRemark clueRemark = new ClueRemark();
        clueRemark.setId(id);
        clueRemark.setNoteContent(noteContent);
        clueRemark.setEditBy(editBy);
        clueRemark.setEditFlag(editFlag);
        clueRemark.setEditTime(editTime);
        clueRemark.setEditFlag(editFlag);

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean success = clueService.updateRemark(clueRemark);
        PrintJson.printJsonFlag(response,success);
    }

    private void showRemarkList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到获取线索备注方法");
        String clueId = request.getParameter("clueId");
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        List<ClueRemark> list = clueService.getRemarkList(clueId);
        PrintJson.printJsonObj(response,list);
    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到保存线索备注方法");
        String id = UUIDUtil.getUUID();
        String noteContent = request.getParameter("noteContent");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String editFlag = "0";
        String clueId = request.getParameter("clueId");
        ClueRemark clueRemark = new ClueRemark();
        clueRemark.setId(id);
        clueRemark.setNoteContent(noteContent);
        clueRemark.setCreateBy(createBy);
        clueRemark.setCreateTime(createTime);
        clueRemark.setEditFlag(editFlag);
        clueRemark.setClueId(clueId);
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean success = clueService.saveClueRemark(clueRemark);
        PrintJson.printJsonFlag(response,success);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到查询线索详细信息方法");
        String id = request.getParameter("id");
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue clue = clueService.getDetail(id);
        request.getSession().setAttribute("clue",clue);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request,response);
    }

    private void searchClue(HttpServletRequest request, HttpServletResponse response) {
        String pageNo = request.getParameter("pageNo");
        String pageSize = request.getParameter("pageSize");
        String fullname = request.getParameter("fullname");
        String company = request.getParameter("company");
        String phone = request.getParameter("phone");
        String source = request.getParameter("source");
        String owner = request.getParameter("owner");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String skipNo = String.valueOf((Integer.valueOf(pageNo)-1)*Integer.valueOf(pageSize));
        Integer skipNo1 = Integer.valueOf(skipNo);
        Integer pageSize1 = Integer.valueOf(pageSize);
        Map map = new HashMap();
        map.put("skipNo",skipNo1);
        map.put("pageSize",pageSize1);
        map.put("fullname",fullname);
        map.put("company",company);
        map.put("phone",phone);
        map.put("source",source);
        map.put("owner",owner);
        map.put("mphone",mphone);
        map.put("state",state);

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        PaginationVO<Clue> paginationVO = clueService.getPagination(map);
        PrintJson.printJsonObj(response,paginationVO);
    }

    private void saveClue(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到保存线索方法");
        String id = UUIDUtil.getUUID();
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Clue clue = new Clue();
        clue.setAddress(address);
        clue.setAppellation(appellation);
        clue.setCompany(company);
        clue.setContactSummary(contactSummary);
        clue.setCreateBy(createBy);
        clue.setCreateTime(createTime);
        clue.setDescription(description);
        clue.setWebsite(website);
        clue.setEmail(email);
        clue.setFullname(fullname);
        clue.setId(id);
        clue.setOwner(owner);
        clue.setJob(job);
        clue.setPhone(phone);
        clue.setMphone(mphone);
        clue.setSource(source);
        clue.setState(state);
        clue.setNextContactTime(nextContactTime);

        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = clueService.saveClue(clue);
        PrintJson.printJsonFlag(response,flag);

    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = userService.getUserList();
        PrintJson.printJsonObj(response,userList);
    }

}
package com.blackwings.crm.workbench.web.controller;

import com.blackwings.crm.settings.service.UserService;
import com.blackwings.crm.settings.service.impl.UserServiceImpl;
import com.blackwings.crm.workbench.domain.Activity;
import com.blackwings.crm.workbench.domain.ActivityRemark;
import com.blackwings.crm.workbench.service.ActivityService;
import com.blackwings.crm.workbench.service.impl.ActivityServiceImpl;
import com.blackwings.crm.settings.domain.User;
import com.blackwings.crm.utils.DateTimeUtil;
import com.blackwings.crm.utils.PrintJson;
import com.blackwings.crm.utils.ServiceFactory;
import com.blackwings.crm.utils.UUIDUtil;
import com.blackwings.crm.vo.PaginationVO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if ("/workbench/activity/getUserList.do".equals(servletPath)){
            getUserList(request,response);
        } else if ("/workbench/activity/save.do".equals(servletPath)){
            save(request,response);
        } else if ("/workbench/activity/search.do".equals(servletPath)){
            search(request,response);
        } else if ("/workbench/activity/delete.do".equals(servletPath)){
            delete(request,response);
        } else if ("/workbench/activity/getUListAndActivityDetail.do".equals(servletPath)){
            getUListAndActivityDetail(request,response);
        } else if ("/workbench/activity/edit.do".equals(servletPath)){
            edit(request,response);
        } else if ("/workbench/activity/detail.do".equals(servletPath)){
            detail(request,response);
        } else if ("/workbench/activity/getDetailRemarks.do".equals(servletPath)){
            getDetailRemarks(request,response);
        } else if ("/workbench/activity/editRemark.do".equals(servletPath)){
            editRemark(request,response);
        } else if ("/workbench/activity/saveRemark.do".equals(servletPath)){
            saveRemark(request,response);
        } else if ("/workbench/activity/deleteRemark.do".equals(servletPath)){
            deleteRemark(request,response);
        }
    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到删除备注列表方法");
        String id = request.getParameter("id");

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean success = activityService.deleteRemark(id);
        PrintJson.printJsonFlag(response,success);
    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到保存备注列表方法");
        String id = UUIDUtil.getUUID();
        String noteContent = request.getParameter("noteContent");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "0";
        String activityId = request.getParameter("activityId");
        ActivityRemark activityRemark = new ActivityRemark();
        activityRemark.setId(id);
        activityRemark.setNoteContent(noteContent);
        activityRemark.setCreateTime(createTime);
        activityRemark.setCreateBy(createBy);
        activityRemark.setEditFlag(editFlag);
        activityRemark.setActivityId(activityId);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean success = activityService.saveRemark(activityRemark);
        PrintJson.printJsonFlag(response,success);
    }

    private void editRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到修改备注列表方法");
        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "1";
        ActivityRemark activityRemark = new ActivityRemark();
        activityRemark.setId(id);
        activityRemark.setNoteContent(noteContent);
        activityRemark.setEditTime(editTime);
        activityRemark.setEditBy(editBy);
        activityRemark.setEditFlag(editFlag);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean success = activityService.editRemark(activityRemark);
        PrintJson.printJsonFlag(response,success);
    }

    private void getDetailRemarks(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到获取备注列表方法");
        String id = request.getParameter("id");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<ActivityRemark> activityRemarks= activityService.getDetailRemarks(id);
        PrintJson.printJsonObj(response,activityRemarks);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到跳转详细信息页");
        String id = request.getParameter("id");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity activity = activityService.detail(id);
        request.getSession().setAttribute("activity", activity);
        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request,response);
    }

    private void edit(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到修改市场活动方法");
        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();

        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setEditTime(editTime);
        activity.setEditBy(editBy);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean success = activityService.editActivity(activity);
        PrintJson.printJsonFlag(response,success);
    }

    private void getUListAndActivityDetail(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到修改市场活动详情页");
        String id = request.getParameter("id");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Map map = activityService.getUListAndActivityDetail(id);
        PrintJson.printJsonObj(response,map);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到删除市场活动方法");
        String[] ids = request.getParameterValues("id");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        int num = activityService.delete(ids);
        Map map = new HashMap();
        map.put("num",num);
        PrintJson.printJsonObj(response,map);
    }

    private void search(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到分页方法");
        String pageNo = request.getParameter("pageNo");
        String pageSize = request.getParameter("pageSize");
        String owner = request.getParameter("Owner");
        String name = request.getParameter("Name");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        Map map = new HashMap();
        map.put("pageNo",pageNo);
        map.put("pageSize",pageSize);
        map.put("owner",owner);
        map.put("name",name);
        map.put("startTime",startTime);
        map.put("endTime",endTime);
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        PaginationVO<Activity> pagination = activityService.search(map);
        PrintJson.printJsonObj(response,pagination);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到保存市场活动方法");
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("Owner");
        String name = request.getParameter("Name");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startTime);
        activity.setEndDate(endTime);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean success = activityService.saveActivity(activity);
        PrintJson.printJsonFlag(response,success);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到获取用户列表方法");
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> list = userService.getUserList();
        PrintJson.printJsonObj(response,list);
    }
}

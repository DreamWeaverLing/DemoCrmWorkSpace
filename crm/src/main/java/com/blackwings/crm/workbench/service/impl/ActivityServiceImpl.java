package com.blackwings.crm.workbench.service.impl;

import com.blackwings.crm.workbench.dao.ActivityDao;
import com.blackwings.crm.workbench.dao.ActivityRemarkDao;
import com.blackwings.crm.workbench.domain.Activity;
import com.blackwings.crm.workbench.domain.ActivityRemark;
import com.blackwings.crm.workbench.service.ActivityService;
import com.blackwings.crm.settings.dao.UserDao;
import com.blackwings.crm.settings.domain.User;
import com.blackwings.crm.utils.SqlSessionUtil;
import com.blackwings.crm.vo.PaginationVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {

    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);

    public List<User> getUserList() {
        List<User> list = userDao.getUserList();
        return list;
    }

    public boolean saveActivity(Activity activity) {
        boolean flag = false;
        int count = activityDao.saveActivity(activity);
        if(count==1){
            flag = true;
        }
        return flag;
    }

    public PaginationVO<Activity> search(Map map) {
        int pageNo = Integer.parseInt((String) map.get("pageNo"));
        int pageSize = Integer.parseInt((String) map.get("pageSize"));
        String owner = (String)map.get("owner");
        String name = (String)map.get("name");
        String startTime = (String)map.get("startTime");
        String endTime = (String)map.get("endTime");
        int skipNo = (pageNo - 1) * pageSize;
        Map pageMap = new HashMap();
        pageMap.put("owner",owner);
        pageMap.put("name",name);
        pageMap.put("startTime",startTime);
        pageMap.put("endTime",endTime);
        pageMap.put("skipNo",skipNo);
        pageMap.put("pageSize",pageSize);
        List<Activity> list = activityDao.search(pageMap);
        int total = activityDao.searchTotal(pageMap);
        PaginationVO<Activity> paginationVO = new PaginationVO<Activity>();
        paginationVO.setDataList(list);
        paginationVO.setTotal(total);

        return paginationVO;
    }

    public int delete(String[] ids) {
        int num2 = activityRemarkDao.searchCount(ids);
        int num1 = activityRemarkDao.delete(ids);
        if (num1 == num2){
            int num = activityDao.delete(ids);
            return num;
        }
        return 0;
    }

    public Map getUListAndActivityDetail(String id) {
        List uList = userDao.getUserList();
        Activity activityDetail = activityDao.getActivityDetail(id);
        Map map = new HashMap();
        map.put("uList",uList);
        map.put("activityDetail",activityDetail);
        return map;
    }

    public boolean editActivity(Activity activity) {
        boolean flag = true;
        int count = activityDao.editActivity(activity);
        if (count!=1){
            flag = false;
        }
        return flag;
    }

    public Activity detail(String id) {
        Activity activity = activityDao.detail(id);
        return activity;
    }

    public List<ActivityRemark> getDetailRemarks(String id) {
        List<ActivityRemark> activityRemarks = activityRemarkDao.getDetailRemarks(id);
        return activityRemarks;
    }

    public boolean editRemark(ActivityRemark activityRemark) {
        boolean flag = false;
        int count = activityRemarkDao.editRemark(activityRemark);
        if (count == 1){
            flag = true;
        }
        return flag;
    }

    public boolean saveRemark(ActivityRemark activityRemark) {
        boolean flag = false;
        int count = activityRemarkDao.saveRemark(activityRemark);
        if (count == 1){
            flag = true;
        }
        return flag;
    }

    public boolean deleteRemark(String id) {
        boolean flag = false;
        int count = activityRemarkDao.deleteRemark(id);
        if (count == 1){
            flag = true;
        }
        return flag;
    }

    public List<Activity> getRelationActivityList(String clueId) {
        List<Activity> activityList = activityDao.getRelationActivityList(clueId);
        return activityList;
    }

    public List<Activity> showUnbundActivityList(Map<String, String> map) {
        List<Activity> activityList = activityDao.showUnbundActivityList(map);
        return activityList;
    }

    public List<Activity> searchActivity(String name) {
        List<Activity> activityList = activityDao.searchActivity(name);
        return activityList;
    }
}

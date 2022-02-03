package com.blackwings.crm.workbench.service;

import com.blackwings.crm.workbench.domain.Activity;
import com.blackwings.crm.workbench.domain.ActivityRemark;
import com.blackwings.crm.settings.domain.User;
import com.blackwings.crm.vo.PaginationVO;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    List<User> getUserList();

    boolean saveActivity(Activity activity);

    PaginationVO<Activity> search(Map map);

    int delete(String[] ids);

    Map getUListAndActivityDetail(String id);

    boolean editActivity(Activity activity);

    Activity detail(String id);

    List<ActivityRemark> getDetailRemarks(String id);

    boolean editRemark(ActivityRemark activityRemark);

    boolean saveRemark(ActivityRemark activityRemark);

    boolean deleteRemark(String id);

    List<Activity> getRelationActivityList(String clueId);

    List<Activity> showUnbundActivityList(Map<String, String> map);

    List<Activity> searchActivity(String name);
}

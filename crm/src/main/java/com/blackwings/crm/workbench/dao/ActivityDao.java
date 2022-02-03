package com.blackwings.crm.workbench.dao;


import com.blackwings.crm.workbench.domain.Activity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int saveActivity(Activity activity);

    List<Activity> search(Map pageMap);

    int searchTotal(Map pageMap);

    int delete(String[] ids);

    Activity getActivityDetail(String id);

    int editActivity(Activity activity);

    Activity detail(String id);

    List<Activity> getRelationActivityList(String clueId);

    List<Activity> showUnbundActivityList(Map<String, String> map);

    List<Activity> searchActivity(@Param("name")String name);
}

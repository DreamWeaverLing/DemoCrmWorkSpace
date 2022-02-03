package com.blackwings.crm.workbench.dao;

import com.blackwings.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
    int delete(String[] ids);

    int searchCount(String[] ids);

    List<ActivityRemark> getDetailRemarks(String id);

    int editRemark(ActivityRemark activityRemark);

    int saveRemark(ActivityRemark activityRemark);

    int deleteRemark(String id);
}

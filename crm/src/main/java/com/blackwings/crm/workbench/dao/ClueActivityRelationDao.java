package com.blackwings.crm.workbench.dao;

import com.blackwings.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {
    int createBundRelation(String id, String clueId, String activityId);

    int deleteRelationActivity(String id);

    List<ClueActivityRelation> getRelation(String clueId);
}

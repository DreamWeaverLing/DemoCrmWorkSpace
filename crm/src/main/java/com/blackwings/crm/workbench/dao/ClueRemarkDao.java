package com.blackwings.crm.workbench.dao;

import com.blackwings.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {
    int saveClueRemark(ClueRemark clueRemark);

    List<ClueRemark> getRemarkList(String clueId);

    int updateRemark(ClueRemark clueRemark);

    int deleteRemark(String id);
}

package com.blackwings.crm.workbench.service;

import com.blackwings.crm.vo.PaginationVO;
import com.blackwings.crm.workbench.domain.Clue;
import com.blackwings.crm.workbench.domain.ClueRemark;
import com.blackwings.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface ClueService {
    boolean saveClue(Clue clue);

    PaginationVO<Clue> getPagination(Map map);

    Clue getDetail(String id);

    boolean saveClueRemark(ClueRemark clueRemark);

    List<ClueRemark> getRemarkList(String clueId);

    boolean updateRemark(ClueRemark clueRemark);

    boolean deleteRemark(String id);

    boolean createBundRelation(String clueId,String[] activityIds);

    boolean deleteRelationActivity(String id);

    boolean convert(String clueId, String createBy, Tran tran);
}

package com.blackwings.crm.workbench.dao;

import com.blackwings.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {
    int saveClue(Clue clue);

    int getTotal(Map map);

    List<Clue> getList(Map map);

    Clue getDetail(String id);

    Clue getClue(String clueId);

    int deleteClue(String clueId);
}

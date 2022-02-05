package com.blackwings.crm.workbench.dao;

import com.blackwings.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {
    int save(Tran tran);

    List<Tran> searchTran(Map map);

    int getTotal(Map map);

    Tran detail(String id);

    int changeStage(Tran tran);
}

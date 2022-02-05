package com.blackwings.crm.workbench.service;

import com.blackwings.crm.vo.PaginationVO;
import com.blackwings.crm.workbench.domain.Tran;
import com.blackwings.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranService {
    boolean saveTransaction(Tran tran, String customerName);

    PaginationVO<Tran> searchTran(Map map);

    Tran detail(String id);

    List<TranHistory> getTranHistory(String tranId);
}

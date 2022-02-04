package com.blackwings.crm.workbench.service;

import com.blackwings.crm.vo.PaginationVO;
import com.blackwings.crm.workbench.domain.Tran;

import java.util.Map;

public interface TranService {
    boolean saveTransaction(Tran tran, String customerName);

    PaginationVO<Tran> searchTran(Map map);

    Tran detail(String id);
}

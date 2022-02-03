package com.blackwings.crm.workbench.dao;

import com.blackwings.crm.workbench.domain.CustomerRemark;

import java.util.List;

public interface CustomerRemarkDao {
    int save(List<CustomerRemark> customerRemarkList);
}

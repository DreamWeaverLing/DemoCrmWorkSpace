package com.blackwings.crm.workbench.service.impl;

import com.blackwings.crm.utils.SqlSessionUtil;
import com.blackwings.crm.workbench.dao.CustomerDao;
import com.blackwings.crm.workbench.service.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    public List<String> getCustomerName(String name) {
        List<String> customerNames = customerDao.getCustomerName(name);
        return customerNames;
    }
}

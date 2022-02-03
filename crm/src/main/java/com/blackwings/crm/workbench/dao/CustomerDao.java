package com.blackwings.crm.workbench.dao;

import com.blackwings.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {
    Customer getCompany(String cusname);

    int save(Customer customer);

    List<String> getCustomerName(String name);
}

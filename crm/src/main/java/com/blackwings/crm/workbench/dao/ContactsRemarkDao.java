package com.blackwings.crm.workbench.dao;

import com.blackwings.crm.workbench.domain.ContactsRemark;

import java.util.List;

public interface ContactsRemarkDao {
    int save(List<ContactsRemark> contactsRemarkList);
}

package com.blackwings.crm.workbench.dao;

import com.blackwings.crm.workbench.domain.ContactsActivityRelation;

import java.util.List;

public interface ContactsActivityRelationDao {
    int save(List<ContactsActivityRelation> contactsActivityRelationList);
}

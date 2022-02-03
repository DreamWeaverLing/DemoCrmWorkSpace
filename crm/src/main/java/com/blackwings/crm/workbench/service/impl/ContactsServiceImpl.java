package com.blackwings.crm.workbench.service.impl;

import com.blackwings.crm.utils.SqlSessionUtil;
import com.blackwings.crm.workbench.dao.ContactsDao;
import com.blackwings.crm.workbench.domain.Contacts;
import com.blackwings.crm.workbench.service.ContactsService;

import java.util.List;

public class ContactsServiceImpl implements ContactsService {
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);

    public List<Contacts> searchContacts(String name) {
        List<Contacts> contactsList = contactsDao.searchContacts(name);
        return contactsList;
    }
}

package com.blackwings.crm.workbench.service;

import com.blackwings.crm.workbench.domain.Contacts;

import java.util.List;

public interface ContactsService {
    List<Contacts> searchContacts(String name);
}

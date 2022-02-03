package com.blackwings.crm.workbench.dao;

import com.blackwings.crm.workbench.domain.Contacts;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ContactsDao {
    int save(Contacts contacts);

    List<Contacts> searchContacts(@Param("name") String name);
}

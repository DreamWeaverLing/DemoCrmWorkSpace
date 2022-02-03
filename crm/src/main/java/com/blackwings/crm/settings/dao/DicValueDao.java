package com.blackwings.crm.settings.dao;

import com.blackwings.crm.settings.domain.DicType;
import com.blackwings.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getDicValue(String dicType);
}

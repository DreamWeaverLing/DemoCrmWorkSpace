package com.blackwings.crm.settings.service;

import com.blackwings.crm.settings.domain.DicValue;

import java.util.List;
import java.util.Map;

public interface DicService {
    Map<String, List<DicValue>> getDicLists();
}

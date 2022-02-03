package com.blackwings.crm.settings.service.impl;

import com.blackwings.crm.settings.dao.DicTypeDao;
import com.blackwings.crm.settings.dao.DicValueDao;
import com.blackwings.crm.settings.domain.DicType;
import com.blackwings.crm.settings.domain.DicValue;
import com.blackwings.crm.settings.service.DicService;
import com.blackwings.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {
    DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    public Map<String, List<DicValue>> getDicLists() {
        Map<String,List<DicValue>> map = new HashMap<String, List<DicValue>>();
        List<DicType> list = dicTypeDao.getList();
        for (DicType dicType: list){
            List<DicValue> dicValueList = dicValueDao.getDicValue(dicType.getCode());
            map.put(dicType.getCode(),dicValueList);
        }
        return map;
    }
}

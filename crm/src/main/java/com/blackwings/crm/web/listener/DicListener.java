package com.blackwings.crm.web.listener;

import com.blackwings.crm.settings.domain.DicValue;
import com.blackwings.crm.settings.service.DicService;
import com.blackwings.crm.settings.service.impl.DicServiceImpl;
import com.blackwings.crm.utils.ServiceFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.*;

public class DicListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("进入监听器初始化方法");
        ServletContext application = event.getServletContext();
        DicService dicService = (DicService) ServiceFactory.getService(new DicServiceImpl());
        Map<String, List<DicValue>> map = dicService.getDicLists();
        Set<String> set = map.keySet();
        for (String code: set){
            List<DicValue> list = map.get(code);
            application.setAttribute(code,list);
        }
        System.out.println("数据字典缓存结束");

        System.out.println("进入阶段可能性初始化方法");
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> stage = resourceBundle.getKeys();
        Map<String,String> jsonMap = new HashMap<String, String>();
        ObjectMapper objectMapper = new ObjectMapper();
        while (stage.hasMoreElements()){
            String key = stage.nextElement();
            String value = resourceBundle.getString(key);
            jsonMap.put(key,value);
        }
        String json = null;
        try {
            json = objectMapper.writeValueAsString(jsonMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        application.setAttribute("Stage2Possibility",json);
    }

    public void contextDestroyed(ServletContextEvent event) {

    }
}

package com.blackwings.crm.web.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ListenerTest {
    @Test
    public void test1(){
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
        System.out.println(json);
    }
}

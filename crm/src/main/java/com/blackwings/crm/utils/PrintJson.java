package com.blackwings.crm.utils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PrintJson {
    public static void printJsonFlag(HttpServletResponse response,boolean flag){
        ObjectMapper om = new ObjectMapper();
        Map<String,Boolean> map = new HashMap<String,Boolean>();
        String Json = "";
        map.put("success",flag);
        try {
            Json = om.writeValueAsString(map);
            response.getWriter().print(Json);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printJsonObj(HttpServletResponse response,Object obj){
        ObjectMapper om = new ObjectMapper();
        try {
            String Json = om.writeValueAsString(obj);
            response.getWriter().print(Json);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

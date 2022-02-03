package com.blackwings.crm.workbench.test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ClueServiceTest {
    @Test
    public void test(){
        List list = new ArrayList();
        Object o = new Object();
        list.add(o);
        for (Object o1 : list){
            System.out.println("111");
        }
    }
}

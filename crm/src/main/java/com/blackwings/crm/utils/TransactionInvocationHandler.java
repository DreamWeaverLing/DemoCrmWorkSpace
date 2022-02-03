package com.blackwings.crm.utils;

import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TransactionInvocationHandler implements InvocationHandler {
    private Object target;

    public TransactionInvocationHandler(Object target) {
        this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        SqlSession session = SqlSessionUtil.getSqlSession();
        Object obj = null;
        try {
            obj = method.invoke(target,args);
            session.commit();
        } catch (Exception e){
            e.printStackTrace();
            session.rollback();
            throw e.getCause();
        } finally {
            SqlSessionUtil.myClose(session);
        }
        return obj;
    }

    public Object getProxy(){
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(),this);
    }
}

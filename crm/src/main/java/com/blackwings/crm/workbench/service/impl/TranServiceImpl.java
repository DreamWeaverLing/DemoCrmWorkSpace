package com.blackwings.crm.workbench.service.impl;

import com.blackwings.crm.utils.DateTimeUtil;
import com.blackwings.crm.utils.SqlSessionUtil;
import com.blackwings.crm.utils.UUIDUtil;
import com.blackwings.crm.vo.PaginationVO;
import com.blackwings.crm.workbench.dao.CustomerDao;
import com.blackwings.crm.workbench.dao.TranDao;
import com.blackwings.crm.workbench.dao.TranHistoryDao;
import com.blackwings.crm.workbench.domain.Customer;
import com.blackwings.crm.workbench.domain.Tran;
import com.blackwings.crm.workbench.domain.TranHistory;
import com.blackwings.crm.workbench.service.TranService;

import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    public boolean saveTransaction(Tran tran, String customerName) {
        boolean flag = true;
        // 根据公司名获取公司对象，若不存在则新建
        Customer customer = customerDao.getCompany(customerName);
        if (customer == null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setOwner(tran.getOwner());
            customer.setName(customerName);
            customer.setCreateBy(tran.getCreateBy());
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setContactSummary(tran.getContactSummary());
            customer.setNextContactTime(tran.getNextContactTime());
            customer.setDescription(tran.getDescription());
            int countCus = customerDao.save(customer);
            if (countCus != 1){
                flag = false;
            }
        }
        // 保存交易
        tran.setCustomerId(customer.getId());
        int count = tranDao.save(tran);
        if (count != 1){
            flag = false;
        }

        // 保存交易历史
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setStage(tran.getStage());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setCreateBy(tran.getCreateBy());
        tranHistory.setCreateTime(DateTimeUtil.getSysTime());
        tranHistory.setTranId(tran.getId());
        int count2 = tranHistoryDao.save(tranHistory);
        if(count2 != 1){
            flag = false;
        }
        return flag;
    }

    public PaginationVO<Tran> searchTran(Map map) {
        List<Tran> tranList = tranDao.searchTran(map);
        int total = tranDao.getTotal(map);
        PaginationVO<Tran> paginationVO = new PaginationVO<Tran>();
        paginationVO.setDataList(tranList);
        paginationVO.setTotal(total);
        return paginationVO;
    }

    public Tran detail(String id) {
        Tran tran = tranDao.detail(id);
        return tran;
    }

    public List<TranHistory> getTranHistory(String tranId) {
        List<TranHistory> tranHistoryList = tranHistoryDao.getTranHistory(tranId);
        return tranHistoryList;
    }

    public boolean changeStage(Tran tran) {
        boolean flag = true;
        int count = tranDao.changeStage(tran);
        if (count != 1){
            flag = false;
        }

        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setTranId(tran.getId());
        tranHistory.setStage(tran.getStage());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setCreateBy(tran.getEditBy());
        tranHistory.setCreateTime(DateTimeUtil.getSysTime());
        tranHistory.setMoney(tran.getMoney());
        int count2 = tranHistoryDao.save(tranHistory);
        if (count2 != 1){
            flag = false;
        }
        return flag;
    }

    public List getEChartsData() {
        List list = tranHistoryDao.getEChartsData();
        return list;
    }

}

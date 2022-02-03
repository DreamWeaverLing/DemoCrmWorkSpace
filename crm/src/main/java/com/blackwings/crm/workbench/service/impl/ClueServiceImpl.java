package com.blackwings.crm.workbench.service.impl;

import com.blackwings.crm.utils.DateTimeUtil;
import com.blackwings.crm.utils.SqlSessionUtil;
import com.blackwings.crm.utils.UUIDUtil;
import com.blackwings.crm.vo.PaginationVO;
import com.blackwings.crm.workbench.dao.*;
import com.blackwings.crm.workbench.domain.*;
import com.blackwings.crm.workbench.service.ClueService;
import com.sun.org.apache.bcel.internal.generic.IFNULL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);

    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    public boolean saveClue(Clue clue) {
        boolean flag = false;
        int count = clueDao.saveClue(clue);
        if (count == 1){
            flag = true;
        }
        return flag;
    }

    public PaginationVO<Clue> getPagination(Map map) {
        int total = clueDao.getTotal(map);
        List<Clue> list = clueDao.getList(map);
        PaginationVO<Clue> paginationVO = new PaginationVO<Clue>();
        paginationVO.setTotal(total);
        paginationVO.setDataList(list);
        return paginationVO;
    }

    public Clue getDetail(String id) {
        Clue clue = clueDao.getDetail(id);
        return clue;
    }

    public boolean saveClueRemark(ClueRemark clueRemark) {
        boolean flag = false;
        int count = clueRemarkDao.saveClueRemark(clueRemark);
        if (count == 1){
            flag = true;
        }
        return flag;
    }

    public List<ClueRemark> getRemarkList(String clueId) {
        List<ClueRemark> list = clueRemarkDao.getRemarkList(clueId);
        return list;
    }

    public boolean updateRemark(ClueRemark clueRemark) {
        boolean flag = false;
        int count = clueRemarkDao.updateRemark(clueRemark);
        if (count == 1){
            flag = true;
        }
        return flag;
    }

    public boolean deleteRemark(String id) {
        boolean flag = false;
        int count = clueRemarkDao.deleteRemark(id);
        if (count == 1){
            flag = true;
        }
        return flag;
    }

    public boolean createBundRelation(String clueId,String[] activityIds) {
        boolean flag = false;
        int count = 0;
        int length = activityIds.length;
        for (int i = 0;i < activityIds.length;i++){
            String id = UUIDUtil.getUUID();
            clueActivityRelationDao.createBundRelation(id,clueId,activityIds[i]);
            count++;
        }
        if (count == length){
            flag = true;
        }
        return flag;
    }

    public boolean deleteRelationActivity(String id) {
        boolean flag = false;
        int count = clueActivityRelationDao.deleteRelationActivity(id);
        if (count == 1){
            flag = true;
        }
        return flag;
    }

    public boolean convert(String clueId, String createBy, Tran tran) {
        boolean flag = true;
        String createTime = DateTimeUtil.getSysTime();
        // 由clueId获取clue对象
        Clue clue = clueDao.getClue(clueId);

        // 通过clue来获取customer名称，并判断是否存在，若不存在则新建
        String cusname = clue.getCompany();
        Customer customer = customerDao.getCompany(cusname);
        if (customer == null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setAddress(clue.getAddress());
            customer.setCreateBy(createBy);
            customer.setDescription(clue.getDescription());
            customer.setCreateTime(createTime);
            customer.setContactSummary(clue.getContactSummary());
            customer.setName(cusname);
            customer.setOwner(clue.getOwner());
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setNextContactTime(clue.getNextContactTime());
            int count = customerDao.save(customer);
            if (count != 1){
                flag = false;
            }
        }

        // 新建联系人
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setOwner(clue.getOwner());
        contacts.setSource(clue.getSource());
        contacts.setCustomerId(customer.getId());
        contacts.setFullname(clue.getFullname());
        contacts.setAppellation(clue.getAppellation());
        contacts.setEmail(clue.getEmail());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setCreateBy(createBy);
        contacts.setCreateTime(createTime);
        contacts.setDescription(clue.getDescription());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setAddress(clue.getAddress());

        int count = contactsDao.save(contacts);
        if (count != 1){
            flag = false;
        }

        // 若tran不为空则新建交易
        if (tran != null) {
            tran.setOwner(clue.getOwner());
            tran.setCustomerId(customer.getId());
            tran.setSource(clue.getSource());
            tran.setContactsId(contacts.getId());
            tran.setDescription(clue.getDescription());
            tran.setContactSummary(clue.getContactSummary());
            tran.setNextContactTime(clue.getNextContactTime());
            int countTran = tranDao.save(tran);
            if (countTran != 1){
                flag = false;
            }
            // 新建交易历史
            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setStage(tran.getStage());
            tranHistory.setMoney(tran.getMoney());
            tranHistory.setExpectedDate(tran.getExpectedDate());
            tranHistory.setCreateTime(createTime);
            tranHistory.setCreateBy(createBy);
            tranHistory.setTranId(tran.getId());
            int countTranHistory = tranHistoryDao.save(tranHistory);
            if (countTranHistory != 1){
                flag = false;
            }
        }

        // 获取线索备注
        List<ClueRemark> clueRemarkList = clueRemarkDao.getRemarkList(clueId);
        if (clueRemarkList.size() > 0){
            // 创建客户备注
            List<CustomerRemark> customerRemarkList = new ArrayList<CustomerRemark>();
            for (ClueRemark clueRemark:clueRemarkList){
                CustomerRemark customerRemark = new CustomerRemark();
                customerRemark.setId(UUIDUtil.getUUID());
                customerRemark.setNoteContent(clueRemark.getNoteContent());
                customerRemark.setCreateBy(createBy);
                customerRemark.setCreateTime(createTime);
                customerRemark.setEditFlag("0");
                customerRemark.setCustomerId(customer.getId());
                customerRemarkList.add(customerRemark);
            }
            int countCustomerRemark = customerRemarkDao.save(customerRemarkList);
            if (countCustomerRemark != customerRemarkList.size()){
                flag = false;
            }

            // 创建联系人备注
            List<ContactsRemark> contactsRemarkList = new ArrayList<ContactsRemark>();
            for (ClueRemark clueRemark:clueRemarkList){
                ContactsRemark contactsRemark = new ContactsRemark();
                contactsRemark.setId(UUIDUtil.getUUID());
                contactsRemark.setNoteContent(clueRemark.getNoteContent());
                contactsRemark.setCreateBy(createBy);
                contactsRemark.setCreateTime(createTime);
                contactsRemark.setEditFlag("0");
                contactsRemark.setContactsId(contacts.getId());
                contactsRemarkList.add(contactsRemark);
            }
            int countContactsRemark = contactsRemarkDao.save(contactsRemarkList);
            if (countContactsRemark != contactsRemarkList.size()){
                flag = false;
            }

            // 删除线索备注
            for (ClueRemark clueRemark:clueRemarkList){
                int deleteClueRemarkCount = clueRemarkDao.deleteRemark(clueRemark.getId());
                if (deleteClueRemarkCount != 1){
                    flag = false;
                }
            }
        }

        // 获取线索与市场活动关系
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getRelation(clueId);
        if (clueActivityRelationList.size() > 0){
            // 创建联系人与市场活动关系
            List<ContactsActivityRelation> contactsActivityRelationList = new ArrayList<ContactsActivityRelation>();
            for (ClueActivityRelation clueActivityRelation:clueActivityRelationList){
                ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
                contactsActivityRelation.setId(UUIDUtil.getUUID());
                contactsActivityRelation.setContactsId(contacts.getId());
                contactsActivityRelation.setActivityId(clueActivityRelation.getActivityId());
                contactsActivityRelationList.add(contactsActivityRelation);
            }
            int countContactsActivityRelation = contactsActivityRelationDao.save(contactsActivityRelationList);
            if (countContactsActivityRelation != contactsActivityRelationList.size()){
                flag = false;
            }

            // 删除线索与市场活动关系
            for (ClueActivityRelation clueActivityRelation:clueActivityRelationList){
                int deleteClueActivityRelationCount = clueActivityRelationDao.deleteRelationActivity(clueActivityRelation.getId());
                if (deleteClueActivityRelationCount != 1){
                    flag = false;
                }
            }
        }

        // 删除线索
        int deleteClueCount = clueDao.deleteClue(clueId);
        if (deleteClueCount != 1){
            flag = false;
        }

        return flag;
    }
}

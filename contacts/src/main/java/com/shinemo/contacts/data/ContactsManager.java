package com.shinemo.contacts.data;


import com.shinemo.contacts.data.local.DbContactsManager;
import com.shinemo.contacts.data.notify.PushContacts;
import com.shinemo.contacts.data.remote.ContactsClientWrapper;
import com.shinemo.contacts.db.entity.DepartmentEntity;
import com.shinemo.contacts.db.entity.OrgEntity;
import com.shinemo.contacts.model.CheckNewResult;
import com.shinemo.contacts.model.GetDeptsResult;
import com.shinemo.contacts.model.mapper.ContactsMapper;
import com.shinemo.contacts.protocol.DepartmentVo;
import com.shinemo.contacts.protocol.OrgVo;
import com.shinemo.contacts.utils.ContactHelper;
import com.shinemo.openim.base.thread.ThreadManager;
import com.shinemo.openim.helper.AccountHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.shinemo.openim.base.RetCode.RET_SUCCESS;

public class ContactsManager {

    private static ContactsManager instance = null;
    
    private ContactsManager() {
    }
    
    public static ContactsManager getInstance() {
        if (instance == null) {
            synchronized (ContactsManager.class) {
                if (instance == null) {
                    instance = new ContactsManager();
                }
            }
        }
        new PushContacts();
        return instance;
    }
    private static final int WAIT_TIME = 60;
    
    public void syncContacts() {
        ThreadManager.INSTANCE.getCommonThread().execute(new Runnable() {
            @Override
            public void run() {
                doSync();
            }
        });
    }



    private void doSync(){
        //checkNew
        String mobile = AccountHelper.getInstance().getAccount().getMobile();
        TreeMap<Long, Long> orgCurrentVerMap = ContactHelper.INSTANCE.getOrgVerMap();
        CheckNewResult checkNewResult = ContactsClientWrapper.getInstance().checkNew(mobile, orgCurrentVerMap);
        if(checkNewResult.retCode != RET_SUCCESS) {
            //TODO 发出通知
            return;
        } else if(checkNewResult.orgLastVerMap.isEmpty()){
            //TODO 不属于任何企业
            return;
        }

        //删除不存在企业数据
        Set<Long> realOrgs = checkNewResult.orgLastVerMap.keySet();
        Set<Long> oldOrgs = orgCurrentVerMap.keySet();
        List<Long> unExistIds = new ArrayList<Long>();
        for (Long id : oldOrgs) {
            if (!realOrgs.contains(id)) {
                unExistIds.add(id);
            }
        }
        if(!unExistIds.isEmpty()) {
            DbContactsManager.getInstance().deleteOrgByIds(unExistIds);
            final Map<Long, Long> userVerMap = ContactHelper.INSTANCE.getUserVerMap();
            for (Long delId : unExistIds) {
                orgCurrentVerMap.remove(delId);
                userVerMap.remove(delId);
            }
            ContactHelper.INSTANCE.setUserVerMap(userVerMap);
            ContactHelper.INSTANCE.updateOrgVerMap(orgCurrentVerMap);
        }


        //获取变化的企业
        Set<Long> needUpdateOrgSet = new HashSet<>();
        for (Map.Entry<Long, Long> entry : checkNewResult.orgLastVerMap.entrySet()) {
            if (entry.getValue() != 0 && !entry.getValue().equals(orgCurrentVerMap.get(entry.getKey()))) {
                needUpdateOrgSet.add(entry.getKey());
            }
        }
        List<DepartmentEntity> dbDepts = DbContactsManager.getInstance().queryUnFinishedDept();
        if(dbDepts != null && !dbDepts.isEmpty()) {
            if(needUpdateOrgSet.isEmpty()) {
                //TODO 通知上次未完成同步部门
            } else {
                Set<Long> orgIds = new HashSet<>();
                for (DepartmentEntity department : dbDepts) {
                    orgIds.add(department.getOrgId());
                }
                needUpdateOrgSet.addAll(orgIds);
            }
        }
        if(needUpdateOrgSet.isEmpty()) return;


        //同步通讯录
        CountDownLatch countDownLatch = new CountDownLatch(needUpdateOrgSet.size());
        CopyOnWriteArrayList<Long> successIds = new CopyOnWriteArrayList<>();
        for (Long orgId : needUpdateOrgSet) {
            new ContactWorker(countDownLatch, successIds, orgId).start();
        }
        try {
            countDownLatch.await(WAIT_TIME, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //更新版本号
        if (successIds.size() == 0) {
            //TODO 全部失败
            return;
        } else {
            if (successIds.size() != needUpdateOrgSet.size()) {
                List<Long> unSuccessIds = new ArrayList<>();
                for (Long id : needUpdateOrgSet) {
                    if (!successIds.contains(id)) {
                        unSuccessIds.add(id);
                    }
                }
                for (Long id : unSuccessIds) {
                    checkNewResult.orgLastVerMap.remove(id);
                }
            }
            ContactHelper.INSTANCE.updateOrgVerMap(checkNewResult.orgLastVerMap);
        }
    }


    class ContactWorker extends Thread {
        private CountDownLatch latch;
        private CopyOnWriteArrayList<Long> successIds;
        private long orgId;

        public ContactWorker(CountDownLatch latch, CopyOnWriteArrayList<Long> successIds, long orgId) {
            this.latch = latch;
            this.successIds = successIds;
            this.orgId = orgId;
        }

        @Override
        public void run() {
            try {
                doWork();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        }

        private void doWork() throws Exception {
            OrgVo orgVo = ContactsClientWrapper.getInstance().getOrgInfo(orgId);
            if (orgVo == null) {
                return;
            }
            OrgEntity orgEntity = ContactsMapper.INSTANCE.aceToDb(orgVo);
            DbContactsManager.getInstance().insertOrg(orgEntity);

            String mobile = AccountHelper.getInstance().getAccount().getMobile();
            GetDeptsResult getDeptsResult = ContactsClientWrapper.getInstance().getDepts(mobile, orgId);
            if(getDeptsResult.retCode != RET_SUCCESS) return;
            //TODO 获取所有部门及版本号
            //TODO 删除不存在的部门
            final Map<Long, Long> lastUserVerMap = ContactHelper.INSTANCE.getUserVerMap();
            if (lastUserVerMap.get(orgId) == null || lastUserVerMap.get(orgId) != orgVo.getUserVersion()) {
                lastUserVerMap.put(orgId, orgVo.getUserVersion());
                ContactHelper.INSTANCE.setUserVerMap(lastUserVerMap);
                insertDepartments(orgVo, getDeptsResult.departmentVos, true);
                updateNotify(null);
            } else {
                //TODO 获取变化的部门
//                ArrayList<DepartmentVo> changedDepts = null;
//                insertDepartments(orgVo, getDeptsResult.departmentVos, false);//始终更新部门信息,如排序,名字
//                if (!changedDepts.isEmpty()) {
//                    insertDepartments(orgVo, changedDepts, true);
//                }
                //TODO 判断直接部门版本号及变化的部门
            }


        }

        private void insertDepartments(OrgVo orgVo, List<DepartmentVo> departmentVos, boolean updateVersion) {
            if (departmentVos != null && departmentVos.size() > 0) {
                List<DepartmentEntity> list = new ArrayList<>();
                for (DepartmentVo department : departmentVos) {
                    DepartmentEntity entity = ContactsMapper.INSTANCE.aceToDb(department);
                    entity.setOrgId(orgVo.getId());
                    entity.setOrgName(orgVo.getName());
                    //TODO
//                    if(updateVersion) {
//                        entity.setVersion(-1);//未完成用户的更新
//                    }
                    list.add(entity);
                }
                DbContactsManager.getInstance().insertDepts(list);
            }
        }

        private void updateNotify(ArrayList<Long> changedDeptIds) {
            ContactsClientWrapper.getInstance().notifyUpdateUserByDeptIds(orgId, -1, changedDeptIds);
        }

    }
}

package com.shinemo.contacts.data.remote;


import android.util.Log;

import com.shinemo.contacts.model.CheckNewResult;
import com.shinemo.contacts.model.GetDeptsResult;
import com.shinemo.contacts.protocol.ContactsClient;
import com.shinemo.contacts.protocol.DepartmentVo;
import com.shinemo.contacts.protocol.OrgConfVo;
import com.shinemo.contacts.protocol.OrgVo;
import com.shinemo.openim.base.wrapper.MutableLong;

import java.util.ArrayList;
import java.util.TreeMap;

import static com.shinemo.openim.base.RetCode.RET_SUCCESS;


/**
 * Created by yangqinlin on 16/12/5.
 */
public class ContactsClientWrapper {

    private static final String TAG = ContactsClientWrapper.class.getSimpleName();
    private static ContactsClientWrapper instance = null;

    private ContactsClientWrapper() {
    }

    public static ContactsClientWrapper getInstance() {
        if (instance == null) {
            synchronized (ContactsClientWrapper.class) {
                if (instance == null) {
                    instance = new ContactsClientWrapper();
                }
            }
        }
        return instance;
    }

    public CheckNewResult checkNew(String mobile, TreeMap<Long, Long> orgCurrentVerMap) {
        int appType = 1;//彩云
        TreeMap<Long, Integer> orgUsrCntMap = new TreeMap<>();//废弃不用
        TreeMap<Long, Long> orgLastVerMap = new TreeMap<>();
        TreeMap<Long, Long> orgActiveVerMap = new TreeMap<>();
        TreeMap<Long, OrgConfVo> orgConfMap = new TreeMap<>();
        int retCode = ContactsClient.get().checkNew(mobile, orgCurrentVerMap, appType, orgUsrCntMap, orgLastVerMap, orgActiveVerMap, orgConfMap);

        CheckNewResult checkNewResult = new CheckNewResult();
        checkNewResult.retCode = retCode;
        checkNewResult.orgLastVerMap = orgLastVerMap;
        checkNewResult.orgActiveVerMap = orgActiveVerMap;
        checkNewResult.orgConfMap = orgConfMap;
        return checkNewResult;
    }


    public OrgVo getOrgInfo(long orgId) {
        OrgVo orgVo = new OrgVo();
        ContactsClient.get().getOrgInfo(orgId, orgVo);

        return orgVo;
    }


    public GetDeptsResult getDepts(String mobile, long orgId) {
        ArrayList<DepartmentVo> departmentVos = new ArrayList<>();
        MutableLong mutableLong = new MutableLong();

        int retCode = ContactsClient.get().getDepts(mobile, orgId, departmentVos, mutableLong);

        GetDeptsResult result = new GetDeptsResult();
        result.retCode = retCode;
        if (retCode == RET_SUCCESS) {
            result.departmentVos = departmentVos;
            result.directDeptVersion = mutableLong.get();
        }
        return result;
    }

    public void notifyUpdateUserByDeptIds(long orgId, long orgVersion, ArrayList<Long> deptIds) {
        MutableLong newVersion = new MutableLong();
        int retCode = ContactsClient.get().needOrgDepartments(orgId, orgVersion, deptIds, newVersion);
        if (retCode != RET_SUCCESS) {
            Log.e(TAG, "notifyUpdateUserByDeptIds fail retCode=" + retCode);
        }
    }
}

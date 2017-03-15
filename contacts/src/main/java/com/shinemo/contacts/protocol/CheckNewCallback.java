package com.shinemo.contacts.protocol;


import com.shinemo.openim.base.callback.AaceCallback;
import com.shinemo.openim.base.model.ResponseNode;

import java.util.TreeMap;

public abstract class CheckNewCallback implements AaceCallback {
    public void __process(ResponseNode __rsp) {
        TreeMap< Long,Integer > orgUsrCntMap = new TreeMap< Long,Integer >();
        TreeMap< Long,Long > orgLastVerMap = new TreeMap< Long,Long >();
        TreeMap< Long,Long > orgActiveVerMap = new TreeMap< Long,Long >();
        TreeMap< Long,OrgConfVo > orgConfMap = new TreeMap< Long,OrgConfVo >();
        int __retcode = ContactsClient.__unpackCheckNew(__rsp, orgUsrCntMap, orgLastVerMap, orgActiveVerMap, orgConfMap);
        process(__retcode, orgUsrCntMap, orgLastVerMap, orgActiveVerMap, orgConfMap);
    }
    protected abstract void process(int __retcode, TreeMap< Long,Integer > orgUsrCntMap, TreeMap< Long,Long > orgLastVerMap, TreeMap< Long,Long > orgActiveVerMap, TreeMap< Long,OrgConfVo > orgConfMap);
}


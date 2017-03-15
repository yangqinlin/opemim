package com.shinemo.contacts.protocol;

import com.shinemo.openim.base.callback.AaceCallback;
import com.shinemo.openim.base.model.ResponseNode;
import com.shinemo.openim.base.wrapper.MutableLong;

import java.util.ArrayList;

public abstract class CheckUserFrequentOrgDepartmentCallback implements AaceCallback {
    public void __process(ResponseNode __rsp) {
        ArrayList< UserOrgDepartment > datas = new ArrayList< UserOrgDepartment >();
        MutableLong newVersion = new MutableLong();
        int __retcode = ContactsClient.__unpackCheckUserFrequentOrgDepartment(__rsp, datas, newVersion);
        process(__retcode, datas, newVersion.get());
    }
    protected abstract void process(int __retcode, ArrayList< UserOrgDepartment > datas, long newVersion);
}


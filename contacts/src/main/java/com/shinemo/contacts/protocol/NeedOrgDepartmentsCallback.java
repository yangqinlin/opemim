package com.shinemo.contacts.protocol;

import com.shinemo.openim.base.callback.AaceCallback;
import com.shinemo.openim.base.model.ResponseNode;
import com.shinemo.openim.base.wrapper.MutableLong;

public abstract class NeedOrgDepartmentsCallback implements AaceCallback {
    public void __process(ResponseNode __rsp) {
        MutableLong newVersion = new MutableLong();
        int __retcode = ContactsClient.__unpackNeedOrgDepartments(__rsp, newVersion);
        process(__retcode, newVersion.get());
    }
    protected abstract void process(int __retcode, long newVersion);
}


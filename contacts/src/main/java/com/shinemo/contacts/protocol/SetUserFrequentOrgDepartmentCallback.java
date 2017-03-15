package com.shinemo.contacts.protocol;


import com.shinemo.openim.base.callback.AaceCallback;
import com.shinemo.openim.base.model.ResponseNode;
import com.shinemo.openim.base.wrapper.MutableBoolean;
import com.shinemo.openim.base.wrapper.MutableLong;

public abstract class SetUserFrequentOrgDepartmentCallback implements AaceCallback {
    public void __process(ResponseNode __rsp) {
        MutableLong newVersion = new MutableLong();
        MutableBoolean errVer = new MutableBoolean(false);
        int __retcode = ContactsClient.__unpackSetUserFrequentOrgDepartment(__rsp, newVersion, errVer);
        process(__retcode, newVersion.get(), errVer.get());
    }
    protected abstract void process(int __retcode, long newVersion, boolean errVer);
}


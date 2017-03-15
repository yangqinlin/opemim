package com.shinemo.contacts.protocol;

import com.shinemo.openim.base.callback.AaceCallback;
import com.shinemo.openim.base.model.ResponseNode;
import com.shinemo.openim.base.wrapper.MutableBoolean;
import com.shinemo.openim.base.wrapper.MutableLong;

public abstract class ChangeUserFrequentOrgDepartmentCallback implements AaceCallback {
    public void __process(ResponseNode __rsp) {
        MutableLong newVersion = new MutableLong();
        MutableBoolean errVer = new MutableBoolean();
        int __retcode = ContactsClient.__unpackChangeUserFrequentOrgDepartment(__rsp, newVersion, errVer);
        process(__retcode, newVersion.get(), errVer.get());
    }
    protected abstract void process(int __retcode, long newVersion, boolean errVer);
}


package com.shinemo.contacts.protocol;


import com.shinemo.openim.base.callback.AaceCallback;
import com.shinemo.openim.base.model.ResponseNode;
import com.shinemo.openim.base.wrapper.MutableLong;

import java.util.ArrayList;

public abstract class GetDeptsCallback implements AaceCallback {
    public void __process(ResponseNode __rsp) {
        ArrayList< DepartmentVo > depts = new ArrayList< DepartmentVo >();
        MutableLong directDepartmentVersion = new MutableLong(0);
        int __retcode = ContactsClient.__unpackGetDepts(__rsp, depts, directDepartmentVersion);
        process(__retcode, depts, directDepartmentVersion.get());
    }
    protected abstract void process(int __retcode, ArrayList< DepartmentVo > depts, long directDepartmentVersion);
}


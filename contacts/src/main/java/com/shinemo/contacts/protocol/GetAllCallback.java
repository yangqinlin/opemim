package com.shinemo.contacts.protocol;


import com.shinemo.openim.base.callback.AaceCallback;
import com.shinemo.openim.base.model.ResponseNode;

public abstract class GetAllCallback implements AaceCallback {
    public void __process(ResponseNode __rsp) {
        OrgVo orgVo = new OrgVo();
        int __retcode = ContactsClient.__unpackGetAll(__rsp, orgVo);
        process(__retcode, orgVo);
    }
    protected abstract void process(int __retcode, OrgVo orgVo);
}


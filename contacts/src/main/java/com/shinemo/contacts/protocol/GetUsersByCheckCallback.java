package com.shinemo.contacts.protocol;

import com.shinemo.openim.base.callback.AaceCallback;
import com.shinemo.openim.base.model.ResponseNode;

import java.util.ArrayList;

public abstract class GetUsersByCheckCallback implements AaceCallback {
    public void __process(ResponseNode __rsp) {
        ArrayList< OrgUsers > userDatas = new ArrayList< OrgUsers >();
        int __retcode = ContactsClient.__unpackGetUsersByCheck(__rsp, userDatas);
        process(__retcode, userDatas);
    }
    protected abstract void process(int __retcode, ArrayList< OrgUsers > userDatas);
}


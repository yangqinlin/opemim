package com.shinemo.contacts.protocol;


import com.shinemo.openim.base.callback.AaceCallback;
import com.shinemo.openim.base.model.ResponseNode;
import com.shinemo.openim.base.wrapper.MutableLong;

import java.util.ArrayList;

public abstract class GetOrgActiveUserCallback implements AaceCallback {
    public void __process(ResponseNode __rsp) {
        ArrayList< String > uids = new ArrayList< String >();
        MutableLong newVersion = new MutableLong();
        int __retcode = ContactsClient.__unpackGetOrgActiveUser(__rsp, uids, newVersion);
        process(__retcode, uids, newVersion.get());
    }
    protected abstract void process(int __retcode, ArrayList< String > uids, long newVersion);
}


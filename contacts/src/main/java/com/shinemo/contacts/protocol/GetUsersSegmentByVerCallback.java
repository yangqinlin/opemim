package com.shinemo.contacts.protocol;


import com.shinemo.openim.base.callback.AaceCallback;
import com.shinemo.openim.base.model.ResponseNode;
import com.shinemo.openim.base.wrapper.MutableBoolean;

import java.util.ArrayList;

public abstract class GetUsersSegmentByVerCallback implements AaceCallback {
    public void __process(ResponseNode __rsp) {
        ArrayList< User > users = new ArrayList< User >();
        MutableBoolean bEnd = new MutableBoolean();
        int __retcode = ContactsClient.__unpackGetUsersSegmentByVer(__rsp, users, bEnd);
        process(__retcode, users, bEnd.get());
    }
    protected abstract void process(int __retcode, ArrayList< User > users, boolean bEnd);
}


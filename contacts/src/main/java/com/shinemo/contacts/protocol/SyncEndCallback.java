package com.shinemo.contacts.protocol;


import com.shinemo.openim.base.callback.AaceCallback;
import com.shinemo.openim.base.model.ResponseNode;

public abstract class SyncEndCallback implements AaceCallback {
    public void __process(ResponseNode __rsp) {
        int __retcode = ContactsClient.__unpackSyncEnd(__rsp);
        process(__retcode);
    }
    protected abstract void process(int __retcode);
}


package com.shinemo.contacts.protocol;


import com.shinemo.openim.base.callback.AaceCallback;
import com.shinemo.openim.base.model.ResponseNode;
import com.shinemo.openim.base.wrapper.MutableString;

public abstract class GetAvatarPrefixCallback implements AaceCallback {
    public void __process(ResponseNode __rsp) {
        MutableString avatarPrefix = new MutableString();
        int __retcode = ContactsClient.__unpackGetAvatarPrefix(__rsp, avatarPrefix);
        process(__retcode, avatarPrefix.get());
    }
    protected abstract void process(int __retcode, String avatarPrefix);
}


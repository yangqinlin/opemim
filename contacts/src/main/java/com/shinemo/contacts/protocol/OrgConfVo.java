package com.shinemo.contacts.protocol;


import com.shinemo.openim.base.packer.FieldType;
import com.shinemo.openim.base.packer.PackData;
import com.shinemo.openim.base.packer.PackException;
import com.shinemo.openim.base.packer.PackStruct;

import java.util.ArrayList;

public class OrgConfVo implements PackStruct {
    protected int contactsMask_;


    public static ArrayList<String> names() {
        ArrayList<String> result = new ArrayList<String>(1);
        result.add("contactsMask");
        return result;
    }
    public int getContactsMask() {
        return this.contactsMask_;
    }
    public void setContactsMask ( int val) {
        this.contactsMask_ = val;
    }

    public int size() {
        int __size = 2;
        __size += PackData.getSize(contactsMask_);
        return __size;
    }
    public void packData(PackData packer) {
        byte __fieldNum = 1;
        packer.packByte(__fieldNum);
        packer.packByte(PackData.FT_NUMBER);
        packer.packInt(contactsMask_);
    }
    public void unpackData(PackData packer) throws PackException {
        byte __num = packer.unpackByte();
        FieldType __field;
        if(__num < 1) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_NUMBER)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        contactsMask_ = packer.unpackInt();
        for(int i = 1; i < __num; i++)
            packer.peekField();
    }
}


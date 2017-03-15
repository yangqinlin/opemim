package com.shinemo.contacts.protocol;


import com.shinemo.openim.base.packer.FieldType;
import com.shinemo.openim.base.packer.PackData;
import com.shinemo.openim.base.packer.PackException;
import com.shinemo.openim.base.packer.PackStruct;

import java.util.ArrayList;

public class DepartmentVo implements PackStruct {
    protected long id_;
    protected String name_;
    protected String descript_;
    protected long parentId_;
    protected int sequence_;
    protected String parentIds_;
    protected long userCounts_;
    protected long version_ = 0;


    public static ArrayList<String> names() {
        ArrayList<String> result = new ArrayList<String>(8);
        result.add("id");
        result.add("name");
        result.add("descript");
        result.add("parent_id");
        result.add("sequence");
        result.add("parent_ids");
        result.add("user_counts");
        result.add("version");
        return result;
    }
    public long getId() {
        return this.id_;
    }
    public void setId ( long val) {
        this.id_ = val;
    }
    public String getName() {
        return this.name_;
    }
    public void setName ( String val) {
        this.name_ = val;
    }
    public String getDescript() {
        return this.descript_;
    }
    public void setDescript ( String val) {
        this.descript_ = val;
    }
    public long getParentId() {
        return this.parentId_;
    }
    public void setParentId ( long val) {
        this.parentId_ = val;
    }
    public int getSequence() {
        return this.sequence_;
    }
    public void setSequence ( int val) {
        this.sequence_ = val;
    }
    public String getParentIds() {
        return this.parentIds_;
    }
    public void setParentIds ( String val) {
        this.parentIds_ = val;
    }
    public long getUserCounts() {
        return this.userCounts_;
    }
    public void setUserCounts ( long val) {
        this.userCounts_ = val;
    }
    public long getVersion() {
        return this.version_;
    }
    public void setVersion ( long val) {
        this.version_ = val;
    }

    public int size() {
        byte __fieldNum = 8;
        do {
            if(version_ == 0)
                __fieldNum--;
            else
                break;
        } while(false);
        int __size = 8;
        __size += PackData.getSize(id_);
        __size += PackData.getSize(name_);
        __size += PackData.getSize(descript_);
        __size += PackData.getSize(parentId_);
        __size += PackData.getSize(sequence_);
        __size += PackData.getSize(parentIds_);
        __size += PackData.getSize(userCounts_);
        do {
            if(__fieldNum == 7) break;
            __size += 1;
            __size += PackData.getSize(version_);
        } while(false);
        return __size;
    }
    public void packData(PackData packer) {
        byte __fieldNum = 8;
        do {
            if(version_ == 0)
                __fieldNum--;
            else
                break;
        } while(false);
        packer.packByte(__fieldNum);
        do {
            packer.packByte(PackData.FT_NUMBER);
            packer.packLong(id_);
            packer.packByte(PackData.FT_STRING);
            packer.packString(name_);
            packer.packByte(PackData.FT_STRING);
            packer.packString(descript_);
            packer.packByte(PackData.FT_NUMBER);
            packer.packLong(parentId_);
            packer.packByte(PackData.FT_NUMBER);
            packer.packInt(sequence_);
            packer.packByte(PackData.FT_STRING);
            packer.packString(parentIds_);
            packer.packByte(PackData.FT_NUMBER);
            packer.packLong(userCounts_);
            if(__fieldNum == 7) break;
            packer.packByte(PackData.FT_NUMBER);
            packer.packLong(version_);
        } while(false);
    }
    public void unpackData(PackData packer) throws PackException {
        byte __num = packer.unpackByte();
        FieldType __field;
        if(__num < 7) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_NUMBER)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        id_ = packer.unpackLong();
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_STRING)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        name_ = packer.unpackString();
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_STRING)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        descript_ = packer.unpackString();
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_NUMBER)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        parentId_ = packer.unpackLong();
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_NUMBER)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        sequence_ = packer.unpackInt();
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_STRING)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        parentIds_ = packer.unpackString();
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_NUMBER)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        userCounts_ = packer.unpackLong();
        do {
            if(__num < 8) break;
            __field = packer.unpackFieldType();
            if(!PackData.matchType(__field.baseType_, PackData.FT_NUMBER)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
            version_ = packer.unpackLong();
        } while(false);
        for(int i = 8; i < __num; i++)
            packer.peekField();
    }
}


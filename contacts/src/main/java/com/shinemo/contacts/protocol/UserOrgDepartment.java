package com.shinemo.contacts.protocol;

import com.shinemo.openim.base.packer.FieldType;
import com.shinemo.openim.base.packer.PackData;
import com.shinemo.openim.base.packer.PackException;
import com.shinemo.openim.base.packer.PackStruct;

import java.util.ArrayList;


public class UserOrgDepartment implements PackStruct {
    protected long orgId_;
    protected ArrayList< Long > departments_;


    public static ArrayList<String> names() {
        ArrayList<String> result = new ArrayList<String>(2);
        result.add("orgId");
        result.add("departments");
        return result;
    }
    public long getOrgId() {
        return this.orgId_;
    }
    public void setOrgId ( long val) {
        this.orgId_ = val;
    }
    public ArrayList< Long > getDepartments() {
        return this.departments_;
    }
    public void setDepartments ( ArrayList< Long > val) {
        this.departments_ = val;
    }

    public int size() {
        int __size = 4;
        __size += PackData.getSize(orgId_);
        if(departments_ == null)
            __size++;
        else {
            __size += PackData.getSize(departments_.size());
            for(int i = 0; i < departments_.size(); i++) {
                __size += PackData.getSize((departments_.get(i)));
            }
        }
        return __size;
    }
    public void packData(PackData packer) {
        byte __fieldNum = 2;
        packer.packByte(__fieldNum);
        packer.packByte(PackData.FT_NUMBER);
        packer.packLong(orgId_);
        packer.packByte(PackData.FT_VECTOR);
        packer.packByte(PackData.FT_NUMBER);
        if(departments_ == null)
            packer.packByte((byte)0);
        else {
            int len = departments_.size();
            packer.packInt(len);
            for(int i = 0; i < departments_.size(); i++) {
                packer.packLong((departments_.get(i)).longValue());
            }
        }
    }
    public void unpackData(PackData packer) throws PackException {
        byte __num = packer.unpackByte();
        FieldType __field;
        if(__num < 2) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_NUMBER)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        orgId_ = packer.unpackLong();
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_VECTOR)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        {
            int size = packer.unpackInt();
            if(size > PackData.MAX_RECORD_SIZE || size < 0) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
            if(size > 0) departments_ = new ArrayList< Long >(size);
            for(int i = 0; i < size; i++) {
                Long tmpVal = null;
                tmpVal = new Long(packer.unpackLong());
                departments_.add(tmpVal);
            }
        }
        for(int i = 2; i < __num; i++)
            packer.peekField();
    }
}


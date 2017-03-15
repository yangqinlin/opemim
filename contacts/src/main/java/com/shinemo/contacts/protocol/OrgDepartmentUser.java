package com.shinemo.contacts.protocol;


import com.shinemo.openim.base.packer.FieldType;
import com.shinemo.openim.base.packer.PackData;
import com.shinemo.openim.base.packer.PackException;
import com.shinemo.openim.base.packer.PackStruct;

import java.util.ArrayList;

public class OrgDepartmentUser implements PackStruct {
    protected long id_;
    protected long version_;
    protected ArrayList< User > users_;


    public static ArrayList<String> names() {
        ArrayList<String> result = new ArrayList<String>(3);
        result.add("id");
        result.add("version");
        result.add("users");
        return result;
    }
    public long getId() {
        return this.id_;
    }
    public void setId ( long val) {
        this.id_ = val;
    }
    public long getVersion() {
        return this.version_;
    }
    public void setVersion ( long val) {
        this.version_ = val;
    }
    public ArrayList< User > getUsers() {
        return this.users_;
    }
    public void setUsers ( ArrayList< User > val) {
        this.users_ = val;
    }

    public int size() {
        int __size = 5;
        __size += PackData.getSize(id_);
        __size += PackData.getSize(version_);
        if(users_ == null)
            __size++;
        else {
            __size += PackData.getSize(users_.size());
            for(int i = 0; i < users_.size(); i++) {
                __size += (users_.get(i)).size();
            }
        }
        return __size;
    }
    public void packData(PackData packer) {
        byte __fieldNum = 3;
        packer.packByte(__fieldNum);
        packer.packByte(PackData.FT_NUMBER);
        packer.packLong(id_);
        packer.packByte(PackData.FT_NUMBER);
        packer.packLong(version_);
        packer.packByte(PackData.FT_VECTOR);
        packer.packByte(PackData.FT_STRUCT);
        if(users_ == null)
            packer.packByte((byte)0);
        else {
            int len = users_.size();
            packer.packInt(len);
            for(int i = 0; i < users_.size(); i++) {
                (users_.get(i)).packData(packer);
            }
        }
    }
    public void unpackData(PackData packer) throws PackException {
        byte __num = packer.unpackByte();
        FieldType __field;
        if(__num < 3) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_NUMBER)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        id_ = packer.unpackLong();
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_NUMBER)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        version_ = packer.unpackLong();
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_VECTOR)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        {
            int size = packer.unpackInt();
            if(size > PackData.MAX_RECORD_SIZE || size < 0) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
            if(size > 0) users_ = new ArrayList< User >(size);
            for(int i = 0; i < size; i++) {
                User tmpVal = null;
                if(tmpVal == null) tmpVal = new User();
                tmpVal.unpackData(packer);
                users_.add(tmpVal);
            }
        }
        for(int i = 3; i < __num; i++)
            packer.peekField();
    }
}


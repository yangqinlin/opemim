package com.shinemo.contacts.protocol;

import com.shinemo.openim.base.packer.FieldType;
import com.shinemo.openim.base.packer.PackData;
import com.shinemo.openim.base.packer.PackException;
import com.shinemo.openim.base.packer.PackStruct;

import java.util.ArrayList;

public class OrgUserIndexs implements PackStruct {
    protected long id_;
    protected ArrayList< Long > userIndexs_;


    public static ArrayList<String> names() {
        ArrayList<String> result = new ArrayList<String>(2);
        result.add("id");
        result.add("userIndexs");
        return result;
    }
    public long getId() {
        return this.id_;
    }
    public void setId ( long val) {
        this.id_ = val;
    }
    public ArrayList< Long > getUserIndexs() {
        return this.userIndexs_;
    }
    public void setUserIndexs ( ArrayList< Long > val) {
        this.userIndexs_ = val;
    }

    public int size() {
        int __size = 4;
        __size += PackData.getSize(id_);
        if(userIndexs_ == null)
            __size++;
        else {
            __size += PackData.getSize(userIndexs_.size());
            for(int i = 0; i < userIndexs_.size(); i++) {
                __size += PackData.getSize((userIndexs_.get(i)));
            }
        }
        return __size;
    }
    public void packData(PackData packer) {
        byte __fieldNum = 2;
        packer.packByte(__fieldNum);
        packer.packByte(PackData.FT_NUMBER);
        packer.packLong(id_);
        packer.packByte(PackData.FT_VECTOR);
        packer.packByte(PackData.FT_NUMBER);
        if(userIndexs_ == null)
            packer.packByte((byte)0);
        else {
            int len = userIndexs_.size();
            packer.packInt(len);
            for(int i = 0; i < userIndexs_.size(); i++) {
                packer.packLong((userIndexs_.get(i)).longValue());
            }
        }
    }
    public void unpackData(PackData packer) throws PackException {
        byte __num = packer.unpackByte();
        FieldType __field;
        if(__num < 2) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_NUMBER)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        id_ = packer.unpackLong();
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_VECTOR)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        {
            int size = packer.unpackInt();
            if(size > PackData.MAX_RECORD_SIZE || size < 0) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
            if(size > 0) userIndexs_ = new ArrayList< Long >(size);
            for(int i = 0; i < size; i++) {
                Long tmpVal = null;
                tmpVal = new Long(packer.unpackLong());
                userIndexs_.add(tmpVal);
            }
        }
        for(int i = 2; i < __num; i++)
            packer.peekField();
    }
}


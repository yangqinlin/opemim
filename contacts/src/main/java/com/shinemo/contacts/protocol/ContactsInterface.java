package com.shinemo.contacts.protocol;

import com.shinemo.openim.base.AaceMgr;
import com.shinemo.openim.base.RetCode;
import com.shinemo.openim.base.handler.AaceHandler;
import com.shinemo.openim.base.packer.FieldType;
import com.shinemo.openim.base.packer.PackData;
import com.shinemo.openim.base.packer.PackException;

import java.util.ArrayList;

public abstract class ContactsInterface extends AaceHandler {
    public ContactsInterface() {
        super();
    }
    protected boolean registerHandler() {
        if(!aaceMgr_.registerHandler("Contacts", "notifyOrgUsers", this, "__notifyOrgUsers", AaceMgr.INTER_SERVER)) return false;
        if(!aaceMgr_.registerHandler("Contacts", "notifyOrg", this, "__notifyOrg", AaceMgr.INTER_SERVER)) return false;
        return true;
    }
    protected abstract void notifyOrgUsers(long orgId, long version, ArrayList< OrgDepartmentUser > datas);
    protected abstract void notifyOrg(long orgId, long vestion);
    public int __notifyOrgUsers(byte[] __message) {
        long orgId;
        long version;
        ArrayList< OrgDepartmentUser > datas = null;
        PackData __packer = new PackData();
        __packer.resetInBuff(__message);
        try { 
            byte __num = __packer.unpackByte();
            FieldType __field;
            if(__num < 3) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
            __field = __packer.unpackFieldType();
            if(!PackData.matchType(__field.baseType_, PackData.FT_NUMBER)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
            orgId = __packer.unpackLong();
            __field = __packer.unpackFieldType();
            if(!PackData.matchType(__field.baseType_, PackData.FT_NUMBER)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
            version = __packer.unpackLong();
            __field = __packer.unpackFieldType();
            if(!PackData.matchType(__field.baseType_, PackData.FT_VECTOR)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
            {
                int size = __packer.unpackInt();
                if(size > PackData.MAX_RECORD_SIZE || size < 0) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
                if(size > 0) datas = new ArrayList< OrgDepartmentUser >(size);
                for(int i = 0; i < size; i++) {
                    OrgDepartmentUser tmpVal = null;
                    if(tmpVal == null) tmpVal = new OrgDepartmentUser();
                    tmpVal.unpackData(__packer);
                    datas.add(tmpVal);
                }
            }
        } catch (PackException e) {
            return PackData.PACK_INVALID;
        }
        notifyOrgUsers(orgId, version, datas);
        return RetCode.RET_NORESPONSE;
    }
    public int __notifyOrg(byte[] __message) {
        long orgId;
        long vestion;
        PackData __packer = new PackData();
        __packer.resetInBuff(__message);
        try { 
            byte __num = __packer.unpackByte();
            FieldType __field;
            if(__num < 2) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
            __field = __packer.unpackFieldType();
            if(!PackData.matchType(__field.baseType_, PackData.FT_NUMBER)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
            orgId = __packer.unpackLong();
            __field = __packer.unpackFieldType();
            if(!PackData.matchType(__field.baseType_, PackData.FT_NUMBER)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
            vestion = __packer.unpackLong();
        } catch (PackException e) {
            return PackData.PACK_INVALID;
        }
        notifyOrg(orgId, vestion);
        return RetCode.RET_NORESPONSE;
    }
}


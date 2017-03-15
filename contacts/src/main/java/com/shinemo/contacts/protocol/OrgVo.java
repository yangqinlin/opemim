package com.shinemo.contacts.protocol;


import com.shinemo.openim.base.packer.FieldType;
import com.shinemo.openim.base.packer.PackData;
import com.shinemo.openim.base.packer.PackException;
import com.shinemo.openim.base.packer.PackStruct;

import java.util.ArrayList;

public class OrgVo implements PackStruct {
    protected long id_;
    protected String name_;
    protected String customerManager_;
    protected String customerManagerPhone_;
    protected String avatarPrefix_;
    protected ArrayList< DepartmentVo > departments_;
    protected ArrayList< User > users_;
    protected long userVersion_ = -1;
    protected long userType_ = -1;
    protected boolean isAuth_ = true;


    public static ArrayList<String> names() {
        ArrayList<String> result = new ArrayList<String>(10);
        result.add("id");
        result.add("name");
        result.add("customer_manager");
        result.add("customer_manager_phone");
        result.add("avatarPrefix");
        result.add("departments");
        result.add("users");
        result.add("user_version");
        result.add("user_type");
        result.add("is_auth");
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
    public String getCustomerManager() {
        return this.customerManager_;
    }
    public void setCustomerManager ( String val) {
        this.customerManager_ = val;
    }
    public String getCustomerManagerPhone() {
        return this.customerManagerPhone_;
    }
    public void setCustomerManagerPhone ( String val) {
        this.customerManagerPhone_ = val;
    }
    public String getAvatarPrefix() {
        return this.avatarPrefix_;
    }
    public void setAvatarPrefix ( String val) {
        this.avatarPrefix_ = val;
    }
    public ArrayList< DepartmentVo > getDepartments() {
        return this.departments_;
    }
    public void setDepartments ( ArrayList< DepartmentVo > val) {
        this.departments_ = val;
    }
    public ArrayList< User > getUsers() {
        return this.users_;
    }
    public void setUsers ( ArrayList< User > val) {
        this.users_ = val;
    }
    public long getUserVersion() {
        return this.userVersion_;
    }
    public void setUserVersion ( long val) {
        this.userVersion_ = val;
    }
    public long getUserType() {
        return this.userType_;
    }
    public void setUserType ( long val) {
        this.userType_ = val;
    }
    public boolean getIsAuth() {
        return this.isAuth_;
    }
    public void setIsAuth ( boolean val) {
        this.isAuth_ = val;
    }

    public int size() {
        byte __fieldNum = 10;
        do {
            if(isAuth_ == true)
                __fieldNum--;
            else
                break;
            if(userType_ == -1)
                __fieldNum--;
            else
                break;
            if(userVersion_ == -1)
                __fieldNum--;
            else
                break;
        } while(false);
        int __size = 10;
        __size += PackData.getSize(id_);
        __size += PackData.getSize(name_);
        __size += PackData.getSize(customerManager_);
        __size += PackData.getSize(customerManagerPhone_);
        __size += PackData.getSize(avatarPrefix_);
        if(departments_ == null)
            __size++;
        else {
            __size += PackData.getSize(departments_.size());
            for(int i = 0; i < departments_.size(); i++) {
                __size += (departments_.get(i)).size();
            }
        }
        if(users_ == null)
            __size++;
        else {
            __size += PackData.getSize(users_.size());
            for(int i = 0; i < users_.size(); i++) {
                __size += (users_.get(i)).size();
            }
        }
        do {
            if(__fieldNum == 7) break;
            __size += 1;
            __size += PackData.getSize(userVersion_);
            if(__fieldNum == 8) break;
            __size += 1;
            __size += PackData.getSize(userType_);
            if(__fieldNum == 9) break;
            __size += 2;
        } while(false);
        return __size;
    }
    public void packData(PackData packer) {
        byte __fieldNum = 10;
        do {
            if(isAuth_ == true)
                __fieldNum--;
            else
                break;
            if(userType_ == -1)
                __fieldNum--;
            else
                break;
            if(userVersion_ == -1)
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
            packer.packString(customerManager_);
            packer.packByte(PackData.FT_STRING);
            packer.packString(customerManagerPhone_);
            packer.packByte(PackData.FT_STRING);
            packer.packString(avatarPrefix_);
            packer.packByte(PackData.FT_VECTOR);
            packer.packByte(PackData.FT_STRUCT);
            if(departments_ == null)
                packer.packByte((byte)0);
            else {
                int len = departments_.size();
                packer.packInt(len);
                for(int i = 0; i < departments_.size(); i++) {
                    (departments_.get(i)).packData(packer);
                }
            }
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
            if(__fieldNum == 7) break;
            packer.packByte(PackData.FT_NUMBER);
            packer.packLong(userVersion_);
            if(__fieldNum == 8) break;
            packer.packByte(PackData.FT_NUMBER);
            packer.packLong(userType_);
            if(__fieldNum == 9) break;
            packer.packByte(PackData.FT_CHAR);
            packer.packBool(isAuth_);
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
        customerManager_ = packer.unpackString();
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_STRING)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        customerManagerPhone_ = packer.unpackString();
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_STRING)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        avatarPrefix_ = packer.unpackString();
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_VECTOR)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        {
            int size = packer.unpackInt();
            if(size > PackData.MAX_RECORD_SIZE || size < 0) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
            if(size > 0) departments_ = new ArrayList< DepartmentVo >(size);
            for(int i = 0; i < size; i++) {
                DepartmentVo tmpVal = null;
                if(tmpVal == null) tmpVal = new DepartmentVo();
                tmpVal.unpackData(packer);
                departments_.add(tmpVal);
            }
        }
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
        do {
            if(__num < 8) break;
            __field = packer.unpackFieldType();
            if(!PackData.matchType(__field.baseType_, PackData.FT_NUMBER)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
            userVersion_ = packer.unpackLong();
            if(__num < 9) break;
            __field = packer.unpackFieldType();
            if(!PackData.matchType(__field.baseType_, PackData.FT_NUMBER)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
            userType_ = packer.unpackLong();
            if(__num < 10) break;
            __field = packer.unpackFieldType();
            if(!PackData.matchType(__field.baseType_, PackData.FT_CHAR)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
            isAuth_ = packer.unpackBool();
        } while(false);
        for(int i = 10; i < __num; i++)
            packer.peekField();
    }
}


package com.shinemo.contacts.protocol;


import com.shinemo.openim.base.packer.FieldType;
import com.shinemo.openim.base.packer.PackData;
import com.shinemo.openim.base.packer.PackException;
import com.shinemo.openim.base.packer.PackStruct;

import java.util.ArrayList;

public class User implements PackStruct {
    protected long uid_;
    protected long departmentId_;
    protected int sequence_;
    protected String mobile_ = "";
    protected String title_ = "";
    protected String name_ = "";
    protected String pinyin_ = "";
    protected byte sex_;
    protected String email_ = "";
    protected String homePhone_ = "";
    protected String personalCellPhone_ = "";
    protected String shortNum_ = "";
    protected String shortNum2_ = "";
    protected String workPhone_ = "";
    protected String workPhone2_ = "";
    protected String virtualCellPhone_ = "";
    protected boolean isAllowLogin_;
    protected String remark_ = "";
    protected String departmentIds_ = "";
    protected String virtualCode_ = "";
    protected String fax_ = "";
    protected String shortPinyin_ = "";
    protected String customField_ = "";
    protected String privilege_ = "";
    protected String suid_ = "";
    protected long index_ = 0;
    protected boolean isActive_ = false;


    public static ArrayList<String> names() {
        ArrayList<String> result = new ArrayList<String>(27);
        result.add("uid");
        result.add("department_id");
        result.add("sequence");
        result.add("mobile");
        result.add("title");
        result.add("name");
        result.add("pinyin");
        result.add("sex");
        result.add("email");
        result.add("home_phone");
        result.add("personal_cell_phone");
        result.add("short_num");
        result.add("short_num2");
        result.add("work_phone");
        result.add("work_phone2");
        result.add("virtual_cell_phone");
        result.add("is_allow_login");
        result.add("remark");
        result.add("department_ids");
        result.add("virtual_code");
        result.add("fax");
        result.add("short_pinyin");
        result.add("custom_field");
        result.add("privilege");
        result.add("suid");
        result.add("index");
        result.add("isActive");
        return result;
    }
    public long getUid() {
        return this.uid_;
    }
    public void setUid ( long val) {
        this.uid_ = val;
    }
    public long getDepartmentId() {
        return this.departmentId_;
    }
    public void setDepartmentId ( long val) {
        this.departmentId_ = val;
    }
    public int getSequence() {
        return this.sequence_;
    }
    public void setSequence ( int val) {
        this.sequence_ = val;
    }
    public String getMobile() {
        return this.mobile_;
    }
    public void setMobile ( String val) {
        this.mobile_ = val;
    }
    public String getTitle() {
        return this.title_;
    }
    public void setTitle ( String val) {
        this.title_ = val;
    }
    public String getName() {
        return this.name_;
    }
    public void setName ( String val) {
        this.name_ = val;
    }
    public String getPinyin() {
        return this.pinyin_;
    }
    public void setPinyin ( String val) {
        this.pinyin_ = val;
    }
    public byte getSex() {
        return this.sex_;
    }
    public void setSex ( byte val) {
        this.sex_ = val;
    }
    public String getEmail() {
        return this.email_;
    }
    public void setEmail ( String val) {
        this.email_ = val;
    }
    public String getHomePhone() {
        return this.homePhone_;
    }
    public void setHomePhone ( String val) {
        this.homePhone_ = val;
    }
    public String getPersonalCellPhone() {
        return this.personalCellPhone_;
    }
    public void setPersonalCellPhone ( String val) {
        this.personalCellPhone_ = val;
    }
    public String getShortNum() {
        return this.shortNum_;
    }
    public void setShortNum ( String val) {
        this.shortNum_ = val;
    }
    public String getShortNum2() {
        return this.shortNum2_;
    }
    public void setShortNum2 ( String val) {
        this.shortNum2_ = val;
    }
    public String getWorkPhone() {
        return this.workPhone_;
    }
    public void setWorkPhone ( String val) {
        this.workPhone_ = val;
    }
    public String getWorkPhone2() {
        return this.workPhone2_;
    }
    public void setWorkPhone2 ( String val) {
        this.workPhone2_ = val;
    }
    public String getVirtualCellPhone() {
        return this.virtualCellPhone_;
    }
    public void setVirtualCellPhone ( String val) {
        this.virtualCellPhone_ = val;
    }
    public boolean getIsAllowLogin() {
        return this.isAllowLogin_;
    }
    public void setIsAllowLogin ( boolean val) {
        this.isAllowLogin_ = val;
    }
    public String getRemark() {
        return this.remark_;
    }
    public void setRemark ( String val) {
        this.remark_ = val;
    }
    public String getDepartmentIds() {
        return this.departmentIds_;
    }
    public void setDepartmentIds ( String val) {
        this.departmentIds_ = val;
    }
    public String getVirtualCode() {
        return this.virtualCode_;
    }
    public void setVirtualCode ( String val) {
        this.virtualCode_ = val;
    }
    public String getFax() {
        return this.fax_;
    }
    public void setFax ( String val) {
        this.fax_ = val;
    }
    public String getShortPinyin() {
        return this.shortPinyin_;
    }
    public void setShortPinyin ( String val) {
        this.shortPinyin_ = val;
    }
    public String getCustomField() {
        return this.customField_;
    }
    public void setCustomField ( String val) {
        this.customField_ = val;
    }
    public String getPrivilege() {
        return this.privilege_;
    }
    public void setPrivilege ( String val) {
        this.privilege_ = val;
    }
    public String getSuid() {
        return this.suid_;
    }
    public void setSuid ( String val) {
        this.suid_ = val;
    }
    public long getIndex() {
        return this.index_;
    }
    public void setIndex ( long val) {
        this.index_ = val;
    }
    public boolean getIsActive() {
        return this.isActive_;
    }
    public void setIsActive ( boolean val) {
        this.isActive_ = val;
    }

    public int size() {
        byte __fieldNum = 27;
        do {
            if(isActive_ == false)
                __fieldNum--;
            else
                break;
            if(index_ == 0)
                __fieldNum--;
            else
                break;
            if("".equals(suid_))
                __fieldNum--;
            else
                break;
            if("".equals(privilege_))
                __fieldNum--;
            else
                break;
            if("".equals(customField_))
                __fieldNum--;
            else
                break;
            if("".equals(shortPinyin_))
                __fieldNum--;
            else
                break;
            if("".equals(fax_))
                __fieldNum--;
            else
                break;
            if("".equals(virtualCode_))
                __fieldNum--;
            else
                break;
            if("".equals(departmentIds_))
                __fieldNum--;
            else
                break;
            if("".equals(remark_))
                __fieldNum--;
            else
                break;
        } while(false);
        int __size = 20;
        __size += PackData.getSize(uid_);
        __size += PackData.getSize(departmentId_);
        __size += PackData.getSize(sequence_);
        __size += PackData.getSize(mobile_);
        __size += PackData.getSize(title_);
        __size += PackData.getSize(name_);
        __size += PackData.getSize(pinyin_);
        __size += PackData.getSize(email_);
        __size += PackData.getSize(homePhone_);
        __size += PackData.getSize(personalCellPhone_);
        __size += PackData.getSize(shortNum_);
        __size += PackData.getSize(shortNum2_);
        __size += PackData.getSize(workPhone_);
        __size += PackData.getSize(workPhone2_);
        __size += PackData.getSize(virtualCellPhone_);
        do {
            if(__fieldNum == 17) break;
            __size += 1;
            __size += PackData.getSize(remark_);
            if(__fieldNum == 18) break;
            __size += 1;
            __size += PackData.getSize(departmentIds_);
            if(__fieldNum == 19) break;
            __size += 1;
            __size += PackData.getSize(virtualCode_);
            if(__fieldNum == 20) break;
            __size += 1;
            __size += PackData.getSize(fax_);
            if(__fieldNum == 21) break;
            __size += 1;
            __size += PackData.getSize(shortPinyin_);
            if(__fieldNum == 22) break;
            __size += 1;
            __size += PackData.getSize(customField_);
            if(__fieldNum == 23) break;
            __size += 1;
            __size += PackData.getSize(privilege_);
            if(__fieldNum == 24) break;
            __size += 1;
            __size += PackData.getSize(suid_);
            if(__fieldNum == 25) break;
            __size += 1;
            __size += PackData.getSize(index_);
            if(__fieldNum == 26) break;
            __size += 2;
        } while(false);
        return __size;
    }
    public void packData(PackData packer) {
        byte __fieldNum = 27;
        do {
            if(isActive_ == false)
                __fieldNum--;
            else
                break;
            if(index_ == 0)
                __fieldNum--;
            else
                break;
            if("".equals(suid_))
                __fieldNum--;
            else
                break;
            if("".equals(privilege_))
                __fieldNum--;
            else
                break;
            if("".equals(customField_))
                __fieldNum--;
            else
                break;
            if("".equals(shortPinyin_))
                __fieldNum--;
            else
                break;
            if("".equals(fax_))
                __fieldNum--;
            else
                break;
            if("".equals(virtualCode_))
                __fieldNum--;
            else
                break;
            if("".equals(departmentIds_))
                __fieldNum--;
            else
                break;
            if("".equals(remark_))
                __fieldNum--;
            else
                break;
        } while(false);
        packer.packByte(__fieldNum);
        do {
            packer.packByte(PackData.FT_NUMBER);
            packer.packLong(uid_);
            packer.packByte(PackData.FT_NUMBER);
            packer.packLong(departmentId_);
            packer.packByte(PackData.FT_NUMBER);
            packer.packInt(sequence_);
            packer.packByte(PackData.FT_STRING);
            packer.packString(mobile_);
            packer.packByte(PackData.FT_STRING);
            packer.packString(title_);
            packer.packByte(PackData.FT_STRING);
            packer.packString(name_);
            packer.packByte(PackData.FT_STRING);
            packer.packString(pinyin_);
            packer.packByte(PackData.FT_CHAR);
            packer.packByte(sex_);
            packer.packByte(PackData.FT_STRING);
            packer.packString(email_);
            packer.packByte(PackData.FT_STRING);
            packer.packString(homePhone_);
            packer.packByte(PackData.FT_STRING);
            packer.packString(personalCellPhone_);
            packer.packByte(PackData.FT_STRING);
            packer.packString(shortNum_);
            packer.packByte(PackData.FT_STRING);
            packer.packString(shortNum2_);
            packer.packByte(PackData.FT_STRING);
            packer.packString(workPhone_);
            packer.packByte(PackData.FT_STRING);
            packer.packString(workPhone2_);
            packer.packByte(PackData.FT_STRING);
            packer.packString(virtualCellPhone_);
            packer.packByte(PackData.FT_CHAR);
            packer.packBool(isAllowLogin_);
            if(__fieldNum == 17) break;
            packer.packByte(PackData.FT_STRING);
            packer.packString(remark_);
            if(__fieldNum == 18) break;
            packer.packByte(PackData.FT_STRING);
            packer.packString(departmentIds_);
            if(__fieldNum == 19) break;
            packer.packByte(PackData.FT_STRING);
            packer.packString(virtualCode_);
            if(__fieldNum == 20) break;
            packer.packByte(PackData.FT_STRING);
            packer.packString(fax_);
            if(__fieldNum == 21) break;
            packer.packByte(PackData.FT_STRING);
            packer.packString(shortPinyin_);
            if(__fieldNum == 22) break;
            packer.packByte(PackData.FT_STRING);
            packer.packString(customField_);
            if(__fieldNum == 23) break;
            packer.packByte(PackData.FT_STRING);
            packer.packString(privilege_);
            if(__fieldNum == 24) break;
            packer.packByte(PackData.FT_STRING);
            packer.packString(suid_);
            if(__fieldNum == 25) break;
            packer.packByte(PackData.FT_NUMBER);
            packer.packLong(index_);
            if(__fieldNum == 26) break;
            packer.packByte(PackData.FT_CHAR);
            packer.packBool(isActive_);
        } while(false);
    }
    public void unpackData(PackData packer) throws PackException {
        byte __num = packer.unpackByte();
        FieldType __field;
        if(__num < 17) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_NUMBER)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        uid_ = packer.unpackLong();
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_NUMBER)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        departmentId_ = packer.unpackLong();
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_NUMBER)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        sequence_ = packer.unpackInt();
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_STRING)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        mobile_ = packer.unpackString();
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_STRING)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        title_ = packer.unpackString();
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_STRING)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        name_ = packer.unpackString();
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_STRING)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        pinyin_ = packer.unpackString();
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_CHAR)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        sex_ = packer.unpackByte();
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_STRING)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        email_ = packer.unpackString();
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_STRING)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        homePhone_ = packer.unpackString();
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_STRING)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        personalCellPhone_ = packer.unpackString();
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_STRING)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        shortNum_ = packer.unpackString();
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_STRING)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        shortNum2_ = packer.unpackString();
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_STRING)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        workPhone_ = packer.unpackString();
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_STRING)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        workPhone2_ = packer.unpackString();
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_STRING)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        virtualCellPhone_ = packer.unpackString();
        __field = packer.unpackFieldType();
        if(!PackData.matchType(__field.baseType_, PackData.FT_CHAR)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
        isAllowLogin_ = packer.unpackBool();
        do {
            if(__num < 18) break;
            __field = packer.unpackFieldType();
            if(!PackData.matchType(__field.baseType_, PackData.FT_STRING)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
            remark_ = packer.unpackString();
            if(__num < 19) break;
            __field = packer.unpackFieldType();
            if(!PackData.matchType(__field.baseType_, PackData.FT_STRING)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
            departmentIds_ = packer.unpackString();
            if(__num < 20) break;
            __field = packer.unpackFieldType();
            if(!PackData.matchType(__field.baseType_, PackData.FT_STRING)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
            virtualCode_ = packer.unpackString();
            if(__num < 21) break;
            __field = packer.unpackFieldType();
            if(!PackData.matchType(__field.baseType_, PackData.FT_STRING)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
            fax_ = packer.unpackString();
            if(__num < 22) break;
            __field = packer.unpackFieldType();
            if(!PackData.matchType(__field.baseType_, PackData.FT_STRING)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
            shortPinyin_ = packer.unpackString();
            if(__num < 23) break;
            __field = packer.unpackFieldType();
            if(!PackData.matchType(__field.baseType_, PackData.FT_STRING)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
            customField_ = packer.unpackString();
            if(__num < 24) break;
            __field = packer.unpackFieldType();
            if(!PackData.matchType(__field.baseType_, PackData.FT_STRING)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
            privilege_ = packer.unpackString();
            if(__num < 25) break;
            __field = packer.unpackFieldType();
            if(!PackData.matchType(__field.baseType_, PackData.FT_STRING)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
            suid_ = packer.unpackString();
            if(__num < 26) break;
            __field = packer.unpackFieldType();
            if(!PackData.matchType(__field.baseType_, PackData.FT_NUMBER)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
            index_ = packer.unpackLong();
            if(__num < 27) break;
            __field = packer.unpackFieldType();
            if(!PackData.matchType(__field.baseType_, PackData.FT_CHAR)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
            isActive_ = packer.unpackBool();
        } while(false);
        for(int i = 27; i < __num; i++)
            packer.peekField();
    }
}


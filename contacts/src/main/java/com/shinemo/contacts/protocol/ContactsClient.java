package com.shinemo.contacts.protocol;

import com.shinemo.openim.base.RetCode;
import com.shinemo.openim.base.comm.AaceCaller;
import com.shinemo.openim.base.model.ResponseNode;
import com.shinemo.openim.base.packer.FieldType;
import com.shinemo.openim.base.packer.PackData;
import com.shinemo.openim.base.packer.PackException;
import com.shinemo.openim.base.wrapper.MutableBoolean;
import com.shinemo.openim.base.wrapper.MutableLong;
import com.shinemo.openim.base.wrapper.MutableString;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantLock;

public class ContactsClient extends AaceCaller {
    private static ReentrantLock uniqLock_ = new ReentrantLock();
    private static ContactsClient uniqInstance = null;
    public static ContactsClient get() {
        if(uniqInstance != null) return uniqInstance;
        uniqLock_.lock();
        if(uniqInstance != null) return uniqInstance;
        uniqInstance = new ContactsClient();
        uniqLock_.unlock();
        return uniqInstance;
    }
    public static byte[] __packCheckNew(String mobile, TreeMap< Long,Long > orgVerMap, int type) {
        PackData __packer = new PackData();
        byte __fieldNum = 3;
        do {
            if(type == 0)
                __fieldNum--;
            else
                break;
        } while(false);
        int __packSize = 5;
        __packSize += PackData.getSize(mobile);
        if(orgVerMap == null)
            __packSize++;
        else {
            __packSize += PackData.getSize(orgVerMap.size());
            Iterator< Map.Entry< Long,Long > >itr = orgVerMap.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry< Long,Long > entry = itr.next();
                __packSize += PackData.getSize((entry.getKey()));
                __packSize += PackData.getSize((entry.getValue()));
            }
        }
        do {
            if(__fieldNum == 2) break;
            __packSize += 1;
            __packSize += PackData.getSize(type);
        } while(false);
        byte[] __reqdata = new byte[__packSize];
        __packer.resetOutBuff(__reqdata);
        __packer.packByte(__fieldNum);
        do {
            __packer.packByte(PackData.FT_STRING);
            __packer.packString(mobile);
            __packer.packByte(PackData.FT_MAP);
            __packer.packByte(PackData.FT_NUMBER);
            __packer.packByte(PackData.FT_NUMBER);
            if(orgVerMap == null)
                __packer.packByte((byte)0);
            else {
                int len = orgVerMap.size();
                __packer.packInt(len);
                Iterator< Map.Entry< Long,Long > >itr = orgVerMap.entrySet().iterator();
                while (itr.hasNext()) {
                    Map.Entry< Long,Long > entry = itr.next();
                    __packer.packLong((entry.getKey()).longValue());
                    __packer.packLong((entry.getValue()).longValue());
                }
            }
            if(__fieldNum == 2) break;
            __packer.packByte(PackData.FT_NUMBER);
            __packer.packInt(type);
        } while(false);
        return __reqdata;
    }
    public static int __unpackCheckNew(ResponseNode __rsp, TreeMap< Long,Integer > orgUsrCntMap, TreeMap< Long,Long > orgLastVerMap, TreeMap< Long,Long > orgActiveVerMap, TreeMap< Long,OrgConfVo > orgConfMap) {
        int __retcode = __rsp.getRetcode();
        if(__retcode != RetCode.RET_SUCCESS) return __retcode;
        PackData __packer = new PackData();
        byte[] __rspdata = __rsp.getRspdata();
        __packer.resetInBuff(__rspdata);
        try {
            __retcode = __packer.unpackInt();
            try {
                byte __num = __packer.unpackByte();
                FieldType __field;
                if(__num < 2) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
                __field = __packer.unpackFieldType();
                if(!PackData.matchType(__field.baseType_, PackData.FT_MAP)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
                {
                    int size = __packer.unpackInt();
                    if(size > PackData.MAX_RECORD_SIZE || size < 0) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
                    for(int i = 0; i < size; i++) {
                        Long key = null;
                        Integer value = null;
                        key = new Long(__packer.unpackLong());
                        value = new Integer(__packer.unpackInt());
                        orgUsrCntMap.put(key, value);
                    }
                }
                __field = __packer.unpackFieldType();
                if(!PackData.matchType(__field.baseType_, PackData.FT_MAP)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
                {
                    int size = __packer.unpackInt();
                    if(size > PackData.MAX_RECORD_SIZE || size < 0) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
                    for(int i = 0; i < size; i++) {
                        Long key = null;
                        Long value = null;
                        key = new Long(__packer.unpackLong());
                        value = new Long(__packer.unpackLong());
                        orgLastVerMap.put(key, value);
                    }
                }
                do {
                    if(__num < 3) break;
                    __field = __packer.unpackFieldType();
                    if(!PackData.matchType(__field.baseType_, PackData.FT_MAP)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
                    {
                        int size = __packer.unpackInt();
                        if(size > PackData.MAX_RECORD_SIZE || size < 0) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
                        for(int i = 0; i < size; i++) {
                            Long key = null;
                            Long value = null;
                            key = new Long(__packer.unpackLong());
                            value = new Long(__packer.unpackLong());
                            orgActiveVerMap.put(key, value);
                        }
                    }
                    if(__num < 4) break;
                    __field = __packer.unpackFieldType();
                    if(!PackData.matchType(__field.baseType_, PackData.FT_MAP)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
                    {
                        int size = __packer.unpackInt();
                        if(size > PackData.MAX_RECORD_SIZE || size < 0) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
                        for(int i = 0; i < size; i++) {
                            Long key = null;
                            OrgConfVo value = null;
                            key = new Long(__packer.unpackLong());
                            if(value == null) value = new OrgConfVo();
                            value.unpackData(__packer);
                            orgConfMap.put(key, value);
                        }
                    }
                } while(false);
            } catch(PackException e) {
                return __retcode != RetCode.RET_SUCCESS ? __retcode : RetCode.RET_INVALID;
            }
        } catch(PackException e) {
            return RetCode.RET_INVALID;
        }
        return __retcode;
    }
    public int checkNew(String mobile, TreeMap< Long,Long > orgVerMap, int type, TreeMap< Long,Integer > orgUsrCntMap, TreeMap< Long,Long > orgLastVerMap, TreeMap< Long,Long > orgActiveVerMap, TreeMap< Long,OrgConfVo > orgConfMap) {
        return checkNew(mobile, orgVerMap, type, orgUsrCntMap, orgLastVerMap, orgActiveVerMap, orgConfMap, DEFAULT_TIMEOUT, true);
    }
    public int checkNew(String mobile, TreeMap< Long,Long > orgVerMap, int type, TreeMap< Long,Integer > orgUsrCntMap, TreeMap< Long,Long > orgLastVerMap, TreeMap< Long,Long > orgActiveVerMap, TreeMap< Long,OrgConfVo > orgConfMap, int __timeout, boolean __checkLogin) {
        byte[] __reqdata = __packCheckNew(mobile, orgVerMap, type);
        ResponseNode __rsp = invoke("Contacts", "checkNew", __reqdata, __timeout, __checkLogin);
        return __unpackCheckNew(__rsp, orgUsrCntMap, orgLastVerMap, orgActiveVerMap, orgConfMap);
    }
    public boolean async_checkNew(String mobile, TreeMap< Long,Long > orgVerMap, int type, CheckNewCallback __callback) {
        return async_checkNew(mobile, orgVerMap, type, __callback, DEFAULT_TIMEOUT, true);
    }
    public boolean async_checkNew(String mobile, TreeMap< Long,Long > orgVerMap, int type, CheckNewCallback __callback, int __timeout, boolean __checkLogin) {
        byte[] __reqdata = __packCheckNew(mobile, orgVerMap, type);
        return asyncCall("Contacts", "checkNew", __reqdata, __callback, __timeout, __checkLogin);
    }
    public static byte[] __packGetAll(long orgId, String mobile) {
        PackData __packer = new PackData();
        int __packSize = 3;
        __packSize += PackData.getSize(orgId);
        __packSize += PackData.getSize(mobile);
        byte[] __reqdata = new byte[__packSize];
        __packer.resetOutBuff(__reqdata);
        byte __fieldNum = 2;
        __packer.packByte(__fieldNum);
        __packer.packByte(PackData.FT_NUMBER);
        __packer.packLong(orgId);
        __packer.packByte(PackData.FT_STRING);
        __packer.packString(mobile);
        return __reqdata;
    }
    public static int __unpackGetAll(ResponseNode __rsp, OrgVo orgVo) {
        int __retcode = __rsp.getRetcode();
        if(__retcode != RetCode.RET_SUCCESS) return __retcode;
        PackData __packer = new PackData();
        byte[] __rspdata = __rsp.getRspdata();
        __packer.resetInBuff(__rspdata);
        try {
            __retcode = __packer.unpackInt();
            try {
                byte __num = __packer.unpackByte();
                FieldType __field;
                if(__num < 1) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
                __field = __packer.unpackFieldType();
                if(!PackData.matchType(__field.baseType_, PackData.FT_STRUCT)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
                if(orgVo == null) orgVo = new OrgVo();
                orgVo.unpackData(__packer);
            } catch(PackException e) {
                return __retcode != RetCode.RET_SUCCESS ? __retcode : RetCode.RET_INVALID;
            }
        } catch(PackException e) {
            return RetCode.RET_INVALID;
        }
        return __retcode;
    }
    public int getAll(long orgId, String mobile, OrgVo orgVo) {
        return getAll(orgId, mobile, orgVo, DEFAULT_TIMEOUT, true);
    }
    public int getAll(long orgId, String mobile, OrgVo orgVo, int __timeout, boolean __checkLogin) {
        byte[] __reqdata = __packGetAll(orgId, mobile);
        ResponseNode __rsp = invoke("Contacts", "getAll", __reqdata, __timeout, __checkLogin);
        return __unpackGetAll(__rsp, orgVo);
    }
    public boolean async_getAll(long orgId, String mobile, GetAllCallback __callback) {
        return async_getAll(orgId, mobile, __callback, DEFAULT_TIMEOUT, true);
    }
    public boolean async_getAll(long orgId, String mobile, GetAllCallback __callback, int __timeout, boolean __checkLogin) {
        byte[] __reqdata = __packGetAll(orgId, mobile);
        return asyncCall("Contacts", "getAll", __reqdata, __callback, __timeout, __checkLogin);
    }
    public static byte[] __packGetOrgInfo(long orgId) {
        PackData __packer = new PackData();
        int __packSize = 2;
        __packSize += PackData.getSize(orgId);
        byte[] __reqdata = new byte[__packSize];
        __packer.resetOutBuff(__reqdata);
        byte __fieldNum = 1;
        __packer.packByte(__fieldNum);
        __packer.packByte(PackData.FT_NUMBER);
        __packer.packLong(orgId);
        return __reqdata;
    }
    public static int __unpackGetOrgInfo(ResponseNode __rsp, OrgVo orgVo) {
        int __retcode = __rsp.getRetcode();
        if(__retcode != RetCode.RET_SUCCESS) return __retcode;
        PackData __packer = new PackData();
        byte[] __rspdata = __rsp.getRspdata();
        __packer.resetInBuff(__rspdata);
        try {
            __retcode = __packer.unpackInt();
            try {
                byte __num = __packer.unpackByte();
                FieldType __field;
                if(__num < 1) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
                __field = __packer.unpackFieldType();
                if(!PackData.matchType(__field.baseType_, PackData.FT_STRUCT)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
                if(orgVo == null) orgVo = new OrgVo();
                orgVo.unpackData(__packer);
            } catch(PackException e) {
                return __retcode != RetCode.RET_SUCCESS ? __retcode : RetCode.RET_INVALID;
            }
        } catch(PackException e) {
            return RetCode.RET_INVALID;
        }
        return __retcode;
    }
    public int getOrgInfo(long orgId, OrgVo orgVo) {
        return getOrgInfo(orgId, orgVo, DEFAULT_TIMEOUT, true);
    }
    public int getOrgInfo(long orgId, OrgVo orgVo, int __timeout, boolean __checkLogin) {
        byte[] __reqdata = __packGetOrgInfo(orgId);
        ResponseNode __rsp = invoke("Contacts", "getOrgInfo", __reqdata, __timeout, __checkLogin);
        return __unpackGetOrgInfo(__rsp, orgVo);
    }
    public boolean async_getOrgInfo(long orgId, GetOrgInfoCallback __callback) {
        return async_getOrgInfo(orgId, __callback, DEFAULT_TIMEOUT, true);
    }
    public boolean async_getOrgInfo(long orgId, GetOrgInfoCallback __callback, int __timeout, boolean __checkLogin) {
        byte[] __reqdata = __packGetOrgInfo(orgId);
        return asyncCall("Contacts", "getOrgInfo", __reqdata, __callback, __timeout, __checkLogin);
    }
    public static byte[] __packGetDepts(String mobile, long orgId) {
        PackData __packer = new PackData();
        int __packSize = 3;
        __packSize += PackData.getSize(mobile);
        __packSize += PackData.getSize(orgId);
        byte[] __reqdata = new byte[__packSize];
        __packer.resetOutBuff(__reqdata);
        byte __fieldNum = 2;
        __packer.packByte(__fieldNum);
        __packer.packByte(PackData.FT_STRING);
        __packer.packString(mobile);
        __packer.packByte(PackData.FT_NUMBER);
        __packer.packLong(orgId);
        return __reqdata;
    }
    public static int __unpackGetDepts(ResponseNode __rsp, ArrayList< DepartmentVo > depts, MutableLong directDepartmentVersion) {
        int __retcode = __rsp.getRetcode();
        if(__retcode != RetCode.RET_SUCCESS) return __retcode;
        PackData __packer = new PackData();
        byte[] __rspdata = __rsp.getRspdata();
        __packer.resetInBuff(__rspdata);
        directDepartmentVersion.set(0);
        try {
            __retcode = __packer.unpackInt();
            try {
                byte __num = __packer.unpackByte();
                FieldType __field;
                if(__num < 1) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
                __field = __packer.unpackFieldType();
                if(!PackData.matchType(__field.baseType_, PackData.FT_VECTOR)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
                {
                    int size = __packer.unpackInt();
                    if(size > PackData.MAX_RECORD_SIZE || size < 0) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
                    depts.ensureCapacity(size);
                    for(int i = 0; i < size; i++) {
                        DepartmentVo tmpVal = null;
                        if(tmpVal == null) tmpVal = new DepartmentVo();
                        tmpVal.unpackData(__packer);
                        depts.add(tmpVal);
                    }
                }
                do {
                    if(__num < 2) break;
                    __field = __packer.unpackFieldType();
                    if(!PackData.matchType(__field.baseType_, PackData.FT_NUMBER)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
                    directDepartmentVersion.set(__packer.unpackLong());
                } while(false);
            } catch(PackException e) {
                return __retcode != RetCode.RET_SUCCESS ? __retcode : RetCode.RET_INVALID;
            }
        } catch(PackException e) {
            return RetCode.RET_INVALID;
        }
        return __retcode;
    }
    public int getDepts(String mobile, long orgId, ArrayList< DepartmentVo > depts, MutableLong directDepartmentVersion) {
        return getDepts(mobile, orgId, depts, directDepartmentVersion, DEFAULT_TIMEOUT, true);
    }
    public int getDepts(String mobile, long orgId, ArrayList< DepartmentVo > depts, MutableLong directDepartmentVersion, int __timeout, boolean __checkLogin) {
        byte[] __reqdata = __packGetDepts(mobile, orgId);
        ResponseNode __rsp = invoke("Contacts", "getDepts", __reqdata, __timeout, __checkLogin);
        return __unpackGetDepts(__rsp, depts, directDepartmentVersion);
    }
    public boolean async_getDepts(String mobile, long orgId, GetDeptsCallback __callback) {
        return async_getDepts(mobile, orgId, __callback, DEFAULT_TIMEOUT, true);
    }
    public boolean async_getDepts(String mobile, long orgId, GetDeptsCallback __callback, int __timeout, boolean __checkLogin) {
        byte[] __reqdata = __packGetDepts(mobile, orgId);
        return asyncCall("Contacts", "getDepts", __reqdata, __callback, __timeout, __checkLogin);
    }
    public static byte[] __packGetUsersSegment(String mobile, long orgId, int startIdx, int count, long deptId) {
        PackData __packer = new PackData();
        byte __fieldNum = 5;
        do {
            if(deptId == -1)
                __fieldNum--;
            else
                break;
        } while(false);
        int __packSize = 5;
        __packSize += PackData.getSize(mobile);
        __packSize += PackData.getSize(orgId);
        __packSize += PackData.getSize(startIdx);
        __packSize += PackData.getSize(count);
        do {
            if(__fieldNum == 4) break;
            __packSize += 1;
            __packSize += PackData.getSize(deptId);
        } while(false);
        byte[] __reqdata = new byte[__packSize];
        __packer.resetOutBuff(__reqdata);
        __packer.packByte(__fieldNum);
        do {
            __packer.packByte(PackData.FT_STRING);
            __packer.packString(mobile);
            __packer.packByte(PackData.FT_NUMBER);
            __packer.packLong(orgId);
            __packer.packByte(PackData.FT_NUMBER);
            __packer.packInt(startIdx);
            __packer.packByte(PackData.FT_NUMBER);
            __packer.packInt(count);
            if(__fieldNum == 4) break;
            __packer.packByte(PackData.FT_NUMBER);
            __packer.packLong(deptId);
        } while(false);
        return __reqdata;
    }
    public static int __unpackGetUsersSegment(ResponseNode __rsp, ArrayList< User > users, MutableBoolean bEnd) {
        int __retcode = __rsp.getRetcode();
        if(__retcode != RetCode.RET_SUCCESS) return __retcode;
        PackData __packer = new PackData();
        byte[] __rspdata = __rsp.getRspdata();
        __packer.resetInBuff(__rspdata);
        bEnd.set(false);
        try {
            __retcode = __packer.unpackInt();
            try {
                byte __num = __packer.unpackByte();
                FieldType __field;
                if(__num < 1) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
                __field = __packer.unpackFieldType();
                if(!PackData.matchType(__field.baseType_, PackData.FT_VECTOR)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
                {
                    int size = __packer.unpackInt();
                    if(size > PackData.MAX_RECORD_SIZE || size < 0) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
                    users.ensureCapacity(size);
                    for(int i = 0; i < size; i++) {
                        User tmpVal = null;
                        if(tmpVal == null) tmpVal = new User();
                        tmpVal.unpackData(__packer);
                        users.add(tmpVal);
                    }
                }
                do {
                    if(__num < 2) break;
                    __field = __packer.unpackFieldType();
                    if(!PackData.matchType(__field.baseType_, PackData.FT_CHAR)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
                    bEnd.set(__packer.unpackBool());
                } while(false);
            } catch(PackException e) {
                return __retcode != RetCode.RET_SUCCESS ? __retcode : RetCode.RET_INVALID;
            }
        } catch(PackException e) {
            return RetCode.RET_INVALID;
        }
        return __retcode;
    }
    public int getUsersSegment(String mobile, long orgId, int startIdx, int count, long deptId, ArrayList< User > users, MutableBoolean bEnd) {
        return getUsersSegment(mobile, orgId, startIdx, count, deptId, users, bEnd, DEFAULT_TIMEOUT, true);
    }
    public int getUsersSegment(String mobile, long orgId, int startIdx, int count, long deptId, ArrayList< User > users, MutableBoolean bEnd, int __timeout, boolean __checkLogin) {
        byte[] __reqdata = __packGetUsersSegment(mobile, orgId, startIdx, count, deptId);
        ResponseNode __rsp = invoke("Contacts", "getUsersSegment", __reqdata, __timeout, __checkLogin);
        return __unpackGetUsersSegment(__rsp, users, bEnd);
    }
    public boolean async_getUsersSegment(String mobile, long orgId, int startIdx, int count, long deptId, GetUsersSegmentCallback __callback) {
        return async_getUsersSegment(mobile, orgId, startIdx, count, deptId, __callback, DEFAULT_TIMEOUT, true);
    }
    public boolean async_getUsersSegment(String mobile, long orgId, int startIdx, int count, long deptId, GetUsersSegmentCallback __callback, int __timeout, boolean __checkLogin) {
        byte[] __reqdata = __packGetUsersSegment(mobile, orgId, startIdx, count, deptId);
        return asyncCall("Contacts", "getUsersSegment", __reqdata, __callback, __timeout, __checkLogin);
    }
    public static byte[] __packGetUsersSegmentByVer(long orgId, int startIdx, int count, long version) {
        PackData __packer = new PackData();
        int __packSize = 5;
        __packSize += PackData.getSize(orgId);
        __packSize += PackData.getSize(startIdx);
        __packSize += PackData.getSize(count);
        __packSize += PackData.getSize(version);
        byte[] __reqdata = new byte[__packSize];
        __packer.resetOutBuff(__reqdata);
        byte __fieldNum = 4;
        __packer.packByte(__fieldNum);
        __packer.packByte(PackData.FT_NUMBER);
        __packer.packLong(orgId);
        __packer.packByte(PackData.FT_NUMBER);
        __packer.packInt(startIdx);
        __packer.packByte(PackData.FT_NUMBER);
        __packer.packInt(count);
        __packer.packByte(PackData.FT_NUMBER);
        __packer.packLong(version);
        return __reqdata;
    }
    public static int __unpackGetUsersSegmentByVer(ResponseNode __rsp, ArrayList< User > users, MutableBoolean bEnd) {
        int __retcode = __rsp.getRetcode();
        if(__retcode != RetCode.RET_SUCCESS) return __retcode;
        PackData __packer = new PackData();
        byte[] __rspdata = __rsp.getRspdata();
        __packer.resetInBuff(__rspdata);
        try {
            __retcode = __packer.unpackInt();
            try {
                byte __num = __packer.unpackByte();
                FieldType __field;
                if(__num < 2) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
                __field = __packer.unpackFieldType();
                if(!PackData.matchType(__field.baseType_, PackData.FT_VECTOR)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
                {
                    int size = __packer.unpackInt();
                    if(size > PackData.MAX_RECORD_SIZE || size < 0) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
                    users.ensureCapacity(size);
                    for(int i = 0; i < size; i++) {
                        User tmpVal = null;
                        if(tmpVal == null) tmpVal = new User();
                        tmpVal.unpackData(__packer);
                        users.add(tmpVal);
                    }
                }
                __field = __packer.unpackFieldType();
                if(!PackData.matchType(__field.baseType_, PackData.FT_CHAR)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
                bEnd.set(__packer.unpackBool());
            } catch(PackException e) {
                return __retcode != RetCode.RET_SUCCESS ? __retcode : RetCode.RET_INVALID;
            }
        } catch(PackException e) {
            return RetCode.RET_INVALID;
        }
        return __retcode;
    }
    public int getUsersSegmentByVer(long orgId, int startIdx, int count, long version, ArrayList< User > users, MutableBoolean bEnd) {
        return getUsersSegmentByVer(orgId, startIdx, count, version, users, bEnd, DEFAULT_TIMEOUT, true);
    }
    public int getUsersSegmentByVer(long orgId, int startIdx, int count, long version, ArrayList< User > users, MutableBoolean bEnd, int __timeout, boolean __checkLogin) {
        byte[] __reqdata = __packGetUsersSegmentByVer(orgId, startIdx, count, version);
        ResponseNode __rsp = invoke("Contacts", "getUsersSegmentByVer", __reqdata, __timeout, __checkLogin);
        return __unpackGetUsersSegmentByVer(__rsp, users, bEnd);
    }
    public boolean async_getUsersSegmentByVer(long orgId, int startIdx, int count, long version, GetUsersSegmentByVerCallback __callback) {
        return async_getUsersSegmentByVer(orgId, startIdx, count, version, __callback, DEFAULT_TIMEOUT, true);
    }
    public boolean async_getUsersSegmentByVer(long orgId, int startIdx, int count, long version, GetUsersSegmentByVerCallback __callback, int __timeout, boolean __checkLogin) {
        byte[] __reqdata = __packGetUsersSegmentByVer(orgId, startIdx, count, version);
        return asyncCall("Contacts", "getUsersSegmentByVer", __reqdata, __callback, __timeout, __checkLogin);
    }
    public static byte[] __packSyncEnd(long orgId) {
        PackData __packer = new PackData();
        int __packSize = 2;
        __packSize += PackData.getSize(orgId);
        byte[] __reqdata = new byte[__packSize];
        __packer.resetOutBuff(__reqdata);
        byte __fieldNum = 1;
        __packer.packByte(__fieldNum);
        __packer.packByte(PackData.FT_NUMBER);
        __packer.packLong(orgId);
        return __reqdata;
    }
    public static int __unpackSyncEnd(ResponseNode __rsp) {
        int __retcode = __rsp.getRetcode();
        if(__retcode != RetCode.RET_SUCCESS) return __retcode;
        PackData __packer = new PackData();
        byte[] __rspdata = __rsp.getRspdata();
        __packer.resetInBuff(__rspdata);
        try {
            __retcode = __packer.unpackInt();
        } catch(PackException e) {
            return RetCode.RET_INVALID;
        }
        return __retcode;
    }
    public int syncEnd(long orgId) {
        return syncEnd(orgId, DEFAULT_TIMEOUT, true);
    }
    public int syncEnd(long orgId, int __timeout, boolean __checkLogin) {
        byte[] __reqdata = __packSyncEnd(orgId);
        ResponseNode __rsp = invoke("Contacts", "syncEnd", __reqdata, __timeout, __checkLogin);
        return __unpackSyncEnd(__rsp);
    }
    public boolean async_syncEnd(long orgId, SyncEndCallback __callback) {
        return async_syncEnd(orgId, __callback, DEFAULT_TIMEOUT, true);
    }
    public boolean async_syncEnd(long orgId, SyncEndCallback __callback, int __timeout, boolean __checkLogin) {
        byte[] __reqdata = __packSyncEnd(orgId);
        return asyncCall("Contacts", "syncEnd", __reqdata, __callback, __timeout, __checkLogin);
    }
    public static byte[] __packGetAvatarPrefix(long orgId) {
        PackData __packer = new PackData();
        int __packSize = 2;
        __packSize += PackData.getSize(orgId);
        byte[] __reqdata = new byte[__packSize];
        __packer.resetOutBuff(__reqdata);
        byte __fieldNum = 1;
        __packer.packByte(__fieldNum);
        __packer.packByte(PackData.FT_NUMBER);
        __packer.packLong(orgId);
        return __reqdata;
    }
    public static int __unpackGetAvatarPrefix(ResponseNode __rsp, MutableString avatarPrefix) {
        int __retcode = __rsp.getRetcode();
        if(__retcode != RetCode.RET_SUCCESS) return __retcode;
        PackData __packer = new PackData();
        byte[] __rspdata = __rsp.getRspdata();
        __packer.resetInBuff(__rspdata);
        try {
            __retcode = __packer.unpackInt();
            try {
                byte __num = __packer.unpackByte();
                FieldType __field;
                if(__num < 1) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
                __field = __packer.unpackFieldType();
                if(!PackData.matchType(__field.baseType_, PackData.FT_STRING)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
                avatarPrefix.set(__packer.unpackString());
            } catch(PackException e) {
                return __retcode != RetCode.RET_SUCCESS ? __retcode : RetCode.RET_INVALID;
            }
        } catch(PackException e) {
            return RetCode.RET_INVALID;
        }
        return __retcode;
    }
    public int getAvatarPrefix(long orgId, MutableString avatarPrefix) {
        return getAvatarPrefix(orgId, avatarPrefix, DEFAULT_TIMEOUT, true);
    }
    public int getAvatarPrefix(long orgId, MutableString avatarPrefix, int __timeout, boolean __checkLogin) {
        byte[] __reqdata = __packGetAvatarPrefix(orgId);
        ResponseNode __rsp = invoke("Contacts", "getAvatarPrefix", __reqdata, __timeout, __checkLogin);
        return __unpackGetAvatarPrefix(__rsp, avatarPrefix);
    }
    public boolean async_getAvatarPrefix(long orgId, GetAvatarPrefixCallback __callback) {
        return async_getAvatarPrefix(orgId, __callback, DEFAULT_TIMEOUT, true);
    }
    public boolean async_getAvatarPrefix(long orgId, GetAvatarPrefixCallback __callback, int __timeout, boolean __checkLogin) {
        byte[] __reqdata = __packGetAvatarPrefix(orgId);
        return asyncCall("Contacts", "getAvatarPrefix", __reqdata, __callback, __timeout, __checkLogin);
    }
    public static byte[] __packGetUsersByCheck(ArrayList< OrgUserIndexs > indexDatas) {
        PackData __packer = new PackData();
        int __packSize = 3;
        if(indexDatas == null)
            __packSize++;
        else {
            __packSize += PackData.getSize(indexDatas.size());
            for(int i = 0; i < indexDatas.size(); i++) {
                __packSize += (indexDatas.get(i)).size();
            }
        }
        byte[] __reqdata = new byte[__packSize];
        __packer.resetOutBuff(__reqdata);
        byte __fieldNum = 1;
        __packer.packByte(__fieldNum);
        __packer.packByte(PackData.FT_VECTOR);
        __packer.packByte(PackData.FT_STRUCT);
        if(indexDatas == null)
            __packer.packByte((byte)0);
        else {
            int len = indexDatas.size();
            __packer.packInt(len);
            for(int i = 0; i < indexDatas.size(); i++) {
                (indexDatas.get(i)).packData(__packer);
            }
        }
        return __reqdata;
    }
    public static int __unpackGetUsersByCheck(ResponseNode __rsp, ArrayList< OrgUsers > userDatas) {
        int __retcode = __rsp.getRetcode();
        if(__retcode != RetCode.RET_SUCCESS) return __retcode;
        PackData __packer = new PackData();
        byte[] __rspdata = __rsp.getRspdata();
        __packer.resetInBuff(__rspdata);
        try {
            __retcode = __packer.unpackInt();
            try {
                byte __num = __packer.unpackByte();
                FieldType __field;
                if(__num < 1) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
                __field = __packer.unpackFieldType();
                if(!PackData.matchType(__field.baseType_, PackData.FT_VECTOR)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
                {
                    int size = __packer.unpackInt();
                    if(size > PackData.MAX_RECORD_SIZE || size < 0) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
                    userDatas.ensureCapacity(size);
                    for(int i = 0; i < size; i++) {
                        OrgUsers tmpVal = null;
                        if(tmpVal == null) tmpVal = new OrgUsers();
                        tmpVal.unpackData(__packer);
                        userDatas.add(tmpVal);
                    }
                }
            } catch(PackException e) {
                return __retcode != RetCode.RET_SUCCESS ? __retcode : RetCode.RET_INVALID;
            }
        } catch(PackException e) {
            return RetCode.RET_INVALID;
        }
        return __retcode;
    }
    public int getUsersByCheck(ArrayList< OrgUserIndexs > indexDatas, ArrayList< OrgUsers > userDatas) {
        return getUsersByCheck(indexDatas, userDatas, DEFAULT_TIMEOUT, true);
    }
    public int getUsersByCheck(ArrayList< OrgUserIndexs > indexDatas, ArrayList< OrgUsers > userDatas, int __timeout, boolean __checkLogin) {
        byte[] __reqdata = __packGetUsersByCheck(indexDatas);
        ResponseNode __rsp = invoke("Contacts", "getUsersByCheck", __reqdata, __timeout, __checkLogin);
        return __unpackGetUsersByCheck(__rsp, userDatas);
    }
    public boolean async_getUsersByCheck(ArrayList< OrgUserIndexs > indexDatas, GetUsersByCheckCallback __callback) {
        return async_getUsersByCheck(indexDatas, __callback, DEFAULT_TIMEOUT, true);
    }
    public boolean async_getUsersByCheck(ArrayList< OrgUserIndexs > indexDatas, GetUsersByCheckCallback __callback, int __timeout, boolean __checkLogin) {
        byte[] __reqdata = __packGetUsersByCheck(indexDatas);
        return asyncCall("Contacts", "getUsersByCheck", __reqdata, __callback, __timeout, __checkLogin);
    }
    public static byte[] __packModifyUserEmail(long orgId, String email) {
        PackData __packer = new PackData();
        int __packSize = 3;
        __packSize += PackData.getSize(orgId);
        __packSize += PackData.getSize(email);
        byte[] __reqdata = new byte[__packSize];
        __packer.resetOutBuff(__reqdata);
        byte __fieldNum = 2;
        __packer.packByte(__fieldNum);
        __packer.packByte(PackData.FT_NUMBER);
        __packer.packLong(orgId);
        __packer.packByte(PackData.FT_STRING);
        __packer.packString(email);
        return __reqdata;
    }
    public static int __unpackModifyUserEmail(ResponseNode __rsp) {
        int __retcode = __rsp.getRetcode();
        if(__retcode != RetCode.RET_SUCCESS) return __retcode;
        PackData __packer = new PackData();
        byte[] __rspdata = __rsp.getRspdata();
        __packer.resetInBuff(__rspdata);
        try {
            __retcode = __packer.unpackInt();
        } catch(PackException e) {
            return RetCode.RET_INVALID;
        }
        return __retcode;
    }
    public int modifyUserEmail(long orgId, String email) {
        return modifyUserEmail(orgId, email, DEFAULT_TIMEOUT, true);
    }
    public int modifyUserEmail(long orgId, String email, int __timeout, boolean __checkLogin) {
        byte[] __reqdata = __packModifyUserEmail(orgId, email);
        ResponseNode __rsp = invoke("Contacts", "modifyUserEmail", __reqdata, __timeout, __checkLogin);
        return __unpackModifyUserEmail(__rsp);
    }
    public boolean async_modifyUserEmail(long orgId, String email, ModifyUserEmailCallback __callback) {
        return async_modifyUserEmail(orgId, email, __callback, DEFAULT_TIMEOUT, true);
    }
    public boolean async_modifyUserEmail(long orgId, String email, ModifyUserEmailCallback __callback, int __timeout, boolean __checkLogin) {
        byte[] __reqdata = __packModifyUserEmail(orgId, email);
        return asyncCall("Contacts", "modifyUserEmail", __reqdata, __callback, __timeout, __checkLogin);
    }
    public static byte[] __packCheckUserFrequentOrgDepartment(long version) {
        PackData __packer = new PackData();
        int __packSize = 2;
        __packSize += PackData.getSize(version);
        byte[] __reqdata = new byte[__packSize];
        __packer.resetOutBuff(__reqdata);
        byte __fieldNum = 1;
        __packer.packByte(__fieldNum);
        __packer.packByte(PackData.FT_NUMBER);
        __packer.packLong(version);
        return __reqdata;
    }
    public static int __unpackCheckUserFrequentOrgDepartment(ResponseNode __rsp, ArrayList< UserOrgDepartment > datas, MutableLong newVersion) {
        int __retcode = __rsp.getRetcode();
        if(__retcode != RetCode.RET_SUCCESS) return __retcode;
        PackData __packer = new PackData();
        byte[] __rspdata = __rsp.getRspdata();
        __packer.resetInBuff(__rspdata);
        try {
            __retcode = __packer.unpackInt();
            try {
                byte __num = __packer.unpackByte();
                FieldType __field;
                if(__num < 2) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
                __field = __packer.unpackFieldType();
                if(!PackData.matchType(__field.baseType_, PackData.FT_VECTOR)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
                {
                    int size = __packer.unpackInt();
                    if(size > PackData.MAX_RECORD_SIZE || size < 0) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
                    datas.ensureCapacity(size);
                    for(int i = 0; i < size; i++) {
                        UserOrgDepartment tmpVal = null;
                        if(tmpVal == null) tmpVal = new UserOrgDepartment();
                        tmpVal.unpackData(__packer);
                        datas.add(tmpVal);
                    }
                }
                __field = __packer.unpackFieldType();
                if(!PackData.matchType(__field.baseType_, PackData.FT_NUMBER)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
                newVersion.set(__packer.unpackLong());
            } catch(PackException e) {
                return __retcode != RetCode.RET_SUCCESS ? __retcode : RetCode.RET_INVALID;
            }
        } catch(PackException e) {
            return RetCode.RET_INVALID;
        }
        return __retcode;
    }
    public int checkUserFrequentOrgDepartment(long version, ArrayList< UserOrgDepartment > datas, MutableLong newVersion) {
        return checkUserFrequentOrgDepartment(version, datas, newVersion, DEFAULT_TIMEOUT, true);
    }
    public int checkUserFrequentOrgDepartment(long version, ArrayList< UserOrgDepartment > datas, MutableLong newVersion, int __timeout, boolean __checkLogin) {
        byte[] __reqdata = __packCheckUserFrequentOrgDepartment(version);
        ResponseNode __rsp = invoke("Contacts", "checkUserFrequentOrgDepartment", __reqdata, __timeout, __checkLogin);
        return __unpackCheckUserFrequentOrgDepartment(__rsp, datas, newVersion);
    }
    public boolean async_checkUserFrequentOrgDepartment(long version, CheckUserFrequentOrgDepartmentCallback __callback) {
        return async_checkUserFrequentOrgDepartment(version, __callback, DEFAULT_TIMEOUT, true);
    }
    public boolean async_checkUserFrequentOrgDepartment(long version, CheckUserFrequentOrgDepartmentCallback __callback, int __timeout, boolean __checkLogin) {
        byte[] __reqdata = __packCheckUserFrequentOrgDepartment(version);
        return asyncCall("Contacts", "checkUserFrequentOrgDepartment", __reqdata, __callback, __timeout, __checkLogin);
    }
    public static byte[] __packSetUserFrequentOrgDepartment(UserOrgDepartment data, long version) {
        PackData __packer = new PackData();
        byte __fieldNum = 2;
        do {
            if(version == -1)
                __fieldNum--;
            else
                break;
        } while(false);
        int __packSize = 2;
        __packSize += data.size();
        do {
            if(__fieldNum == 1) break;
            __packSize += 1;
            __packSize += PackData.getSize(version);
        } while(false);
        byte[] __reqdata = new byte[__packSize];
        __packer.resetOutBuff(__reqdata);
        __packer.packByte(__fieldNum);
        do {
            __packer.packByte(PackData.FT_STRUCT);
            data.packData(__packer);
            if(__fieldNum == 1) break;
            __packer.packByte(PackData.FT_NUMBER);
            __packer.packLong(version);
        } while(false);
        return __reqdata;
    }
    public static int __unpackSetUserFrequentOrgDepartment(ResponseNode __rsp, MutableLong newVersion, MutableBoolean errVer) {
        int __retcode = __rsp.getRetcode();
        if(__retcode != RetCode.RET_SUCCESS) return __retcode;
        PackData __packer = new PackData();
        byte[] __rspdata = __rsp.getRspdata();
        __packer.resetInBuff(__rspdata);
        errVer.set(false);
        try {
            __retcode = __packer.unpackInt();
            try {
                byte __num = __packer.unpackByte();
                FieldType __field;
                if(__num < 1) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
                __field = __packer.unpackFieldType();
                if(!PackData.matchType(__field.baseType_, PackData.FT_NUMBER)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
                newVersion.set(__packer.unpackLong());
                do {
                    if(__num < 2) break;
                    __field = __packer.unpackFieldType();
                    if(!PackData.matchType(__field.baseType_, PackData.FT_CHAR)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
                    errVer.set(__packer.unpackBool());
                } while(false);
            } catch(PackException e) {
                return __retcode != RetCode.RET_SUCCESS ? __retcode : RetCode.RET_INVALID;
            }
        } catch(PackException e) {
            return RetCode.RET_INVALID;
        }
        return __retcode;
    }
    public int setUserFrequentOrgDepartment(UserOrgDepartment data, long version, MutableLong newVersion, MutableBoolean errVer) {
        return setUserFrequentOrgDepartment(data, version, newVersion, errVer, DEFAULT_TIMEOUT, true);
    }
    public int setUserFrequentOrgDepartment(UserOrgDepartment data, long version, MutableLong newVersion, MutableBoolean errVer, int __timeout, boolean __checkLogin) {
        byte[] __reqdata = __packSetUserFrequentOrgDepartment(data, version);
        ResponseNode __rsp = invoke("Contacts", "setUserFrequentOrgDepartment", __reqdata, __timeout, __checkLogin);
        return __unpackSetUserFrequentOrgDepartment(__rsp, newVersion, errVer);
    }
    public boolean async_setUserFrequentOrgDepartment(UserOrgDepartment data, long version, SetUserFrequentOrgDepartmentCallback __callback) {
        return async_setUserFrequentOrgDepartment(data, version, __callback, DEFAULT_TIMEOUT, true);
    }
    public boolean async_setUserFrequentOrgDepartment(UserOrgDepartment data, long version, SetUserFrequentOrgDepartmentCallback __callback, int __timeout, boolean __checkLogin) {
        byte[] __reqdata = __packSetUserFrequentOrgDepartment(data, version);
        return asyncCall("Contacts", "setUserFrequentOrgDepartment", __reqdata, __callback, __timeout, __checkLogin);
    }
    public static byte[] __packChangeUserFrequentOrgDepartment(long orgId, long departmentId, boolean bAdd, long version) {
        PackData __packer = new PackData();
        int __packSize = 6;
        __packSize += PackData.getSize(orgId);
        __packSize += PackData.getSize(departmentId);
        __packSize += PackData.getSize(version);
        byte[] __reqdata = new byte[__packSize];
        __packer.resetOutBuff(__reqdata);
        byte __fieldNum = 4;
        __packer.packByte(__fieldNum);
        __packer.packByte(PackData.FT_NUMBER);
        __packer.packLong(orgId);
        __packer.packByte(PackData.FT_NUMBER);
        __packer.packLong(departmentId);
        __packer.packByte(PackData.FT_CHAR);
        __packer.packBool(bAdd);
        __packer.packByte(PackData.FT_NUMBER);
        __packer.packLong(version);
        return __reqdata;
    }
    public static int __unpackChangeUserFrequentOrgDepartment(ResponseNode __rsp, MutableLong newVersion, MutableBoolean errVer) {
        int __retcode = __rsp.getRetcode();
        if(__retcode != RetCode.RET_SUCCESS) return __retcode;
        PackData __packer = new PackData();
        byte[] __rspdata = __rsp.getRspdata();
        __packer.resetInBuff(__rspdata);
        try {
            __retcode = __packer.unpackInt();
            try {
                byte __num = __packer.unpackByte();
                FieldType __field;
                if(__num < 2) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
                __field = __packer.unpackFieldType();
                if(!PackData.matchType(__field.baseType_, PackData.FT_NUMBER)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
                newVersion.set(__packer.unpackLong());
                __field = __packer.unpackFieldType();
                if(!PackData.matchType(__field.baseType_, PackData.FT_CHAR)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
                errVer.set(__packer.unpackBool());
            } catch(PackException e) {
                return __retcode != RetCode.RET_SUCCESS ? __retcode : RetCode.RET_INVALID;
            }
        } catch(PackException e) {
            return RetCode.RET_INVALID;
        }
        return __retcode;
    }
    public int changeUserFrequentOrgDepartment(long orgId, long departmentId, boolean bAdd, long version, MutableLong newVersion, MutableBoolean errVer) {
        return changeUserFrequentOrgDepartment(orgId, departmentId, bAdd, version, newVersion, errVer, DEFAULT_TIMEOUT, true);
    }
    public int changeUserFrequentOrgDepartment(long orgId, long departmentId, boolean bAdd, long version, MutableLong newVersion, MutableBoolean errVer, int __timeout, boolean __checkLogin) {
        byte[] __reqdata = __packChangeUserFrequentOrgDepartment(orgId, departmentId, bAdd, version);
        ResponseNode __rsp = invoke("Contacts", "changeUserFrequentOrgDepartment", __reqdata, __timeout, __checkLogin);
        return __unpackChangeUserFrequentOrgDepartment(__rsp, newVersion, errVer);
    }
    public boolean async_changeUserFrequentOrgDepartment(long orgId, long departmentId, boolean bAdd, long version, ChangeUserFrequentOrgDepartmentCallback __callback) {
        return async_changeUserFrequentOrgDepartment(orgId, departmentId, bAdd, version, __callback, DEFAULT_TIMEOUT, true);
    }
    public boolean async_changeUserFrequentOrgDepartment(long orgId, long departmentId, boolean bAdd, long version, ChangeUserFrequentOrgDepartmentCallback __callback, int __timeout, boolean __checkLogin) {
        byte[] __reqdata = __packChangeUserFrequentOrgDepartment(orgId, departmentId, bAdd, version);
        return asyncCall("Contacts", "changeUserFrequentOrgDepartment", __reqdata, __callback, __timeout, __checkLogin);
    }
    public static byte[] __packGetOrgActiveUser(long orgId, long version) {
        PackData __packer = new PackData();
        int __packSize = 3;
        __packSize += PackData.getSize(orgId);
        __packSize += PackData.getSize(version);
        byte[] __reqdata = new byte[__packSize];
        __packer.resetOutBuff(__reqdata);
        byte __fieldNum = 2;
        __packer.packByte(__fieldNum);
        __packer.packByte(PackData.FT_NUMBER);
        __packer.packLong(orgId);
        __packer.packByte(PackData.FT_NUMBER);
        __packer.packLong(version);
        return __reqdata;
    }
    public static int __unpackGetOrgActiveUser(ResponseNode __rsp, ArrayList< String > uids, MutableLong newVersion) {
        int __retcode = __rsp.getRetcode();
        if(__retcode != RetCode.RET_SUCCESS) return __retcode;
        PackData __packer = new PackData();
        byte[] __rspdata = __rsp.getRspdata();
        __packer.resetInBuff(__rspdata);
        try {
            __retcode = __packer.unpackInt();
            try {
                byte __num = __packer.unpackByte();
                FieldType __field;
                if(__num < 2) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
                __field = __packer.unpackFieldType();
                if(!PackData.matchType(__field.baseType_, PackData.FT_VECTOR)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
                {
                    int size = __packer.unpackInt();
                    if(size > PackData.MAX_RECORD_SIZE || size < 0) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
                    uids.ensureCapacity(size);
                    for(int i = 0; i < size; i++) {
                        String tmpVal = null;
                        tmpVal = __packer.unpackString();
                        uids.add(tmpVal);
                    }
                }
                __field = __packer.unpackFieldType();
                if(!PackData.matchType(__field.baseType_, PackData.FT_NUMBER)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
                newVersion.set(__packer.unpackLong());
            } catch(PackException e) {
                return __retcode != RetCode.RET_SUCCESS ? __retcode : RetCode.RET_INVALID;
            }
        } catch(PackException e) {
            return RetCode.RET_INVALID;
        }
        return __retcode;
    }
    public int getOrgActiveUser(long orgId, long version, ArrayList< String > uids, MutableLong newVersion) {
        return getOrgActiveUser(orgId, version, uids, newVersion, DEFAULT_TIMEOUT, true);
    }
    public int getOrgActiveUser(long orgId, long version, ArrayList< String > uids, MutableLong newVersion, int __timeout, boolean __checkLogin) {
        byte[] __reqdata = __packGetOrgActiveUser(orgId, version);
        ResponseNode __rsp = invoke("Contacts", "getOrgActiveUser", __reqdata, __timeout, __checkLogin);
        return __unpackGetOrgActiveUser(__rsp, uids, newVersion);
    }
    public boolean async_getOrgActiveUser(long orgId, long version, GetOrgActiveUserCallback __callback) {
        return async_getOrgActiveUser(orgId, version, __callback, DEFAULT_TIMEOUT, true);
    }
    public boolean async_getOrgActiveUser(long orgId, long version, GetOrgActiveUserCallback __callback, int __timeout, boolean __checkLogin) {
        byte[] __reqdata = __packGetOrgActiveUser(orgId, version);
        return asyncCall("Contacts", "getOrgActiveUser", __reqdata, __callback, __timeout, __checkLogin);
    }
    public static byte[] __packNeedOrgDepartments(long orgId, long version, ArrayList< Long > departments) {
        PackData __packer = new PackData();
        int __packSize = 5;
        __packSize += PackData.getSize(orgId);
        __packSize += PackData.getSize(version);
        if(departments == null)
            __packSize++;
        else {
            __packSize += PackData.getSize(departments.size());
            for(int i = 0; i < departments.size(); i++) {
                __packSize += PackData.getSize((departments.get(i)));
            }
        }
        byte[] __reqdata = new byte[__packSize];
        __packer.resetOutBuff(__reqdata);
        byte __fieldNum = 3;
        __packer.packByte(__fieldNum);
        __packer.packByte(PackData.FT_NUMBER);
        __packer.packLong(orgId);
        __packer.packByte(PackData.FT_NUMBER);
        __packer.packLong(version);
        __packer.packByte(PackData.FT_VECTOR);
        __packer.packByte(PackData.FT_NUMBER);
        if(departments == null)
            __packer.packByte((byte)0);
        else {
            int len = departments.size();
            __packer.packInt(len);
            for(int i = 0; i < departments.size(); i++) {
                __packer.packLong((departments.get(i)).longValue());
            }
        }
        return __reqdata;
    }
    public static int __unpackNeedOrgDepartments(ResponseNode __rsp, MutableLong newVersion) {
        int __retcode = __rsp.getRetcode();
        if(__retcode != RetCode.RET_SUCCESS) return __retcode;
        PackData __packer = new PackData();
        byte[] __rspdata = __rsp.getRspdata();
        __packer.resetInBuff(__rspdata);
        try {
            __retcode = __packer.unpackInt();
            try {
                byte __num = __packer.unpackByte();
                FieldType __field;
                if(__num < 1) throw new PackException(PackData.PACK_LENGTH_ERROR, "PACK_LENGTH_ERROR");
                __field = __packer.unpackFieldType();
                if(!PackData.matchType(__field.baseType_, PackData.FT_NUMBER)) throw new PackException(PackData.PACK_TYPEMATCH_ERROR, "PACK_TYPEMATCH_ERROR");
                newVersion.set(__packer.unpackLong());
            } catch(PackException e) {
                return __retcode != RetCode.RET_SUCCESS ? __retcode : RetCode.RET_INVALID;
            }
        } catch(PackException e) {
            return RetCode.RET_INVALID;
        }
        return __retcode;
    }
    public int needOrgDepartments(long orgId, long version, ArrayList< Long > departments, MutableLong newVersion) {
        return needOrgDepartments(orgId, version, departments, newVersion, DEFAULT_TIMEOUT, true);
    }
    public int needOrgDepartments(long orgId, long version, ArrayList< Long > departments, MutableLong newVersion, int __timeout, boolean __checkLogin) {
        byte[] __reqdata = __packNeedOrgDepartments(orgId, version, departments);
        ResponseNode __rsp = invoke("Contacts", "needOrgDepartments", __reqdata, __timeout, __checkLogin);
        return __unpackNeedOrgDepartments(__rsp, newVersion);
    }
    public boolean async_needOrgDepartments(long orgId, long version, ArrayList< Long > departments, NeedOrgDepartmentsCallback __callback) {
        return async_needOrgDepartments(orgId, version, departments, __callback, DEFAULT_TIMEOUT, true);
    }
    public boolean async_needOrgDepartments(long orgId, long version, ArrayList< Long > departments, NeedOrgDepartmentsCallback __callback, int __timeout, boolean __checkLogin) {
        byte[] __reqdata = __packNeedOrgDepartments(orgId, version, departments);
        return asyncCall("Contacts", "needOrgDepartments", __reqdata, __callback, __timeout, __checkLogin);
    }
    public static byte[] __packNotifyOrgUsers(long orgId, long version, ArrayList< OrgDepartmentUser > datas) {
        PackData __packer = new PackData();
        int __packSize = 5;
        __packSize += PackData.getSize(orgId);
        __packSize += PackData.getSize(version);
        if(datas == null)
            __packSize++;
        else {
            __packSize += PackData.getSize(datas.size());
            for(int i = 0; i < datas.size(); i++) {
                __packSize += (datas.get(i)).size();
            }
        }
        byte[] __reqdata = new byte[__packSize];
        __packer.resetOutBuff(__reqdata);
        byte __fieldNum = 3;
        __packer.packByte(__fieldNum);
        __packer.packByte(PackData.FT_NUMBER);
        __packer.packLong(orgId);
        __packer.packByte(PackData.FT_NUMBER);
        __packer.packLong(version);
        __packer.packByte(PackData.FT_VECTOR);
        __packer.packByte(PackData.FT_STRUCT);
        if(datas == null)
            __packer.packByte((byte)0);
        else {
            int len = datas.size();
            __packer.packInt(len);
            for(int i = 0; i < datas.size(); i++) {
                (datas.get(i)).packData(__packer);
            }
        }
        return __reqdata;
    }
    public boolean notifyOrgUsers(long orgId, long version, ArrayList< OrgDepartmentUser > datas) {
        return notifyOrgUsers(orgId, version, datas, true);
    }
    public boolean notifyOrgUsers(long orgId, long version, ArrayList< OrgDepartmentUser > datas, boolean __checkLogin) {
        byte[] __reqdata = __packNotifyOrgUsers(orgId, version, datas);
        return notify("Contacts", "notifyOrgUsers", __reqdata, __checkLogin);
    }
    public static byte[] __packNotifyOrg(long orgId, long vestion) {
        PackData __packer = new PackData();
        int __packSize = 3;
        __packSize += PackData.getSize(orgId);
        __packSize += PackData.getSize(vestion);
        byte[] __reqdata = new byte[__packSize];
        __packer.resetOutBuff(__reqdata);
        byte __fieldNum = 2;
        __packer.packByte(__fieldNum);
        __packer.packByte(PackData.FT_NUMBER);
        __packer.packLong(orgId);
        __packer.packByte(PackData.FT_NUMBER);
        __packer.packLong(vestion);
        return __reqdata;
    }
    public boolean notifyOrg(long orgId, long vestion) {
        return notifyOrg(orgId, vestion, true);
    }
    public boolean notifyOrg(long orgId, long vestion, boolean __checkLogin) {
        byte[] __reqdata = __packNotifyOrg(orgId, vestion);
        return notify("Contacts", "notifyOrg", __reqdata, __checkLogin);
    }
}


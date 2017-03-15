package com.shinemo.contacts.db.entity;


import com.shinemo.contacts.db.generator.DaoSession;
import com.shinemo.contacts.db.generator.UserEntityDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;

/**
 * Created by yangqinlin on 16/12/5.
 */
@Entity(active = true, indexes = {
        @Index(value = "uid, departmentId, orgId", unique = true),
        @Index(value = "departmentId, orgId"),
        @Index(value = "uid, orgId")
})
public class UserEntity {

    @Index
    private String uid;

    @Index
    private long orgId;
    private long departmentId;
    private int sequence;

    @Index
    private String mobile;
    private String title;

    @Index
    private String name;
    private String pinyin;
    private int sex;

    @Index
    private String email;
    private String homePhone;
    private String personalCellPhone;
    private String shortNum;
    private String shortNum2;
    private String workPhone;
    private String workPhone2;
    private String virtualCellPhone;
    private String remark;
    private boolean isAllowLogin;
    private String virtualCode;
    private String fax;
    private String shortPinyin;
    private String customField;
    private String privilege;
    private String orgName;
    private boolean isLogin;

/** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;

/** Used for active entity operations. */
@Generated(hash = 1814575071)
private transient UserEntityDao myDao;
@Generated(hash = 1003242941)
public UserEntity(String uid, long orgId, long departmentId, int sequence,
        String mobile, String title, String name, String pinyin, int sex,
        String email, String homePhone, String personalCellPhone,
        String shortNum, String shortNum2, String workPhone, String workPhone2,
        String virtualCellPhone, String remark, boolean isAllowLogin,
        String virtualCode, String fax, String shortPinyin, String customField,
        String privilege, String orgName, boolean isLogin) {
    this.uid = uid;
    this.orgId = orgId;
    this.departmentId = departmentId;
    this.sequence = sequence;
    this.mobile = mobile;
    this.title = title;
    this.name = name;
    this.pinyin = pinyin;
    this.sex = sex;
    this.email = email;
    this.homePhone = homePhone;
    this.personalCellPhone = personalCellPhone;
    this.shortNum = shortNum;
    this.shortNum2 = shortNum2;
    this.workPhone = workPhone;
    this.workPhone2 = workPhone2;
    this.virtualCellPhone = virtualCellPhone;
    this.remark = remark;
    this.isAllowLogin = isAllowLogin;
    this.virtualCode = virtualCode;
    this.fax = fax;
    this.shortPinyin = shortPinyin;
    this.customField = customField;
    this.privilege = privilege;
    this.orgName = orgName;
    this.isLogin = isLogin;
}
@Generated(hash = 1433178141)
public UserEntity() {
}
public String getUid() {
    return this.uid;
}
public void setUid(String uid) {
    this.uid = uid;
}
public long getOrgId() {
    return this.orgId;
}
public void setOrgId(long orgId) {
    this.orgId = orgId;
}
public long getDepartmentId() {
    return this.departmentId;
}
public void setDepartmentId(long departmentId) {
    this.departmentId = departmentId;
}
public int getSequence() {
    return this.sequence;
}
public void setSequence(int sequence) {
    this.sequence = sequence;
}
public String getMobile() {
    return this.mobile;
}
public void setMobile(String mobile) {
    this.mobile = mobile;
}
public String getTitle() {
    return this.title;
}
public void setTitle(String title) {
    this.title = title;
}
public String getName() {
    return this.name;
}
public void setName(String name) {
    this.name = name;
}
public String getPinyin() {
    return this.pinyin;
}
public void setPinyin(String pinyin) {
    this.pinyin = pinyin;
}
public int getSex() {
    return this.sex;
}
public void setSex(int sex) {
    this.sex = sex;
}
public String getEmail() {
    return this.email;
}
public void setEmail(String email) {
    this.email = email;
}
public String getHomePhone() {
    return this.homePhone;
}
public void setHomePhone(String homePhone) {
    this.homePhone = homePhone;
}
public String getPersonalCellPhone() {
    return this.personalCellPhone;
}
public void setPersonalCellPhone(String personalCellPhone) {
    this.personalCellPhone = personalCellPhone;
}
public String getShortNum() {
    return this.shortNum;
}
public void setShortNum(String shortNum) {
    this.shortNum = shortNum;
}
public String getShortNum2() {
    return this.shortNum2;
}
public void setShortNum2(String shortNum2) {
    this.shortNum2 = shortNum2;
}
public String getWorkPhone() {
    return this.workPhone;
}
public void setWorkPhone(String workPhone) {
    this.workPhone = workPhone;
}
public String getWorkPhone2() {
    return this.workPhone2;
}
public void setWorkPhone2(String workPhone2) {
    this.workPhone2 = workPhone2;
}
public String getVirtualCellPhone() {
    return this.virtualCellPhone;
}
public void setVirtualCellPhone(String virtualCellPhone) {
    this.virtualCellPhone = virtualCellPhone;
}
public String getRemark() {
    return this.remark;
}
public void setRemark(String remark) {
    this.remark = remark;
}
public boolean getIsAllowLogin() {
    return this.isAllowLogin;
}
public void setIsAllowLogin(boolean isAllowLogin) {
    this.isAllowLogin = isAllowLogin;
}
public String getVirtualCode() {
    return this.virtualCode;
}
public void setVirtualCode(String virtualCode) {
    this.virtualCode = virtualCode;
}
public String getFax() {
    return this.fax;
}
public void setFax(String fax) {
    this.fax = fax;
}
public String getShortPinyin() {
    return this.shortPinyin;
}
public void setShortPinyin(String shortPinyin) {
    this.shortPinyin = shortPinyin;
}
public String getCustomField() {
    return this.customField;
}
public void setCustomField(String customField) {
    this.customField = customField;
}
public String getPrivilege() {
    return this.privilege;
}
public void setPrivilege(String privilege) {
    this.privilege = privilege;
}
public String getOrgName() {
    return this.orgName;
}
public void setOrgName(String orgName) {
    this.orgName = orgName;
}
public boolean getIsLogin() {
    return this.isLogin;
}
public void setIsLogin(boolean isLogin) {
    this.isLogin = isLogin;
}
/**
 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
 * Entity must attached to an entity context.
 */
@Generated(hash = 128553479)
public void delete() {
    if (myDao == null) {
        throw new DaoException("Entity is detached from DAO context");
    }
    myDao.delete(this);
}
/**
 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
 * Entity must attached to an entity context.
 */
@Generated(hash = 1942392019)
public void refresh() {
    if (myDao == null) {
        throw new DaoException("Entity is detached from DAO context");
    }
    myDao.refresh(this);
}
/**
 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
 * Entity must attached to an entity context.
 */
@Generated(hash = 713229351)
public void update() {
    if (myDao == null) {
        throw new DaoException("Entity is detached from DAO context");
    }
    myDao.update(this);
}
/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 287999134)
public void __setDaoSession(DaoSession daoSession) {
    this.daoSession = daoSession;
    myDao = daoSession != null ? daoSession.getUserEntityDao() : null;
}

}

package com.shinemo.contacts.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by yangqinlin on 16/12/5.
 */
@Entity
public class OrgEntity {
    @Id
    @Property(nameInDb = "ID")
    private long id;
    private String avatar;
    private String name;
    private int userType = -1;
    private String customerManager;
    private String customerManagerPhone;
    private long userVersion = -1;
    private boolean isAuth = true;
    @Generated(hash = 584938907)
    public OrgEntity(long id, String avatar, String name, int userType,
            String customerManager, String customerManagerPhone, long userVersion,
            boolean isAuth) {
        this.id = id;
        this.avatar = avatar;
        this.name = name;
        this.userType = userType;
        this.customerManager = customerManager;
        this.customerManagerPhone = customerManagerPhone;
        this.userVersion = userVersion;
        this.isAuth = isAuth;
    }
    @Generated(hash = 2061818262)
    public OrgEntity() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getAvatar() {
        return this.avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getUserType() {
        return this.userType;
    }
    public void setUserType(int userType) {
        this.userType = userType;
    }
    public String getCustomerManager() {
        return this.customerManager;
    }
    public void setCustomerManager(String customerManager) {
        this.customerManager = customerManager;
    }
    public String getCustomerManagerPhone() {
        return this.customerManagerPhone;
    }
    public void setCustomerManagerPhone(String customerManagerPhone) {
        this.customerManagerPhone = customerManagerPhone;
    }
    public long getUserVersion() {
        return this.userVersion;
    }
    public void setUserVersion(long userVersion) {
        this.userVersion = userVersion;
    }
    public boolean getIsAuth() {
        return this.isAuth;
    }
    public void setIsAuth(boolean isAuth) {
        this.isAuth = isAuth;
    }
}

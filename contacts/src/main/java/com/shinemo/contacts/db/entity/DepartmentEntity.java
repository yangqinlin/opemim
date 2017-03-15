package com.shinemo.contacts.db.entity;


import com.shinemo.contacts.db.generator.DaoSession;
import com.shinemo.contacts.db.generator.DepartmentEntityDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

/**
 * Created by yangqinlin on 16/12/5.
 */
@Entity(active = true, indexes = {
        @Index(value = "departmentId, orgId", unique = true),
        @Index(value = "parentId, orgId")
})
public class DepartmentEntity {

    @Id(autoincrement = true)
    private Long id;

    @Index
    private long orgId;
    private long departmentId;
    private long parentId;
    private long userCounts;
    private int sequence;
    private String name;
    private String description;
    private String parentIds;
    private String orgName;
    private long version = 0;

/** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;

/** Used for active entity operations. */
@Generated(hash = 983847591)
private transient DepartmentEntityDao myDao;
@Generated(hash = 1574596962)
public DepartmentEntity(Long id, long orgId, long departmentId, long parentId,
        long userCounts, int sequence, String name, String description,
        String parentIds, String orgName, long version) {
    this.id = id;
    this.orgId = orgId;
    this.departmentId = departmentId;
    this.parentId = parentId;
    this.userCounts = userCounts;
    this.sequence = sequence;
    this.name = name;
    this.description = description;
    this.parentIds = parentIds;
    this.orgName = orgName;
    this.version = version;
}
@Generated(hash = 768875561)
public DepartmentEntity() {
}
public Long getId() {
    return this.id;
}
public void setId(Long id) {
    this.id = id;
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
public long getParentId() {
    return this.parentId;
}
public void setParentId(long parentId) {
    this.parentId = parentId;
}
public long getUserCounts() {
    return this.userCounts;
}
public void setUserCounts(long userCounts) {
    this.userCounts = userCounts;
}
public int getSequence() {
    return this.sequence;
}
public void setSequence(int sequence) {
    this.sequence = sequence;
}
public String getName() {
    return this.name;
}
public void setName(String name) {
    this.name = name;
}
public String getDescription() {
    return this.description;
}
public void setDescription(String description) {
    this.description = description;
}
public String getParentIds() {
    return this.parentIds;
}
public void setParentIds(String parentIds) {
    this.parentIds = parentIds;
}
public String getOrgName() {
    return this.orgName;
}
public void setOrgName(String orgName) {
    this.orgName = orgName;
}
public long getVersion() {
    return this.version;
}
public void setVersion(long version) {
    this.version = version;
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
@Generated(hash = 2098133619)
public void __setDaoSession(DaoSession daoSession) {
    this.daoSession = daoSession;
    myDao = daoSession != null ? daoSession.getDepartmentEntityDao() : null;
}
}

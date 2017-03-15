package com.shinemo.contacts.db.generator;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.shinemo.contacts.db.entity.DepartmentEntity;
import com.shinemo.contacts.db.entity.OrgEntity;
import com.shinemo.contacts.db.entity.UserEntity;

import com.shinemo.contacts.db.generator.DepartmentEntityDao;
import com.shinemo.contacts.db.generator.OrgEntityDao;
import com.shinemo.contacts.db.generator.UserEntityDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig departmentEntityDaoConfig;
    private final DaoConfig orgEntityDaoConfig;
    private final DaoConfig userEntityDaoConfig;

    private final DepartmentEntityDao departmentEntityDao;
    private final OrgEntityDao orgEntityDao;
    private final UserEntityDao userEntityDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        departmentEntityDaoConfig = daoConfigMap.get(DepartmentEntityDao.class).clone();
        departmentEntityDaoConfig.initIdentityScope(type);

        orgEntityDaoConfig = daoConfigMap.get(OrgEntityDao.class).clone();
        orgEntityDaoConfig.initIdentityScope(type);

        userEntityDaoConfig = daoConfigMap.get(UserEntityDao.class).clone();
        userEntityDaoConfig.initIdentityScope(type);

        departmentEntityDao = new DepartmentEntityDao(departmentEntityDaoConfig, this);
        orgEntityDao = new OrgEntityDao(orgEntityDaoConfig, this);
        userEntityDao = new UserEntityDao(userEntityDaoConfig, this);

        registerDao(DepartmentEntity.class, departmentEntityDao);
        registerDao(OrgEntity.class, orgEntityDao);
        registerDao(UserEntity.class, userEntityDao);
    }
    
    public void clear() {
        departmentEntityDaoConfig.clearIdentityScope();
        orgEntityDaoConfig.clearIdentityScope();
        userEntityDaoConfig.clearIdentityScope();
    }

    public DepartmentEntityDao getDepartmentEntityDao() {
        return departmentEntityDao;
    }

    public OrgEntityDao getOrgEntityDao() {
        return orgEntityDao;
    }

    public UserEntityDao getUserEntityDao() {
        return userEntityDao;
    }

}

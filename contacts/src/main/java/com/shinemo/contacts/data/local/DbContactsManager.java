package com.shinemo.contacts.data.local;

import com.shinemo.contacts.db.DbManager;
import com.shinemo.contacts.db.entity.DepartmentEntity;
import com.shinemo.contacts.db.entity.OrgEntity;
import com.shinemo.contacts.db.entity.UserEntity;
import com.shinemo.contacts.db.generator.DaoSession;
import com.shinemo.contacts.db.generator.DepartmentEntityDao;
import com.shinemo.contacts.db.generator.OrgEntityDao;
import com.shinemo.contacts.db.generator.UserEntityDao;
import com.shinemo.openim.utils.CollectionUtil;
import com.shinemo.openim.utils.DbUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * DbContactsManager
 * Created by yanxinwei on 2016/12/6.
 */

public class DbContactsManager {

    private static DbContactsManager instance = null;
    
    private DbContactsManager() {
    }
    
    public static DbContactsManager getInstance() {
        if (instance == null) {
            synchronized (DbContactsManager.class) {
                if (instance == null) {
                    instance = new DbContactsManager();
                }
            }
        }
        return instance;
    }
    /**
     * 查询所有企业
     *
     * @return
     */
    public List<OrgEntity> queryAllOrg() {
        DaoSession session = DbManager.getInstance().getSession();
        OrgEntityDao dao = session.getOrgEntityDao();
        return dao.queryBuilder().list();
    }

    /**
     * 根据id查询指定企业
     * @param orgId 企业id
     * @return
     */
    public OrgEntity queryOrgById(long orgId) {
        OrgEntityDao dao = DbManager.getInstance().getSession().getOrgEntityDao();
        return dao.queryBuilder().where(OrgEntityDao.Properties.Id.eq(orgId)).unique();
    }

    /**
     * 根据企业id集合删除企业
     * @param orgIds 企业id集合
     *
     */
    public void deleteOrgByIds(List<Long> orgIds) {
        DaoSession session = DbManager.getInstance().getSession();
        OrgEntityDao orgDao = session.getOrgEntityDao();
        orgDao.deleteByKeyInTx(orgIds);

        deleteDeptByOrgIds(orgIds);

        deleteUsersByOrgIds(orgIds);
    }

    /**
     * 插入或更新单个企业
     * @param orgEntity 单个企业
     *
     */
    public void insertOrg(OrgEntity orgEntity) {
        OrgEntityDao dao = DbManager.getInstance().getSession().getOrgEntityDao();
        dao.insertOrReplace(orgEntity);
    }

    /**
     * 插入或更新企业集合
     * @param orgEntities 企业集合
     *
     */
    public void insertOrgs(List<OrgEntity> orgEntities) {
        OrgEntityDao dao = DbManager.getInstance().getSession().getOrgEntityDao();
        dao.insertOrReplaceInTx(orgEntities);
    }

    /**
     * 根据企业id集合删除部门
     * @param orgIds 企业id集合
     *
     */
    public void deleteDeptByOrgIds(List<Long> orgIds) {
        DepartmentEntityDao dao = DbManager.getInstance().getSession().getDepartmentEntityDao();
        if (orgIds.size() > DbManager.IN_MAX_COUNT) {
            List<List<Long>> splitLists = DbUtils.splitList(orgIds, DbManager.IN_MAX_COUNT);
            for (List<Long> ids : splitLists) {
                deleteDeptByOrgIds(dao, ids);
            }
        } else {
            deleteDeptByOrgIds(dao, orgIds);
        }

    }

    /**
     * 根据企业id集合删除部门
     * @param orgIds 企业id集合
     */
    private void deleteDeptByOrgIds(DepartmentEntityDao dao, List<Long> orgIds) {
        dao.queryBuilder().where(DepartmentEntityDao.Properties.OrgId.in(orgIds)).buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }

    /**
     * 根据企业id集合删除user表
     * @param orgIds 企业id集合
     */
    public void deleteUsersByOrgIds(List<Long> orgIds) {
        UserEntityDao dao = DbManager.getInstance().getSession().getUserEntityDao();
        if (orgIds.size() > DbManager.IN_MAX_COUNT) {
            List<List<Long>> splitLists = DbUtils.splitList(orgIds, DbManager.IN_MAX_COUNT);
            for (List<Long> ids : splitLists) {
                deleteUsersByOrgIds(dao, ids);
            }
        } else {
            deleteUsersByOrgIds(dao, orgIds);
        }
    }

    /**
     * 根据企业id集合删除user表
     * @param orgIds 企业id集合
     */
    private void deleteUsersByOrgIds(UserEntityDao dao, List<Long> orgIds) {
        dao.queryBuilder().where(UserEntityDao.Properties.OrgId.in(orgIds)).buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }

    /**
     * 插入或更新单个部门
     * @param entity 单个部门
     */
    public void insertDept(DepartmentEntity entity) {
        DepartmentEntityDao dao = DbManager.getInstance().getSession().getDepartmentEntityDao();
        dao.insertOrReplace(entity);
    }

    /**
     * 插入或更新部门集合
     * @param entities 部门集合
     */
    public void insertDepts(List<DepartmentEntity> entities) {
        DepartmentEntityDao dao = DbManager.getInstance().getSession().getDepartmentEntityDao();
        dao.insertOrReplaceInTx(entities);
    }

    /**
     * 插入或更新单个用户
     * @param entity 单个用户
     */
    public void insertUser(UserEntity entity) {
        UserEntityDao dao = DbManager.getInstance().getSession().getUserEntityDao();
        dao.insertOrReplace(entity);
    }

    /**
     * 插入或更新用户集合
     * @param entities 用户集合
     *
     */
    public void insertUsers(List<UserEntity> entities) {
        UserEntityDao dao = DbManager.getInstance().getSession().getUserEntityDao();
        dao.insertOrReplaceInTx(entities);
    }

    /**
     * 查询未完成更新的部门
     *
     * @return
     */
    public List<DepartmentEntity> queryUnFinishedDept() {
        DepartmentEntityDao dao = DbManager.getInstance().getSession().getDepartmentEntityDao();
        return dao.queryBuilder().where(DepartmentEntityDao.Properties.Version.eq(-1L)).list();
    }

    /**
     * 查询部门
     *
     * @return
     */
    public List<Long> queryUserDeptId(String uid) {
        UserEntityDao dao = DbManager.getInstance().getSession().getUserEntityDao();
        List<UserEntity> entities = dao.queryBuilder().where(UserEntityDao.Properties.Uid.eq(uid)).list();
        List<Long> deptId = new ArrayList<>();
        if (!CollectionUtil.isEmpty(entities)) {
            for (UserEntity entity : entities) {
                deptId.add(entity.getDepartmentId());
            }
        }
        return deptId;
    }

    /**
     * 查询部门
     *
     * @return
     */
    public List<UserEntity> queryUsersByDeptId(long departmentId) {
        UserEntityDao dao = DbManager.getInstance().getSession().getUserEntityDao();
        return dao.queryBuilder().where(UserEntityDao.Properties.DepartmentId.eq(departmentId)).list();
    }

}

package com.shinemo.contacts.data.notify;


import android.util.Log;

import com.shinemo.contacts.data.local.DbContactsManager;
import com.shinemo.contacts.db.entity.UserEntity;
import com.shinemo.contacts.model.mapper.ContactsMapper;
import com.shinemo.contacts.protocol.ContactsImpl;
import com.shinemo.contacts.protocol.OrgDepartmentUser;
import com.shinemo.contacts.protocol.User;
import com.shinemo.openim.utils.CollectionUtil;

import java.util.ArrayList;
import java.util.List;


public class PushContacts extends ContactsImpl {

    public PushContacts() {
        super();
    }

    @Override
    public void notifyOrgUsers(final long orgId, final long version, final ArrayList<OrgDepartmentUser> datas) {
        for (OrgDepartmentUser orgDepartmentUser : datas) {
            List<User> netUserList = orgDepartmentUser.getUsers();
            List<UserEntity> userEntities = new ArrayList<>();
            if(CollectionUtil.isNotEmpty(netUserList)) {
                for(User user : netUserList) {
                    UserEntity userEntity = ContactsMapper.INSTANCE.aceToDb(user);
                    userEntity.setOrgId(orgId);
                    userEntities.add(userEntity);
                }

                DbContactsManager.getInstance().insertUsers(userEntities);
            }
            Log.e("shinemo", "@@@@orgId: " + orgId +" user size: " + userEntities.size());
        }
    }
}

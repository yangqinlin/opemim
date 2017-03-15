package com.shinemo.contacts.model.mapper;

import com.shinemo.contacts.db.entity.DepartmentEntity;
import com.shinemo.contacts.db.entity.OrgEntity;
import com.shinemo.contacts.db.entity.UserEntity;
import com.shinemo.contacts.model.UserVo;
import com.shinemo.contacts.protocol.DepartmentVo;
import com.shinemo.contacts.protocol.OrgVo;
import com.shinemo.contacts.protocol.User;
import com.shinemo.openim.service.group.model.GroupMember;
import com.shinemo.openim.utils.CollectionUtil;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangqinlin on 16/12/7.
 */
@Mapper
public abstract class ContactsMapper {

    public static ContactsMapper INSTANCE = Mappers.getMapper(ContactsMapper.class);

    @Mappings({
            @Mapping(target = "avatar", source = "avatarPrefix")
    })
    public abstract OrgEntity aceToDb(OrgVo orgVo);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "orgId", ignore = true),
            @Mapping(target = "orgName", ignore = true),
            @Mapping(target = "departmentId", source = "id"),
            @Mapping(target = "description", source = "descript")
    })
    public abstract DepartmentEntity aceToDb(DepartmentVo departmentVo);

    @Mappings({
            @Mapping(target = "orgId", ignore = true),
            @Mapping(target = "orgName", ignore = true),
            @Mapping(target = "uid", source = "suid"),
            @Mapping(target = "isLogin", source = "isActive")
    })
    public abstract UserEntity aceToDb(User user);

    public abstract UserVo dbToUserVo(UserEntity userEntity);

    public List<UserVo> dbToUserVos(List<UserEntity> userEntities){
        List<UserVo> userVos = new ArrayList<>();
        if (!CollectionUtil.isEmpty(userEntities)) {
            for (UserEntity userEntity : userEntities) {
                userVos.add(dbToUserVo(userEntity));
            }
        }
        return userVos;
    }

    @Mappings({
            @Mapping(target = "groupId", ignore = true),
            @Mapping(target = "account", source = "uid"),
            @Mapping(target = "nick", source = "name")
    })
    public abstract GroupMember userVoToGroupMember(UserVo userVo);

    public List<GroupMember> userVoToGroupMembers(List<UserVo> userVos){
        List<GroupMember> groupMembers = new ArrayList<>();
        if (!CollectionUtil.isEmpty(userVos)) {
            for (UserVo userVo : userVos) {
                groupMembers.add(userVoToGroupMember(userVo));
            }
        }
        return groupMembers;
    }
}

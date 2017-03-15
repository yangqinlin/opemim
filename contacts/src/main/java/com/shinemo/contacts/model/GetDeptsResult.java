package com.shinemo.contacts.model;


import com.shinemo.contacts.protocol.DepartmentVo;

import java.util.ArrayList;

/**
 * Created by yangqinlin on 16/12/6.
 */

public class GetDeptsResult {

    public int retCode;
    public ArrayList<DepartmentVo> departmentVos;
    public long directDeptVersion;
}

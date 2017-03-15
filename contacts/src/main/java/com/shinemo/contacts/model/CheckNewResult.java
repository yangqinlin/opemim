package com.shinemo.contacts.model;


import com.shinemo.contacts.protocol.OrgConfVo;

import java.util.TreeMap;

/**
 * Created by yangqinlin on 16/12/6.
 */

public class CheckNewResult {

    public int retCode;
    public TreeMap<Long, Long> orgLastVerMap;
    public TreeMap<Long, Long> orgActiveVerMap;
    public TreeMap< Long,OrgConfVo> orgConfMap;
}

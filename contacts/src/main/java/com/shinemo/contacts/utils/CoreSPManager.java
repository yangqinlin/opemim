package com.shinemo.contacts.utils;

import android.text.TextUtils;

import com.shinemo.openim.Account;
import com.shinemo.openim.helper.AccountHelper;
import com.shinemo.openim.helper.SharedPrefsManager;

/**
 * Created by yangqinlin on 16/12/6.
 */

public class CoreSPManager extends SharedPrefsManager {

    private static CoreSPManager instance = null;

    private CoreSPManager() {
    }

    public static CoreSPManager getInstance() {
        if (instance == null) {
            synchronized (CoreSPManager.class) {
                if (instance == null) {
                    instance = new CoreSPManager();
                }
            }
        }
        return instance;
    }

    @Override
    protected String getSPName() {
        Account account = AccountHelper.getInstance().getAccount();
        if(account == null || TextUtils.isEmpty(account.getUserId())) return "";

        return "CORE_" + account.getUserId();
    }
}

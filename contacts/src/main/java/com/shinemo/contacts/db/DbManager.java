package com.shinemo.contacts.db;


import android.text.TextUtils;

import com.shinemo.contacts.db.generator.DaoMaster;
import com.shinemo.contacts.db.generator.DaoSession;
import com.shinemo.openim.Account;
import com.shinemo.openim.helper.AccountHelper;
import com.shinemo.openim.helper.ApplicationContext;

/**
 * Created by yangqinlin on 16/12/5.
 */

public class DbManager {

    public static final String DATABASE_NAME = "shinemo-contacts-";
    private static DbManager instance = null;

    public static final int IN_MAX_COUNT = 500;

    private DaoSession daoSession;
    private DaoMaster daoMaster;
    private String userId;

    private DbManager() {
    }

    public static DbManager getInstance() {
        if (instance == null) {
            synchronized (DbManager.class) {
                if (instance == null) {
                    instance = new DbManager();
                }
            }
        }
        return instance;
    }

    public synchronized void init(String uid) {
        if(!uid.equals(userId)) {
            if(daoMaster != null) {
                daoMaster.getDatabase().close();
            }
            daoMaster = new DaoMaster(new DbOpenHelper(ApplicationContext.getInstance(), DATABASE_NAME + uid).getWritableDatabase());
            userId = uid;
        }
        daoSession = daoMaster.newSession();
    }

    public synchronized DaoSession getSession() {
        if(daoSession == null) {
            Account account = AccountHelper.getInstance().getAccount();
            if(account != null && !TextUtils.isEmpty(account.getUserId())) {
                init(account.getUserId());
            } else {
                init("");//容错
            }
        }

        return daoSession;
    }
}

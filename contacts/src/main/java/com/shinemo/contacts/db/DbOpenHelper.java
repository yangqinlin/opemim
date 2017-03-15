package com.shinemo.contacts.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.shinemo.contacts.db.generator.DaoMaster;


/**
 * Created by yangqinlin on 16/12/5.
 */

public class DbOpenHelper extends DaoMaster.OpenHelper {

    public DbOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }
}

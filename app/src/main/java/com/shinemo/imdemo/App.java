package com.shinemo.imdemo;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Process;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.shinemo.MyService;
import com.shinemo.openim.helper.ApplicationContext;

import java.util.List;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if(shouldInit()) {
            ApplicationContext.context = this;
            startService(new Intent(this, MyService.class));
            Fresco.initialize(this);
            Stetho.initialize(Stetho.newInitializerBuilder(this).enableDumpapp(Stetho.defaultDumperPluginsProvider(this)).
                    enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this)).build());
        }
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();

        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }

        return false;
    }
}

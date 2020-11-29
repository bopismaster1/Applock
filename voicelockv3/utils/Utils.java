package com.voicelock.voicelockv3.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.List;

import io.paperdb.Paper;

import static android.app.AppOpsManager.MODE_ALLOWED;

public class Utils {

    private  String EXTRA_LAST_APP ="EXTRA_LAST_APP";
    private  Context context;

    public Utils(Context context) {
        this.context = context;
        Paper.init(context);
    }

    public boolean isLock(String packagename){
        return Paper.book().read(packagename) != null;

    }

    public void lock (String pk){
        Paper.book().write(pk,pk);
    }

    public void unlock(String pk){
        Paper.book().delete(pk);
    }

    public void setLastAPP(String pk){
        Paper.book().write(EXTRA_LAST_APP,pk);

    }

    public String getLastApp(){
        Log.e("PKG name:",EXTRA_LAST_APP+"sda");
        return Paper.book().read(EXTRA_LAST_APP);

    }
    public String samplegetLastApp(String pk){
        Log.e("PKG name:",EXTRA_LAST_APP+"sda");
        return Paper.book().read(EXTRA_LAST_APP);

    }

    public  void clearLastApp(){
        Paper.book().delete(EXTRA_LAST_APP);
    }



    public static boolean checkPermision(Context ctx){
        AppOpsManager appOpsManager= (AppOpsManager)ctx.getSystemService(Context.APP_OPS_SERVICE);

            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(),ctx.getPackageName());
            return  mode == MODE_ALLOWED;


    }

    UsageStatsManager usageStatsManager;


    public String getLauncherToApp(){

        ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
           usageStatsManager = (UsageStatsManager) context.getSystemService((context.USAGE_STATS_SERVICE));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Log.v("mssg","Getlauncherto app is called >= kitkiat");
            List<ActivityManager.RunningTaskInfo> taskInfoList = manager.getRunningTasks(1);
            if (null != taskInfoList && !taskInfoList.isEmpty()){
                Log.e(taskInfoList.get(0).topActivity.getPackageName(),taskInfoList.get(0).topActivity.getPackageName());
                    return taskInfoList.get(0).topActivity.getPackageName();


            }
        }else{
            long endtime = System.currentTimeMillis();
            long begintime = endtime-10000;
            Log.e("mssg","Getlauncherto app is called else kitkiat");
                String result="";
                UsageEvents.Event event = new UsageEvents.Event();
                UsageEvents usageEvents = usageStatsManager.queryEvents(begintime,endtime);
                while (usageEvents.hasNextEvent()){

                    usageEvents.getNextEvent(event);
                    if (event.getEventType() == UsageEvents.Event.FOREGROUND_SERVICE_START){
                        return event.getPackageName();
                    }
                }
                if (!TextUtils.isEmpty(result))
                    return result;


        }
        return "";
    }
}

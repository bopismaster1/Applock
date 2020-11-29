package com.voicelock.voicelockv3.services;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.voicelock.voicelockv3.broadcast.ReceiverApplock;

public class ServiceApplockJobIntent extends JobIntentService {

    private static final int  JOB_ID=15462;
    public static void enqueuework(Context context,Intent work){
        enqueueWork(context,ServiceApplockJobIntent.class,JOB_ID,work);

    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
    runApplock();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        BackgroundManager.getInstance().init(this).startAlarmmanager();

        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        BackgroundManager.getInstance().init(this).startAlarmmanager();
        super.onDestroy();
    }

    private void runApplock(){
        long endTime=System.currentTimeMillis()+210;
        while (System.currentTimeMillis()<endTime){
            synchronized (this){
                try {
                    Intent intent = new Intent(this, ReceiverApplock.class);
                    sendBroadcast(intent);
                    wait(endTime-System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}

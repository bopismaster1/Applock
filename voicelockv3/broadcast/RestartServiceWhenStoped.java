package com.voicelock.voicelockv3.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.voicelock.voicelockv3.sec_lock;
import com.voicelock.voicelockv3.utils.Utils;

public class RestartServiceWhenStoped extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Utils utils = new Utils(context);

        String appRunning = null;
            appRunning = utils.getLauncherToApp();
            Log.e("restart"," restart "+appRunning);


        if (utils.isLock(appRunning)){
            Log.e("naka lock",appRunning);
            if (!appRunning.equals(utils.getLastApp())){

                utils.clearLastApp();
                utils.setLastAPP(appRunning);

                Intent i = new Intent(context, sec_lock.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtra("broadcast_reciever","broadcast_reciever");
                context.startActivity(i);

            }
        }
    }
}

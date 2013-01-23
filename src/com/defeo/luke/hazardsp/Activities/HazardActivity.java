package com.defeo.luke.hazardsp.Activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import com.defeo.luke.hazardsp.Client.Client;
//import com.defeo.luke.hazardsp.Client.Client;

import java.util.List;

/**
 * @author Thomas Yorkshire
 */
public class HazardActivity extends Activity {

    public HazardActivity() {
        if (Client.get() != null) {
            Client.get().setCurrentActivity(this);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        Context context = getApplicationContext();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
//        if (!taskInfo.isEmpty()) {
//            ComponentName topActivity = taskInfo.get(0).topActivity;
//            if (!topActivity.getPackageName().equals(context.getPackageName())) {
//                ConnectActivity.mp.stop();
//            }
//        }
    }
}
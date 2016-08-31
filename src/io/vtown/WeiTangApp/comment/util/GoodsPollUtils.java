package io.vtown.WeiTangApp.comment.util;

import java.util.HashMap;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import io.vtown.WeiTangApp.service.SGoodsInf;

/**
 * 简单的轮询工具
 */
public class GoodsPollUtils {

    /**
     * @param context
     * @param seconds
     * @param cls
     * @param action
     */
    private static PendingIntent pendingIntent;
    private static AlarmManager manager;
    public static void startPollingService(Context context, String GoodsId) {
//		if(manager==null)
        manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SGoodsInf.class);
        intent.putExtra("goodid",GoodsId);
//        Bundle MyBund=new Bundle();
//        MyBund.putString("goodid",GoodsId);
//        intent.putExtra("ex",MyBund);
        intent.setAction(SGoodsInf.ACTION_START);
        pendingIntent = PendingIntent.getService(context, 2,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long triggerAtTime = SystemClock.elapsedRealtime();
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerAtTime,//ELAPSED_REALTIME
                10 * 1000, pendingIntent);
    }

    /**
     * @param context
     */
    public static void stopPollingService(Context context) {
        if (null == manager) {
            manager = (AlarmManager) context
                    .getSystemService(Context.ALARM_SERVICE);
        }
        Intent intent = new Intent(context, SGoodsInf.class);
        intent.setAction(SGoodsInf.ACTION_STOP);
        pendingIntent = PendingIntent.getService(context, 2,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.cancel(pendingIntent);

    }


}

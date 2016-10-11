package io.vtown.WeiTangApp.comment.util;

import io.vtown.WeiTangApp.event.receiver.NewMessageBroadcastReceiver;

import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.util.Log;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMGroupManager;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-7-21 下午5:11:10
 */
public class IMUtile {
    /**
     * 全局的监听
     */
    public NewMessageBroadcastReceiver myregist;

    /**
     * 全局的上下文
     */

    public Activity mPContext;

    public IntentFilter intentFilter;

    public IMUtile(NewMessageBroadcastReceiver myregist, Activity mPContext) {
        super();
        this.myregist = myregist;
        this.mPContext = mPContext;
        // 初始化广播接收器
        intentFilter = new IntentFilter(EMChatManager.getInstance()
                .getNewMessageBroadcastAction());
        intentFilter.setPriority(3);

    }

    public void Login(String username, String password) {
        onInit(mPContext);

        EMChatManager.getInstance().login(username, password, new EMCallBack() {

            @Override
            public void onError(int arg0, final String errorMsg) {
                mPContext.runOnUiThread(new Runnable() {
                    public void run() {

                    }
                });
            }

            @Override
            public void onProgress(int arg0, String arg1) {
            }

            @Override
            public void onSuccess() {
                mPContext.runOnUiThread(new Runnable() {
                    public void run() {
                        EMGroupManager.getInstance().loadAllGroups();
                        EMChatManager.getInstance().loadAllConversations();

                        mPContext.registerReceiver(myregist, intentFilter);
                        EMChat.getInstance().setAppInited();
                    }
                });

            }
        });
    }

    /**
     * 取消注销
     */

    public void ImLister_UnRegist() {
        mPContext.unregisterReceiver(myregist);

    }

    public void ImLister_Regist() {
        mPContext.registerReceiver(myregist, intentFilter);
    }

    public static void LoginOut() {
        EMChatManager.getInstance().logout(new EMCallBack() {

            @Override
            public void onSuccess() {

            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String error) {

            }
        });
    }

    public static synchronized boolean onInit(Context context) {

        // Context appContext = getApplicationContext();
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(context, pid);
        // 如果app启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process
        // name就立即返回

        if (processAppName == null
                || !processAppName.equalsIgnoreCase("io.vtown.WeiTangApp")) {
            // Log.e(TAG, "enter the service process!");
            // "com.easemob.chatuidemo"为demo的包名，换到自己项目中要改成自己包名

            // 则此application::onCreate 是被service 调用的，直接返回
            System.out
                    .println("~~~~~~~~~~~~~~~~~~~~~~未初始化环信~~~~~~~~~~~~~~~~~~");
            return false;
        }
        // 初始化环信SDK
        Log.d("DemoApplication", "Initialize EMChat SDK");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~初始化环信~~~~~~~~~~~~~~~~~~");
        EMChat.getInstance().init(context);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~已初始化环信~~~~~~~~~~~~~~~~~~");
        // 获取到EMChatOptions对象
        EMChatOptions options = EMChatManager.getInstance().getChatOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        // 设置收到消息是否有新消息通知，默认为true
        options.setNotificationEnable(false);
        // 设置收到消息是否有声音提示，默认为true
        options.setNoticeBySound(false);
        // 设置收到消息是否震动 默认为true
        options.setNoticedByVibrate(false);
        // 设置语音消息播放是否设置为扬声器播放 默认为true
        // options.setUseSpeaker(false);

        return true;
    }

    public static String getAppName(Context mContext, int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = mContext.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
                    .next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm
                            .getApplicationInfo(info.processName,
                                    PackageManager.GET_META_DATA));
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
            }
        }
        return processName;
    }
}

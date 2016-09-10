package io.vtown.WeiTangApp;

import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.PicImageItem;
import io.vtown.WeiTangApp.bean.bcomment.easy.shop.BShopGoods;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.event.receiver.NewMessageBroadcastReceiver;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.qiniu.android.storage.UploadManager;

public class BaseApplication extends Application {
    // private static final String CompressFormat = null;

    /**
     * 全局单例
     */
    private static BaseApplication instance = null;

    /**
     * 系统异常捕获
     */
    private AppException appException = AppException.getAppExceptionHandler();

    private List<BShopGoods> ZiYingShop_To_Ls = new ArrayList<BShopGoods>();

    NewMessageBroadcastReceiver msgReceiver = new NewMessageBroadcastReceiver();
    public int PicType;// 1==>selectpice；；；2==》goodshow;3==>goodshare
    //在点击图片 跳转到大图查看区域
    private List<PicImageItem> PicImages = new ArrayList<PicImageItem>();
    @Override
    public void onCreate() {
        super.onCreate();
        IIM();
        InItData();
        /**
         * 系统异常注册
         */
        Thread.setDefaultUncaughtExceptionHandler(appException);
    }

    private void IIM() {
        EMChat.getInstance().init(getApplicationContext());
        /**
         * debugMode == true 时为打开，SDK会在log里输入调试信息
         *
         * @param debugMode
         *            在做代码混淆的时候需要设置成false
         */
        EMChat.getInstance().setDebugMode(true);
        IntentFilter intentFilter = new IntentFilter(EMChatManager
                .getInstance().getNewMessageBroadcastAction());
        intentFilter.setPriority(3);
        registerReceiver(msgReceiver, intentFilter);
    }

    private void Init() {
        // boolean isLogin_Get = Spuit.IsLogin_Get(getApplicationContext());
        // if(isLogin_Get){
        // BUser user_Get = Spuit.User_Get(getApplicationContext());
        // String member_id = user_Get.getMember_id();
        JPushInterface.setDebugMode(false);
        JPushInterface.init(getApplicationContext());
        JPushInterface.setAliasAndTags(getApplicationContext(), "11", null,
                new TagAliasCallback() {
                    @Override
                    public void gotResult(int arg0, String arg1,
                                          Set<String> arg2) {

                    }
                });
        // }
    }

    /**
     * imagload配置文件
     */
    private void IImagLoad() {// PicCach
        ImageLoaderConfiguration config = new
                ImageLoaderConfiguration.Builder(
                getApplicationContext()).denyCacheImageMultipleSizesInMemory()
                .threadPriority(Thread.NORM_PRIORITY - 1)
                .denyCacheImageMultipleSizesInMemory()
                .discCache(new UnlimitedDiscCache(new
                        File(Constants.PicCach)))//自定义缓存路径,图片缓存到sd卡
                .memoryCacheExtraOptions(720, 1280)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13)
                .discCacheFileCount(200).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();

        ImageLoader.getInstance().init(config);

    }

    /**
     * I七牛
     */
    private void ILoadData() {

    }

    /**
     * 清除图片的缓存
     */
    public void ClearnImagview() {
        ImageLoader.getInstance().clearDiskCache(); // 清除本地缓存
    }

    /**
     * 获取application （单例）
     */
    public static BaseApplication GetInstance() {
        if (null == instance) {
            instance = new BaseApplication();
        }
        return instance;
    }

    /**
     * TODO设置全局变量
     */
    private void InItData() {
        IImagLoad();
        ILoadData();
    }

    /**
     * 获取App安装包信
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    /**
     * 设置图层达到夜间模式的效果
     */
    private void WindowBlack() {
        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP;
        params.y = 10;// 距离底部的距离是10像素 如果是 top 就是距离top是10像素

        View tv = new TextView(this);
        tv.setBackgroundColor(0x55000000);
        manager.addView(tv, params);
    }

    public synchronized boolean onInit(Context context) {
        Context appContext = getApplicationContext();
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果app启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process
        // name就立即返回

        if (processAppName == null
                || !processAppName.equalsIgnoreCase("com.example.chat")) {
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
        EMChat.getInstance().init(appContext);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~已初始化环信~~~~~~~~~~~~~~~~~~");
        // 获取到EMChatOptions对象
        EMChatOptions options = EMChatManager.getInstance().getChatOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        // 设置收到消息是否有新消息通知，默认为true
        options.setNotificationEnable(false);
        // 设置收到消息是否有声音提示，默认为true
        // options.setNoticeBySound(false);
        // 设置收到消息是否震动 默认为true
        options.setNoticedByVibrate(false);
        // 设置语音消息播放是否设置为扬声器播放 默认为true
        // options.setUseSpeaker(false);

        return true;
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this
                .getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
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

    public List<BShopGoods> getZiYingShop_To_Ls() {
        return ZiYingShop_To_Ls;
    }

    public void setZiYingShop_To_Ls(List<BShopGoods> ziYingShop_To_Ls) {
        ZiYingShop_To_Ls = ziYingShop_To_Ls;
    }

    public int getPicType() {
        return PicType;
    }

    public void setPicType(int picType) {
        PicType = picType;
    }

    public List<PicImageItem> getPicImages() {
        return PicImages;
    }

    public void setPicImages(List<PicImageItem> picImages) {
        PicImages = picImages;
    }
}

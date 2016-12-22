package io.vtown.WeiTangApp.comment.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.comment.util.image.BitmapBlurUtil;

/**
 * Created by datutu on 2016/10/8.
 */

public class ViewUtils {

    /**
     * 默认是文本文字的右边
     *
     * @param PcContext
     * @param Tv
     * @param ResourceIv
     * @param ivwidth
     */
    public static void SetIvOnTextview(Context PcContext, TextView Tv, int ResourceIv, int ivwidth) {
        Drawable drawable = PcContext.getResources().getDrawable(ResourceIv);
        drawable.setBounds(0, 0, ivwidth, 12);//必须设置图片大小，否则不显示
        Tv.setCompoundDrawables(null, null, drawable, null); //分别对应 左上右下
    }

    public static void SetGaoSi(Context PContext, final ImageView VV, int ResourceId) {
        final Bitmap mBitmap = StrUtils
                .drawableToBitmap(PContext.getResources()
                        .getDrawable(ResourceId));
        BitmapBlurUtil.addTask(mBitmap, new Handler() {

            @Override
            public void handleMessage(Message msg) {

                super.handleMessage(msg);

                Drawable drawable = (Drawable) msg.obj;

                VV.setImageDrawable(drawable);

                mBitmap.recycle();

            }

        });
    }

    /**
     * 判断 用户是否安装微信客户端
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断 用户是否安装QQ客户端
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;

                if (pn.equalsIgnoreCase("com.tencent.qqlite") || pn.equalsIgnoreCase("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *判断是否安装新浪微博客户端
     */
    public static boolean isWeiboInstalled(Context context) {
        PackageManager pm;
        if ((pm = context.getApplicationContext().getPackageManager()) == null) {
            return false;
        }
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for (PackageInfo info : packages) {
            String name = info.packageName.toLowerCase(Locale.ENGLISH);
            if ("com.sina.weibo".equals(name)) {
                return true;
            }
        }
        return false;
    }

}

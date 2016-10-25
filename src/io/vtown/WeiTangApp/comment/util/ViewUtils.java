package io.vtown.WeiTangApp.comment.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.comment.util.image.BitmapBlurUtil;

/**
 * Created by datutu on 2016/10/8.
 */

public class ViewUtils {

    /**
     * 默认是文本文字的右边
     * @param PcContext
     * @param Tv
     * @param ResourceIv
     * @param ivwidth
     */
    public static void SetIvOnTextview(Context PcContext, TextView Tv, int ResourceIv, int ivwidth ) {
        Drawable drawable = PcContext.getResources().getDrawable(ResourceIv);
        drawable.setBounds(0, 0, ivwidth, 12);//必须设置图片大小，否则不显示
        Tv.setCompoundDrawables(null, null, drawable, null); //分别对应 左上右下
    }

    public static void SetGaoSi(Context PContext, final ImageView VV, int ResourceId){
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
}

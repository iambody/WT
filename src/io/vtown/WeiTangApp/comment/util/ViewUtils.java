package io.vtown.WeiTangApp.comment.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

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
}

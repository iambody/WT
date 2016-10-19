package io.vtown.WeiTangApp.comment.util.ui;

import android.graphics.drawable.GradientDrawable;
import android.view.View;

/**
 * Created by datutu on 2016/10/19.
 */

public class UiHelper {
    /**
     * 不同的shape替换里面的色值
     */
    public static void SetShapeColor(View VV, int Color) {
        GradientDrawable myGrad = (GradientDrawable) VV.getBackground();
        myGrad.setColor(Color);
    }
}

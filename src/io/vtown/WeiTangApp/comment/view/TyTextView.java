package io.vtown.WeiTangApp.comment.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-7-13 下午8:06:47 设置自定义字体
 */
public class TyTextView extends TextView {
	public TyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fzxh.ttf"));
	}

	public TyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TyTextView(Context context) {
		super(context);
	}

//	public void setTypeface(Typeface tf, int style) {
//		if (style == Typeface.BOLD) {
//			super.setTypeface(Typeface.createFromAsset(
//					getContext().getAssets(), "fonts/fzxh_Bold.ttf"));
//		} else {
//			super.setTypeface(Typeface.createFromAsset(
//					getContext().getAssets(), "fonts/fzxh.ttf"));
//		}
//	}
}

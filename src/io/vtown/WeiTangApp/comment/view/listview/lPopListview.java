package io.vtown.WeiTangApp.comment.view.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-4-20 下午3:32:44
 * 
 */
public class lPopListview extends ListView {
	 
	public lPopListview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public lPopListview(Context context) {
		super(context);
	}

	public lPopListview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int maxWidth = meathureWidthByChilds() + getPaddingLeft()
				+ getPaddingRight();
		super.onMeasure(
				MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY),
				heightMeasureSpec);
	}

	public int meathureWidthByChilds() {
		int maxWidth = 0;
		int maxheight=0;//h
		View view = null;
		for (int i = 0; i < getAdapter().getCount(); i++) {
			view = getAdapter().getView(i, view, this);
			view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			if (view.getMeasuredWidth() > maxWidth) {
				maxWidth = view.getMeasuredWidth();
			 
			}
		}
		return maxWidth;
	}
}

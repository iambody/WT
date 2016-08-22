package io.vtown.WeiTangApp.comment.view;

import android.R.integer;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-8-13 下午4:24:00 可以获取滑动距离的ScrollView
 * 
 */
public class ScrollDistanceScrollView extends ScrollView {

	private OnGetDistanceListener mListener = null;
	private int OdlerYdistance = 0;

	public ScrollDistanceScrollView(Context context) {
		super(context);

	}

	public ScrollDistanceScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScrollDistanceScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {

		super.onScrollChanged(l, t, oldl, oldt);

		if (mListener != null) {
			mListener.getDistance(t+getHeight() - getVerticalScrollbarPosition());
			OdlerYdistance = t;

		}
	}

	public interface OnGetDistanceListener {
		public void getDistance(int distance);
	}

	public void SetOnGetDistanceListener(OnGetDistanceListener listener) {
		this.mListener = listener;
	}

}

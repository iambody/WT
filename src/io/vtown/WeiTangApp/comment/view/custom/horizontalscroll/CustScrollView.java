package io.vtown.WeiTangApp.comment.view.custom.horizontalscroll;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-4-27 下午3:00:28
 * 
 */
public class CustScrollView extends ScrollView {
	private GestureDetector mGestureDetector;

	public CustScrollView(Context context) {
		super(context);
		mGestureDetector = new GestureDetector(new VScrollDetector());
		setFadingEdgeLength(0);
	}

	public CustScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mGestureDetector = new GestureDetector(new VScrollDetector());
		setFadingEdgeLength(0);
	}

	public CustScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mGestureDetector = new GestureDetector(new VScrollDetector());
		setFadingEdgeLength(0);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev)
				&& mGestureDetector.onTouchEvent(ev);
	}

	// Return false if we're scrolling in the x direction
	class VScrollDetector extends SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if (Math.abs(distanceY) > Math.abs(distanceX)) {
				return true;
			}

			return false;
		}
	}
}

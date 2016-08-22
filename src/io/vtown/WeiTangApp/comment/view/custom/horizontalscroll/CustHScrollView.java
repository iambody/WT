package io.vtown.WeiTangApp.comment.view.custom.horizontalscroll;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-4-27 下午2:51:14
 * 
 */
public class CustHScrollView extends HorizontalScrollView {
	private GestureDetector mGestureDetector;
	private View.OnTouchListener mGestureListener;

	private static final String TAG = "CustomHScrollView";

	public CustHScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		mGestureDetector = new GestureDetector(new HScrollDetector());
		setFadingEdgeLength(0);
	}

	public CustHScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(new HScrollDetector());
		setFadingEdgeLength(0);
	}

	public CustHScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mGestureDetector = new GestureDetector(new HScrollDetector());
		setFadingEdgeLength(0);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev)
				&& mGestureDetector.onTouchEvent(ev);
	}

	class HScrollDetector extends SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if (Math.abs(distanceX) > Math.abs(distanceY)) {
				return true;
			}

			return false;
		}
	}
}

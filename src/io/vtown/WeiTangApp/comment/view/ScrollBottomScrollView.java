package io.vtown.WeiTangApp.comment.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-8-9 下午1:47:59
 * 
 */
public class ScrollBottomScrollView extends ScrollView {
	private ScrollBottomListener scrollBottomListener;

	public ScrollBottomScrollView(Context context) {
		super(context);
	}

	public ScrollBottomScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScrollBottomScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		if (t + getHeight() >= computeVerticalScrollRange() ) {
			// ScrollView滑动到底部了
			scrollBottomListener.scrollBottom();
		}else{
			scrollBottomListener.scrollUp();
		}
	}

	public void setScrollBottomListener(
			ScrollBottomListener scrollBottomListener) {
		this.scrollBottomListener = scrollBottomListener;
	}

	public interface ScrollBottomListener {
		public void scrollBottom();
		public void scrollUp();
	}
}

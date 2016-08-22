package io.vtown.WeiTangApp.comment.view.custom.horizontalscroll;

import java.util.List;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * 进行内容处理的抽象类
 * 
 */
public abstract class HBaseAdapter {
	private HorizontalScrollMenu mHorizontalScrollMenu;

	public abstract List<String> getMenuItems();

	public abstract List<View> getContentViews();
	
	 
	
	public abstract void onPageChanged(int position, boolean visitStatus);

	public void setHorizontalScrollMenu(
			HorizontalScrollMenu horizontalScrollMenu) {
		mHorizontalScrollMenu = horizontalScrollMenu;
	}

	public void notifyDataSetChanged() {
		mHorizontalScrollMenu.notifyDataSetChanged(this);
	}
}

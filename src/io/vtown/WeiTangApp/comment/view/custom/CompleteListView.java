package io.vtown.WeiTangApp.comment.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.ListView;

/**
 * 自适应listview
 * 
 * @author 大兔兔的账户
 * 
 */
public class CompleteListView extends ListView {
	public CompleteListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CompleteListView(Context context) {
		super(context);
	}

	public CompleteListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
/**
 * 
 */
package io.vtown.WeiTangApp.comment.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;
 
/**
 *@author 王永奎 E-mail:wangyk@nsecurities.cn
 *@department  
 *@version 创建时间：2015-12-22 下午1:44:20
 */

public class CustomExpandableListView extends ExpandableListView {

	public CustomExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

		MeasureSpec.AT_MOST);

		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}

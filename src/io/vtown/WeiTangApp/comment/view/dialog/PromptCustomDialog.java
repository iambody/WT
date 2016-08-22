package io.vtown.WeiTangApp.comment.view.dialog;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-8-7 下午7:09:56
 *  
 */
public class PromptCustomDialog extends Dialog implements android.view.View.OnClickListener {
	
	private Context mContext;
	private int mLayoutRes;
	private String mContentValue;
	/**
	 * dialog内容
	 */
	private TextView promptcustomdialog_content_value;
	/**
	 * 确认按钮
	 */
	private TextView promptcustomdialog_confirm;

	public PromptCustomDialog(Context context) {
		super(context);
		this.mContext = context;
		
	}
	/**
	 * @param context
	 * @param resLayout
	 */
	public PromptCustomDialog(Context context, int resLayout) {
		super(context);
		this.mContext = context;
		this.mLayoutRes = resLayout;

	}

	/**
	 * 
	 * @param context
	 * @param theme
	 * @param resLayout
	 */
	public PromptCustomDialog(Context context, int theme,String contentValue) {
		super(context, theme);
		this.mContext = context;
		this.mContentValue = contentValue;

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.prompt_custom_dialog);
		promptcustomdialog_content_value = (TextView) findViewById(R.id.promptcustomdialog_content_value);
		promptcustomdialog_confirm = (TextView) findViewById(R.id.promptcustomdialog_confirm);
		StrUtils.SetTxt(promptcustomdialog_content_value, mContentValue);
		promptcustomdialog_confirm.setOnClickListener(this);
	}
	
	public interface onDialogConfirmClick {
		void onConfirmCLick(View v);

	}

	
	private onDialogConfirmClick myclick;

	public void setConfirmListener(onDialogConfirmClick click) {
		this.myclick = click;
	}

	@Override
	public void onClick(View v) {
		if(null != myclick){
			myclick.onConfirmCLick(v);
		}
	}

	

}

package io.vtown.WeiTangApp.comment.view.dialog;

import io.vtown.WeiTangApp.R;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Description:自定义Dialog（参数传入Dialog样式文件，Dialog布局文件
 * 
 * @author 王永
 */
public class CustomDialog extends Dialog {
	private oncancelClick myclick1;

	public void setcancelListener(oncancelClick click) {
		this.myclick1 = click;
	}

	private onConfirmClick myclick;

	public void setConfirmListener(onConfirmClick click) {
		this.myclick = click;
	}

	private onbackclick myclick2;

	public void setbacklistener(onbackclick click) {
		this.myclick2 = click;
	}

	int layoutRes;// 布局文件
	Context context;
	/** 确定按钮 **/
	private Button confirmBtn;
	/** 取消按钮 **/
	private Button cancelBtn;
//	private Typeface tf;
	/**
	 * 直接
	 */
	private Button backBtn;
	public TextView mytitle;
	public boolean titleIsGone = true;
	private String positiveBtn = "确定";
	private String negativeBtn = "取消";

	// private String backBtn = "直接退出;
	private int Tage = 0;// 4时候是否取消委托
	/**
	 * 左上角的叉
	 */
	private ImageView iv_dialog_dismess;
	private TextView mytitle2;

	public TextView getMytitle() {
		return mytitle;
	}

	public void setMytitle(TextView mytitle) {
		this.mytitle = mytitle;
	}

	public TextView getMytitle2() {
		return mytitle2;
	}

	public void setMytitle2(TextView mytitle2) {
		this.mytitle2 = mytitle2;
	}

	public CustomDialog(Context context) {
		super(context);
		this.context = context;
	}

	/**
	 * @param context
	 * @param resLayout
	 */
	public CustomDialog(Context context, int resLayout) {
		super(context);
		this.context = context;
		this.layoutRes = resLayout;
		
	}

	/**
	 * 
	 * @param context
	 * @param theme
	 * @param resLayout
	 */
	public CustomDialog(Context context, int theme, int resLayout, int tag) {
		super(context, theme);
		this.context = context;
		this.layoutRes = resLayout;
		if (0 == tag) {// 0代表不显示title
			titleIsGone = false;
		}
		Window window = this.getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.main_menu_animstyless);
	}

	public CustomDialog(Context context, int theme, int resLayout, int tag,
			String negativeBtn, String positiveBtn) {
		this(context, theme, resLayout, tag);
		if (0 == tag) {// 0代表不显示title
			titleIsGone = false;
		}
		this.positiveBtn = positiveBtn;
		this.negativeBtn = negativeBtn;

	}

	public void SetbackView() {

		backBtn.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(layoutRes);

		confirmBtn = (Button) findViewById(R.id.confirm_btn);
		cancelBtn = (Button) findViewById(R.id.cancel_btn);
		backBtn = (Button) findViewById(R.id.back_btn);
		iv_dialog_dismess = (ImageView) findViewById(R.id.iv_dialog_dismess);
		if (null != iv_dialog_dismess) {
			iv_dialog_dismess.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					CustomDialog.this.dismiss();
				}
			});
		}

		backBtn.setVisibility(View.GONE);
		confirmBtn.setText(positiveBtn);
		cancelBtn.setText(negativeBtn);
		mytitle = (TextView) findViewById(R.id.mytitle);
		 
		mytitle2 = (TextView) findViewById(R.id.mytitle2);

		// if(!titleIsGone)mytitle.setVisibility(View.GONE);
		confirmBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (myclick != null) {
					myclick.onConfirmCLick(v);

				}

			}
		});
		cancelBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (myclick1 != null) {
					myclick1.oncancelClick(v);

				}

			}
		});
		backBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (myclick2 != null) {
					myclick2.back(v);
				}
			}
		});
		// 设置按钮的文本颜�?
//		confirmBtn.setTextColor(0xff1E90FF);
//		cancelBtn.setTextColor(0xff1E90FF);
		if (Tage == 4) {
			confirmBtn.setTextColor(context.getResources()
					.getColor(R.color.red));
		}
		// 为按钮绑定点击事件监听器

	}

	public interface onConfirmClick {
		void onConfirmCLick(View v);

	}

	public interface oncancelClick {
		void oncancelClick(View v);
	}

	public interface onbackclick {
		void back(View v);
	}

	public void setTitleText(String text) {
		mytitle.setText(text);
	}

	public void HindTitle2() {
		mytitle2.setVisibility(View.GONE);
	}

	public void Settitles(String tt) {
		mytitle2.setVisibility(View.VISIBLE);
		mytitle2.setText(tt);
	}

	public void setTitleText2(String text) {
		mytitle2.setText(text);
	}

}
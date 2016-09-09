package io.vtown.WeiTangApp.comment.view.pop;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.addgood.BCategory;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-6-14 下午12:35:00
 * 
 */
public class PAddGoosSort extends PopupWindow implements OnClickListener {
	/**
	 * 上下文
	 */
	private Context pContext;

	/**
	 * 基view
	 */
	private View BaseView;

	/**
	 * 外层的view
	 */
	private View addgoods_out;
	/**
	 * 内容的view
	 */
	private View addgoods_in;
	// 规格1名称
	private TextView tv_addgoodpop_rule1;
	// 规格2名称
	private TextView tv_addgoodpop_rule2;
	// 规格1的输入框
	private EditText et_addgoodpop_rule1;
	// 规格2的输入框
	private EditText et_addgoodpop_rule2;
	// 建议零售价输入框
	private EditText et_addgoodpop_retail_price;
	// 库存的的输入框
	private EditText et_addgoodpop_inventory;
	// 取消按钮
	private Button pop_addgoods_confirm_btn;
	// 确定按钮
	private Button pop_addgoods_cancel_btn;
	/**
	 * 获取的数据源
	 */
	private BCategory ResouceData = new BCategory();

	public PAddGoosSort(Context pContext, BCategory datBlCommen) {
		super();
		this.pContext = pContext;

		BaseView = LayoutInflater.from(pContext).inflate(
				R.layout.pop_addgoods_sort, null);
		ResouceData = datBlCommen;

		addgoods_out = BaseView.findViewById(R.id.addgoods_out);
		addgoods_in = BaseView.findViewById(R.id.addgoods_in);
		IPop();
		IView();
		IData(ResouceData);
		ITouch();
	}

	// 初始化组件
	private void IView() {

		tv_addgoodpop_rule1 = (TextView) BaseView
				.findViewById(R.id.tv_addgoodpop_rule1);
		et_addgoodpop_rule1 = (EditText) BaseView
				.findViewById(R.id.et_addgoodpop_rule1);
		tv_addgoodpop_rule2 = (TextView) BaseView
				.findViewById(R.id.tv_addgoodpop_rule2);
		et_addgoodpop_rule2 = (EditText) BaseView
				.findViewById(R.id.et_addgoodpop_rule2);
		et_addgoodpop_retail_price = (EditText) BaseView
				.findViewById(R.id.et_addgoodpop_retail_price);
		et_addgoodpop_inventory = (EditText) BaseView
				.findViewById(R.id.et_addgoodpop_inventory);
		pop_addgoods_cancel_btn = (Button) BaseView
				.findViewById(R.id.pop_addgoods_cancel_btn);
		pop_addgoods_confirm_btn = (Button) BaseView
				.findViewById(R.id.pop_addgoods_confirm_btn);
		pop_addgoods_cancel_btn.setOnClickListener(this);
		pop_addgoods_confirm_btn.setOnClickListener(this);

	}

	/**
	 * @param resouceData2
	 *            刷新POP数据
	 */
	private void IData(BCategory resouceData2) {
		tv_addgoodpop_rule1.setText(resouceData2.getAdd_good_attrs_name_1());
		tv_addgoodpop_rule2.setText(resouceData2.getAdd_good_attrs_name_2());
	}

	/**
	 * 初始化本弹出框的各种属性配置
	 */
	private void IPop() {
		setContentView(BaseView);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);

		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x00000000);
		setBackgroundDrawable(dw);
		this.setOutsideTouchable(true);
	}

	/**
	 * 触摸外层dissmis本弹出框操作
	 */
	private void ITouch() {
		addgoods_out.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				int Bottom = addgoods_in.getBottom();
				int Top = addgoods_in.getTop();
				int Left = addgoods_in.getLeft();
				int Right = addgoods_in.getRight();
				int y = (int) event.getY();
				int x = (int) event.getX();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y > Bottom || y < Top || x < Left || x > Right) {
						dismiss();
					}
				}
				return true;
			}
		});
	}

	@Override
	public void onClick(View V) {
		switch (V.getId()) {
		case R.id.pop_addgoods_cancel_btn:

			this.dismiss();

			break;

		case R.id.pop_addgoods_confirm_btn:
			if (getAttrsValues()) {
				this.dismiss();
			}

			break;

		default:
			break;
		}
	}

	/**
	 * 获取输入的值
	 */
	private boolean getAttrsValues() {
		String value1 = et_addgoodpop_rule1.getText().toString().trim();
		String value2 = et_addgoodpop_rule2.getText().toString().trim();
		String value3 = et_addgoodpop_retail_price.getText().toString().trim();
		String value4 = et_addgoodpop_inventory.getText().toString().trim();
		if (StrUtils.isEmpty(value1)) {
			PromptManager.ShowMyToast(pContext,
					"请输入" + ResouceData.getAdd_good_attrs_name_1());
			return false;
		}
		if (StrUtils.isEmpty(value2)) {
			PromptManager.ShowMyToast(pContext,
					"请输入" + ResouceData.getAdd_good_attrs_name_2());
			return false;
		}
		if (StrUtils.isEmpty(value3)) {
			PromptManager.ShowMyToast(pContext, "请输入建议零售价");
			return false;
		}

		if (StrUtils.isEmpty(value4)) {
			PromptManager.ShowMyToast(pContext, "请输入库存");
			return false;
		}

		BCategory mComment = new BCategory();

		mComment.setAdd_good_attrs_value_1(value1);
		mComment.setAdd_good_attrs_value_2(value2);
		mComment.setAdd_good_attrs_value_3(String.valueOf(Float.valueOf(value3)*100));
		mComment.setAdd_good_attrs_value_4(value4);
		mComment.setAdd_good_attrs_id_1(ResouceData.getAdd_good_attrs_id_1());
		mComment.setAdd_good_attrs_id_2(ResouceData.getAdd_good_attrs_id_2());
		mComment.setAdd_good_attrs_name_1(ResouceData
				.getAdd_good_attrs_name_1());
		mComment.setAdd_good_attrs_name_2(ResouceData
				.getAdd_good_attrs_name_2());

		if (null != myListener)
			myListener.sendData(mComment);

		return true;

	}

	private OnPopupListener myListener;

	public interface OnPopupListener {
		void sendData(BCategory ResouceData);
	}

	public void SetOnPopupListener(OnPopupListener listener) {
		myListener = listener;
	}

}

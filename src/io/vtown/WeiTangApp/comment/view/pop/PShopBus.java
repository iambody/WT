package io.vtown.WeiTangApp.comment.view.pop;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcache.BHome;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-5-19 上午10:22:52
 * 
 */
public class PShopBus extends PopupWindow implements OnClickListener {
	/**
	 * 上下文
	 */
	private Context pContext;

	/**
	 * 基view
	 */
	private View BaseView;

	/**
	 * 回掉接口
	 * 
	 * @param pContext
	 */
	private BusSelecListener mClickListener;
	/**
	 * 零售接口
	 */
	private TextView pop_shopbus_lingshou, pop_shopbus_caigou;
	/**
	 * 零售商品
	 */
	public static final int Type_LingShou = 202;
	/**
	 * 采购商品
	 */
	public static final int Type_CaiGou = 203;
	/**
	 * 1标识只有零售商品 2标识只有采购商品 3标识零售商品和采购商品全部都有
	 */
	private int ShowTyp;

	/**
	 * 暴露接口
	 */
	public void GetSelectReult(BusSelecListener listener) {
		this.mClickListener = listener;
	}

	public PShopBus(Context pContext, int Type) {
		super();
		this.pContext = pContext;
		this.ShowTyp = Type;
		BaseView = LayoutInflater.from(pContext).inflate(
				R.layout.pop_select_shopbus, null);
		IPop();
		IView();
	}

	private void IView() {
		pop_shopbus_lingshou = ViewHolder.get(BaseView,
				R.id.pop_shopbus_lingshou);
		pop_shopbus_caigou = ViewHolder.get(BaseView, R.id.pop_shopbus_caigou);
		pop_shopbus_lingshou.setOnClickListener(this);
		pop_shopbus_caigou.setOnClickListener(this);
	}

	/**
	 * 初始化
	 */
	private void IPop() {
		BaseView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		setContentView(BaseView);
		setWidth(LayoutParams.WRAP_CONTENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		setBackgroundDrawable(dw);

		this.setOutsideTouchable(true);
	}

	public interface BusSelecListener {
		void GetResult(int type);

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		 
		case R.id.pop_shopbus_lingshou:
			mClickListener.GetResult(Type_LingShou);
			this.dismiss();
			break;
		case R.id.pop_shopbus_caigou:
			mClickListener.GetResult(Type_CaiGou);
			this.dismiss();
			break;
		default:
			break;
		}
	}
}

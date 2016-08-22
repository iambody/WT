package io.vtown.WeiTangApp.ui.afragment;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.DimensionPixelUtil;
import io.vtown.WeiTangApp.comment.view.custom.horizontalscroll.HBaseAdapter;
import io.vtown.WeiTangApp.comment.view.custom.horizontalscroll.HorizontalScrollMenu;
import io.vtown.WeiTangApp.fragment.FBase;
import io.vtown.WeiTangApp.fragment.FShopPurchase;
import io.vtown.WeiTangApp.ui.ABaseFragment;
import io.vtown.WeiTangApp.ui.title.mynew.ANew;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.greenrobot.event.EventBus;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-5-9 下午5:26:39
 * @author 商铺里面的 我的采购单 管理
 * 
 */
public class AShopPurchase extends ABaseFragment implements OnClickListener {
	/**
	 * 滑动2组建
	 */
	private HorizontalScrollMenu activity_f_shop_purchase_container;
	/**
	 * fragment组 全部，代付款，已付款，待收货，退货
	 */
	private FShopPurchase FPurchaseAll, FPurchaseDaiFu, FPurchaseYiFa,
			FPurchaseYiShou, FPurchaseTuiKuan, FPurchaseClose;
	/**
	 * 碎片管理栈
	 */
	private List<FShopPurchase> FragmentLss;
	/**
	 * 头部的title
	 */
	private String[] TitleNames = new String[] { "全部", "待付款", "已付款", "待收货",
			"退款/仲裁", "已完成" };

	/**
	 * 当前的Postion
	 */
	private int CurrentPostion = 0;
	/**
	 * 标记位置
	 */
	private boolean IsIn = false;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.fragment_shop_purchase);
		initView();
	}

	private void initView() {
		Ifragment();
		activity_f_shop_purchase_container = (HorizontalScrollMenu) findViewById(R.id.activity_f_shop_purchase_container);
		activity_f_shop_purchase_container.setSwiped(true);
		activity_f_shop_purchase_container.setAdapter(new MenusssAdapter());
		// activity_f_shop_purchase_container.set
	}

	private void Ifragment() {// 创建0
		FPurchaseAll = FShopPurchase.newInstance(FShopPurchase.PAll);
		// 创建1
		FPurchaseDaiFu = FShopPurchase.newInstance(FShopPurchase.PDaiFu);
		// 创建3
		FPurchaseYiFa = FShopPurchase.newInstance(FShopPurchase.PYiFu);
		// 创建4
		FPurchaseYiShou = FShopPurchase.newInstance(FShopPurchase.PDaiShou);
		// 创建5
		FPurchaseTuiKuan = FShopPurchase.newInstance(FShopPurchase.PTuiKuan);
		// 已关闭
		FPurchaseClose = FShopPurchase.newInstance(FShopPurchase.PClose);

		FragmentLss = new ArrayList<FShopPurchase>();
		// 添加
		FragmentLss.add(FPurchaseAll);
		FragmentLss.add(FPurchaseDaiFu);
		FragmentLss.add(FPurchaseYiFa);
		FragmentLss.add(FPurchaseYiShou);
		FragmentLss.add(FPurchaseTuiKuan);
		FragmentLss.add(FPurchaseClose);
		// s设置当前cureentfragment
		setCurrentFragment(FPurchaseAll);

	}

	@Override
	protected void InitTile() {
		SetTitleTxt(getResources().getString(R.string.shop_oder_purchase));
		ImageView right_right_iv = (ImageView) findViewById(R.id.right_right_iv);
		right_right_iv.setVisibility(View.VISIBLE);
		right_right_iv.setImageDrawable(getResources().getDrawable(
				R.drawable.new1));
		right_right_iv.setOnClickListener(this);
	}

	private class MenusssAdapter extends HBaseAdapter {

		public MenusssAdapter() {
			super();
		}

		int MyScreenWidth = screenWidth;

		@Override
		public List<String> getMenuItems() {
			return Arrays.asList(TitleNames);
		}

		@Override
		public List<View> getContentViews() {
			List<View> views = new ArrayList<View>();
			for (String str : TitleNames) {
				View v = LayoutInflater.from(BaseContext).inflate(
						R.layout.content_view, null);
				TextView tv = (TextView) v.findViewById(R.id.tv_content);
				tv.setText(str);
				LinearLayout.LayoutParams ps = new LinearLayout.LayoutParams(
						120, DimensionPixelUtil.dip2px(BaseContext, 50));
				ps.setMargins(16, 10, 16, 10);
				v.setLayoutParams(ps);
				views.add(v);
			}
			return views;
		}

		@Override
		public void onPageChanged(int position, boolean visitStatus) {

			if (!IsIn) {
				IsIn = true;
				switchContent1(FPurchaseAll,
						R.id.activity_f_shop_purchase_frgment);

			} else {
				if (position == 0) {
					BMessage message = new BMessage(701);

					EventBus.getDefault().post(message);
				}
				if (position == 1) {
					BMessage message = new BMessage(702);

					EventBus.getDefault().post(message);
				}
				switchContent(FragmentLss.get(CurrentPostion),
						FragmentLss.get(position),
						R.id.activity_f_shop_purchase_frgment);
				setCurrentFragment(FragmentLss.get(position));
				CurrentPostion = position;
			}

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.right_right_iv:// 标题栏消息按钮
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
					ANew.class));
			break;

		default:
			break;
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			EventBus.getDefault().unregister(this);
		} catch (Exception e) {
		}
	}

	@Override
	protected void NetConnect() {

		NetError.setVisibility(View.GONE);
		int ket_Tage = FShopPurchase.GetKetTage();
		switch (ket_Tage) {
		case FShopPurchase.PAll:
			FPurchaseAll.RegetData();
			break;
		case FShopPurchase.PDaiFu:
			FPurchaseDaiFu.RegetData();
			break;
		case FShopPurchase.PYiFu:
			FPurchaseYiFa.RegetData();
			break;
		case FShopPurchase.PDaiShou:
			FPurchaseYiShou.RegetData();
			break;
		case FShopPurchase.PTuiKuan:
			FPurchaseTuiKuan.RegetData();
			break;
		case FShopPurchase.PClose:
			FPurchaseClose.RegetData();
			break;

		default:
			break;
		}
	}

	@Override
	protected void NetDisConnect() {
		NetError.setVisibility(View.VISIBLE);
	}

	@Override
	protected void SetNetView() {
		SetNetStatuse(NetError);
	}
}

package io.vtown.WeiTangApp.ui.afragment;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.DimensionPixelUtil;
import io.vtown.WeiTangApp.comment.view.custom.horizontalscroll.HBaseAdapter;
import io.vtown.WeiTangApp.comment.view.custom.horizontalscroll.HorizontalScrollMenu;
import io.vtown.WeiTangApp.fragment.FBase;
import io.vtown.WeiTangApp.fragment.FShopOderManger;
import io.vtown.WeiTangApp.ui.ABaseFragment;
import io.vtown.WeiTangApp.ui.title.mynew.ANew;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jauker.widget.BadgeView;

import de.greenrobot.event.EventBus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-5-6 下午4:51:24
 * @author 商铺里面的商品管理
 */
public class AShopOderManage extends ABaseFragment implements OnClickListener {
	/**
	 * 滑动2组建
	 */
	private HorizontalScrollMenu hsm_container;
	/**
	 * fragment组
	 */
	private FShopOderManger FAll, FDaiFu, FDaiFa, FYiFa, FTuiKuan, FClose;
	/**
	 * 碎片管理栈
	 */
	private List<FShopOderManger> FragmentLs;
	/**
	 * 头部的title
	 */
	private String[] TitleNames = new String[] { "全部", "待付款", "待发货", "已发货",
			"退款/仲裁", "已完成" };

	/**
	 * 当前的Postion
	 */
	private int CurrentPostion = 0;
	/**
	 * 标记位置
	 */

	private boolean IsIn = false;
	/**
	 * 订单状态
	 */
	private int order_stutas = 0;
	private ImageView right_right_iv;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_f_shop_odermanage);
		initView();
	}

	protected void InitTile() {
		SetTitleTxt(getResources().getString(R.string.shop_oder_manger));
		right_right_iv = (ImageView) findViewById(R.id.right_right_iv);
		right_right_iv.setVisibility(View.VISIBLE);
		right_right_iv.setImageDrawable(getResources().getDrawable(
				R.drawable.new1));
		
		right_right_iv.setOnClickListener(this);
	}

	public void initView() {
		Ifragment();
		hsm_container = (HorizontalScrollMenu) findViewById(R.id.activity_f_shop_odermanger_container);
		hsm_container.setSwiped(false);
		hsm_container.setAdapter(new MenuAdapter());
	}

	private void Ifragment() {
		// 创建0
		FAll = FShopOderManger.newInstance(0);
		// 创建1
		FDaiFu = FShopOderManger.newInstance(10);
		// 创建2
		FDaiFa = FShopOderManger.newInstance(20);
		// 创建3
		FYiFa = FShopOderManger.newInstance(30);
		// 创建4
		FTuiKuan = FShopOderManger.newInstance(40);
		// 创建6
		FClose = FShopOderManger.newInstance(100);

		FragmentLs = new ArrayList<FShopOderManger>();
		// 添加
		FragmentLs.add(FAll);
		FragmentLs.add(FDaiFu);
		FragmentLs.add(FDaiFa);
		FragmentLs.add(FYiFa);
		FragmentLs.add(FTuiKuan);
		FragmentLs.add(FClose);
		// s设置当前cureentfragment
		setCurrentFragment(FragmentLs.get(order_stutas));
	}
	

	class MenuAdapter extends HBaseAdapter {

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
				switchContent1(FAll, R.id.activity_f_shop_odermanger_frgment);
				return;
			}
			switchContent(FragmentLs.get(CurrentPostion),
					FragmentLs.get(position),
					R.id.activity_f_shop_odermanger_frgment);
			setCurrentFragment(FragmentLs.get(position));
			CurrentPostion = position;
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
	protected void NetConnect() {

		NetError.setVisibility(View.GONE);
		int ket_tage = FShopOderManger.GetKetTage();
		switch (ket_tage) {
		case 0:
			FAll.RegetData();
			break;

		case 10:
			FDaiFu.RegetData();
			break;
		case 20:
			FDaiFa.RegetData();
			break;
		case 30:
			FYiFa.RegetData();
			break;
		case 40:
			FTuiKuan.RegetData();
			break;

		case 100:
			FClose.RegetData();
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

	@Override
	protected void onDestroy() {

		super.onDestroy();
		try {
			EventBus.getDefault().unregister(this);
		} catch (Exception e) {
		}
		FragmentLs.clear();
	}
}

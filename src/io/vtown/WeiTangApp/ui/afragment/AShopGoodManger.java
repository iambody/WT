package io.vtown.WeiTangApp.ui.afragment;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.comment.util.DimensionPixelUtil;
import io.vtown.WeiTangApp.comment.view.custom.horizontalscroll.HBaseAdapter;
import io.vtown.WeiTangApp.comment.view.custom.horizontalscroll.HorizontalScrollMenu;
import io.vtown.WeiTangApp.fragment.FBase;
import io.vtown.WeiTangApp.fragment.FShopGoodManger;
import io.vtown.WeiTangApp.ui.ABaseFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.greenrobot.event.EventBus;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-5-12 下午6:22:54 商品管理页面
 */
public class AShopGoodManger extends ABaseFragment {

	private HorizontalScrollMenu activity_f_good_odermanger_container;
	/**
	 * 头部的title
	 */
	private String[] TitleNames = new String[] { "在售中", "已下架", "品牌商品", "垃圾桶" };

	/**
	 * 碎片管理栈
	 */
	private List<FBase> FragmentLs;
	/**
	 * fragment组
	 */
	private FShopGoodManger FSelling, FSellOut, FBrand, FRecycle;
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
		Ifragment();
		setContentView(R.layout.activity_shop_good_manger);
		initView();

	}

	private void initView() {
		activity_f_good_odermanger_container = (HorizontalScrollMenu) findViewById(R.id.activity_f_good_odermanger_container);
		activity_f_good_odermanger_container.setSwiped(true);
		activity_f_good_odermanger_container.setAdapter(new MenuAdapter());
		activity_f_good_odermanger_container.initMenuItems3(BaseContext,
				screenWidth / 4);
	}

	private void Ifragment() {
		FSelling = FShopGoodManger.newInstance(100);

		// 创建1
		FSellOut = FShopGoodManger.newInstance(20);

		// 创建2
		FBrand = FShopGoodManger.newInstance(0);
		// 创建3
		FRecycle = FShopGoodManger.newInstance(1);

		FragmentLs = new ArrayList<FBase>();
		// 添加
		FragmentLs.add(FSelling);
		FragmentLs.add(FSellOut);
		FragmentLs.add(FBrand);
		FragmentLs.add(FRecycle);

		// s设置当前cureentfragment
		setCurrentFragment(FSelling);
	}

	@Override
	protected void InitTile() {
		SetTitleTxt(getResources().getString(R.string.shop_good_manger));
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
			// PromptManager.ShowCustomToast(BaseContext, "页数" + position);
			if (!IsIn) {
				IsIn = true;
				switchContent1(FSelling,
						R.id.activity_f_shop_goodmanger_frgment);
				return;
			}
			switchContent(FragmentLs.get(CurrentPostion),
					FragmentLs.get(position),
					R.id.activity_f_shop_goodmanger_frgment);
			setCurrentFragment(FragmentLs.get(position));
			CurrentPostion = position;
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

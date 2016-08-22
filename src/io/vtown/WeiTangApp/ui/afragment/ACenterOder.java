package io.vtown.WeiTangApp.ui.afragment;

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

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.DimensionPixelUtil;
import io.vtown.WeiTangApp.comment.view.custom.horizontalscroll.HBaseAdapter;
import io.vtown.WeiTangApp.comment.view.custom.horizontalscroll.HorizontalScrollMenu;
import io.vtown.WeiTangApp.fragment.FBase;
import io.vtown.WeiTangApp.fragment.FCenterOder;
import io.vtown.WeiTangApp.ui.ABaseFragment;
import io.vtown.WeiTangApp.ui.title.mynew.ANew;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-5-19 下午1:38:10
 * 
 */
public class ACenterOder extends ABaseFragment implements OnClickListener {
	/**
	 * 滑动2组建
	 */
	private HorizontalScrollMenu hsm_container;
	/**
	 * fragment组
	 */
	private FCenterOder FAll, FDaiFu, FYiFu, FDaiShou, FTuiKuan, FClose;

	/**
	 * 碎片管理栈
	 */
	private List<FCenterOder> FragmentLs;
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
		setContentView(R.layout.activity_f_center_oder);
		initView();
	}

	@Override
	protected void InitTile() {
		SetTitleTxt(getResources().getString(R.string.center_oder));
		ImageView right_right_iv = (ImageView) findViewById(R.id.right_right_iv);
		right_right_iv.setVisibility(View.VISIBLE);
		right_right_iv.setImageDrawable(getResources().getDrawable(
				R.drawable.new1));
		right_right_iv.setOnClickListener(this);
	}

	public void initView() {
		Ifragment();
		hsm_container = (HorizontalScrollMenu) findViewById(R.id.activity_f_center_oder_container);
		hsm_container.setSwiped(true);
		hsm_container.setAdapter(new MenuAdapter());
	}

	private void Ifragment() {
		// 创建0
		FAll = FCenterOder.newInstance(FCenterOder.PAll);
		// 创建1
		FDaiFu = FCenterOder.newInstance(FCenterOder.PDaiFu);

		// 创建3
		FYiFu = FCenterOder.newInstance(FCenterOder.PYiFu);
		// 创建4
		FDaiShou = FCenterOder.newInstance(FCenterOder.PDaiShou);
		// 创建5
		FTuiKuan = FCenterOder.newInstance(FCenterOder.PTuiKuan);

		// 创建6
		FClose = FCenterOder.newInstance(FCenterOder.PClose);

		FragmentLs = new ArrayList<FCenterOder>();
		// 添加
		FragmentLs.add(FAll);
		FragmentLs.add(FDaiFu);
		FragmentLs.add(FYiFu);
		FragmentLs.add(FDaiShou);
		FragmentLs.add(FTuiKuan);
		FragmentLs.add(FClose);
		// s设置当前cureentfragment
		setCurrentFragment(FAll);
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
				switchContent1(FAll, R.id.activity_f_center_oder_frgment);
				return;
			}else{
				if (position == 0) {
					BMessage message = new BMessage(713);

					EventBus.getDefault().post(message);
				}
				if (position == 1) {
					BMessage message = new BMessage(714);

					EventBus.getDefault().post(message);
				}
				switchContent(FragmentLs.get(CurrentPostion),
						FragmentLs.get(position),
						R.id.activity_f_center_oder_frgment);
				setCurrentFragment(FragmentLs.get(position));
				CurrentPostion = position;
			}

			
			// CurrentPostion = position;
			// switchContent1(FragmentLs.get(CurrentPostion),
			// R.id.activity_f_center_oder_frgment);

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
		int Ket_Tage = FCenterOder.GetKetTage();
		switch (Ket_Tage) {
		case FCenterOder.PAll:
			FAll.RegetData();

			break;
		case FCenterOder.PDaiFu:
			FDaiFu.RegetData();
			break;
		case FCenterOder.PYiFu:
			FYiFu.RegetData();
			break;
		case FCenterOder.PDaiShou:
			FDaiShou.RegetData();
			break;
		case FCenterOder.PTuiKuan:
			FTuiKuan.RegetData();
			break;
		case FCenterOder.PClose:
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
}

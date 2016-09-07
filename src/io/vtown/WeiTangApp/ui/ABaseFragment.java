package io.vtown.WeiTangApp.ui;

import io.vtown.WeiTangApp.BaseApplication;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.NetUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.event.ConnectivityReceiver;
import io.vtown.WeiTangApp.event.ConnectivityReceiver.OnNetworkAvailableListener;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-5-6 下午1:47:43
 * 
 */
public abstract class ABaseFragment extends FragmentActivity {
	/**
	 * 全局单利application
	 */
	protected BaseApplication baseApplication;
	/**
	 * 上下文context
	 */
	protected Context BaseContext;
	/**
	 * 上下文 activity
	 */
	protected Activity BaseActivity;
	/**
	 * 屏幕宽度
	 */
	protected int screenWidth;
	/**
	 * 屏幕高度
	 */
	protected int screenHeight;
	/**
	 * 网络状态变化接收器
	 */
	protected ConnectivityReceiver BaseReceiver;
	/**
	 * 网络错误的标示箭头
	 */
	protected View NetError;

	/**
	 * oncreate里面 初始化view控件
	 */
	protected abstract void InItBaseView();

	/**
	 * oncreate里面 初始化view控件
	 */
	protected abstract void InitTile();

	/**
	 * 网络连接时候需要的DO()
	 */

	protected abstract void NetConnect();

	/**
	 * 网络断开时候需要的DO()
	 */

	protected abstract void NetDisConnect();

	/**
	 * 初始化网络状态
	 */
	protected abstract void SetNetView();

	protected FragmentManager BaseFragmentManager;
	protected FragmentTransaction BaseFragmentTransaction;

	/**
	 * 当前的fragment
	 */
	protected Fragment CurrentFragment;
	protected TextView right_txt;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		InItBaseConfig();
		InItBaseView();
		InitTile();
		NetError = findViewById(R.id.neterrorview);
		BaseReceiver = new ConnectivityReceiver(BaseContext);
		/**
		 * 判断网络状态
		 */
		BaseReceiver
				.setOnNetworkAvailableListener(new OnNetworkAvailableListener() {

					@Override
					public void onNetworkUnavailable() {
						NetDisConnect();
					}

					@Override
					public void onNetworkAvailable() {
						NetConnect();

					}
				});
		NetError.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PromptManager.GoToNetSeting(BaseContext);

			}
		});

	}

	/**
	 * 初始化基类的配置信息
	 */
	private void InItBaseConfig() {
		screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		screenHeight = getWindowManager().getDefaultDisplay().getHeight();
		BaseContext = ABaseFragment.this;
		BaseActivity = ABaseFragment.this;

		BaseFragmentManager = getSupportFragmentManager();
		BaseFragmentTransaction = BaseFragmentManager.beginTransaction();

	}

	protected void SetNetStatuse(View v) {
		if (NetUtil.NETWORN_NONE == NetUtil.getNetworkState(BaseContext)) {
			v.setVisibility(View.VISIBLE);
		} else {
			v.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置标题文本
	 */
	protected void SetTitleTxt(String Str) {
		TextView title;
		title = (TextView) findViewById(R.id.title);
		title.setText(StrUtils.NullToStr(Str));
	}

	@Override
	protected void onResume() {
		super.onResume();
		SetNetView();
		try {
			BaseReceiver.bind(BaseContext);
		} catch (Exception e) {

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			BaseReceiver.unbind(BaseContext);
		} catch (Exception e) {

		}
	}

	/**
	 * 设置标题右边的文本
	 * 
	 * @param str
	 */
	protected void SetTitleRigthTxt(String str) {
		right_txt = (TextView) findViewById(R.id.right_txt);
		right_txt.setVisibility(View.VISIBLE);
		right_txt.setText(StrUtils.NullToStr(str));

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.push_rigth_in, R.anim.push_rigth_out);
	}

	/**
	 * 点击左侧按钮的监听事件
	 */
	public void title_left_bt(View v) {
		finish();
		overridePendingTransition(R.anim.push_rigth_in, R.anim.push_rigth_out);
	};

	public void switchContent(Fragment from, Fragment to, int ResourceId) {
		BaseFragmentManager = getSupportFragmentManager();
		BaseFragmentTransaction = BaseFragmentManager.beginTransaction();
		if (CurrentFragment != to) {
			CurrentFragment = to;
			BaseFragmentTransaction.setCustomAnimations(R.anim.push_rigth_in,
					R.anim.push_rigth_out);
			if (!to.isAdded()) { // 先判断是否被add过
//				BaseFragmentTransaction.hide(from).add(ResourceId, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
				BaseFragmentTransaction.add(ResourceId, to).hide(from).commit();
			} else {
//				BaseFragmentTransaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
				BaseFragmentTransaction.show(to).hide(from).commit(); // 隐藏当前的fragment，显示下一个
			}
		}
	}

	public void switchContent1(Fragment fragment, int ResourceId) {
		 //if (CurrentFragment != fragment) {
		 CurrentFragment = fragment;
		BaseFragmentManager = getSupportFragmentManager();
		BaseFragmentTransaction = BaseFragmentManager.beginTransaction();
		BaseFragmentTransaction.replace(ResourceId, fragment) // 替换Fragment，实现切换
				.commit();
		// }
	}

	public Fragment getCurrentFragment() {
		return CurrentFragment;
	}

	public void setCurrentFragment(Fragment currentFragment) {
		CurrentFragment = currentFragment;
	}

}

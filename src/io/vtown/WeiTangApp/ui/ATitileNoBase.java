package io.vtown.WeiTangApp.ui;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.NetUtil;
import io.vtown.WeiTangApp.event.ConnectivityReceiver.OnNetworkAvailableListener;
import io.vtown.WeiTangApp.event.interf.IHttpResult;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-4-11 下午2:40:26
 *          TODO=>在使用该基类是必须要include-Xml布局net_status_bar_info_top同时ID名字为neterrorview
 * 
 */
public abstract class ATitileNoBase extends ABase implements IHttpResult<BComment>,
		OnClickListener {
	protected Activity BaseActivity;
	protected Context BaseContext;

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
	 * 获取网络数据成功时候的接口
	 */
	protected abstract void DataResult(int Code, String Msg, BComment Data);

	/**
	 * 获取网络失败时候的接口
	 */
	protected abstract void DataError(String error,int LoadType);
	protected abstract void NetConnect();

	protected abstract void NetDisConnect();

	protected abstract void SetNetView();
	/**
	 * onclick点击的回掉接口
	 */
	protected abstract void MyClick(View V);
	
	/**
	 * oncreate里面 进行Bundle数据重置
	 */
	protected abstract void InItBundle(Bundle bundle);

	/**
	 * onSaveInstanceState 里面 进行Bundle 保存
	 */
	protected abstract void SaveBundle(Bundle bundle);

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		if (null != savedInstanceState) {
			InItBundle(savedInstanceState);
		}

		BaseActivity = this;
		BaseContext = this;
		InItBaseView();
		NetError = findViewById(R.id.neterrorview);
		InitTile();
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

	protected void SetNetStatuse(View v) {
		if (NetUtil.NETWORN_NONE == NetUtil.getNetworkState(BaseContext)) {
			v.setVisibility(View.VISIBLE);
		} else {
			v.setVisibility(View.GONE);
		}
	}

	@Override
	public void getResult(int Code, String Msg, BComment Data) {
		DataResult(Code, Msg, Data);
	}

	@Override
	public void onError(String error,int LoadType) {
		DataError(error,LoadType);
	}

	@Override
	public void onClick(View arg0) {
		MyClick(arg0);

	}
	/**
	 * 上边是IV下边是文字的布局
	 * 
	 * @param title
	 * @param IvRource
	 * @param V
	 */
	public void SetCommentIV(String title, int IvRource, View V) {
		ImageView viessw;

		((ImageView) V.findViewById(R.id.comment_ivtxt_iv))
				.setBackgroundResource(IvRource);
		((TextView) V.findViewById(R.id.comment_ivtxt_txt)).setText(title);
		V.setOnClickListener(this);

	}
}

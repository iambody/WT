package io.vtown.WeiTangApp.ui.comment;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.view.ProgressWebView;
import io.vtown.WeiTangApp.ui.ATitleBase;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebViewClient;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-4-26 下午4:03:16
 */
public class AWeb extends ATitleBase {

	private ProgressWebView aweb_web;

	/**
	 * 传进来的bean的key
	 */
	public final static String Key_Bean = "beankey";
	/**
	 * 传进来的bean
	 */
	private BComment Data;

	@Override
	protected void InItBaseView() {
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.activity_web);
		if (getIntent().getExtras() != null
				&& getIntent().getExtras().containsKey(Key_Bean)) {
			Data = (BComment) getIntent().getSerializableExtra(Key_Bean);
		} else
			BaseActivity.finish();

		aweb_web = (ProgressWebView) findViewById(R.id.aweb_web);

		try {
			aweb_web.getSettings().setDisplayZoomControls(true);
			aweb_web.getSettings().setJavaScriptEnabled(true);
			aweb_web.setWebViewClient(new WebViewClient());
			aweb_web.setDownloadListener(new DownloadListener() {
				@Override
				public void onDownloadStart(String url, String userAgent,
						String contentDisposition, String mimetype,
						long contentLength) {
					if (url != null && url.startsWith("http://"))
						startActivity(new Intent(Intent.ACTION_VIEW, Uri
								.parse(url)));
				}
			});
			aweb_web.loadUrl(StrUtils.NullToStr(Data.getId()));
			// 开启DOM 存储
			aweb_web.getSettings().setDomStorageEnabled(true);
			// aweb_web.addJavascriptInterface(this, "login");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取到通知
	 * 
	 * @param a
	 */
	/*
	 * public void startFunction(String a) {
	 * PromptManager.ShowCustomToast(BaseContext, "获取JS数据");
	 * WebActivity.this.finish();
	 * 
	 * }
	 */
	@Override
	protected void InitTile() {
		SetTitleTxt(Data.getTitle());
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {

	}

	@Override
	protected void DataError(String error,int LoadTyp) {
	}

	@Override
	protected void NetConnect() {

	}

	@Override
	protected void NetDisConnect() {

	}

	@Override
	protected void SetNetView() {

	}

	@Override
	protected void MyClick(View V) {

	}

	@Override
	protected void InItBundle(Bundle bundle) {

	}

	@Override
	protected void SaveBundle(Bundle bundle) {

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && aweb_web.canGoBack()) {
			aweb_web.goBack();// 返回前一个页面
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}

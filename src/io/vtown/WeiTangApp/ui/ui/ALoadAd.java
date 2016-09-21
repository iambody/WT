package io.vtown.WeiTangApp.ui.ui;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.ui.ABase;
import io.vtown.WeiTangApp.ui.title.loginregist.ALogin;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-6-22 下午7:15:20
 * @see加载广告页
 */
public class ALoadAd extends ABase {
	private ImageView splash_iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loadad);
		splash_iv = (ImageView) findViewById(R.id.splash_iv);
		ISplash();
	}

	private void ISplash() {
		AlphaAnimation aa = new AlphaAnimation(0f, 1.0f);
		aa.setDuration(2000);
		splash_iv.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
                if (Spuit.IsLogin_Get(BaseContext)) {
                    PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                            AMainTab.class));
                    BaseActivity.finish();
                    return;
                }
				PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
						ALogin.class));
				BaseActivity.finish();
			}
		});
	}

	private void TxtAlpha(ImageView VV) {
		ObjectAnimator mAnimator = ObjectAnimator.ofFloat(VV, "alpha", 1f, 0f,
				1f);
		mAnimator.setDuration(1 * 1000);
		mAnimator.start();
	}
}

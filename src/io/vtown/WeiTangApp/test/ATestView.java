package io.vtown.WeiTangApp.test;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.ui.ATitleBase;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-7-15 下午4:23:52
 * 
 */
public class ATestView extends ATitleBase {
	private ImageView test_iv;

	private Button buttt;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_testview);
		test_iv = (ImageView) findViewById(R.id.test_iv);
		buttt = (Button) findViewById(R.id.buttt);
		buttt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				TxtAlpha();
//				TxtRoTation();
//				TxtTranslationx();
				TxtScaleY();
			}
		});
		test_iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PromptManager.ShowCustomToast(BaseContext, "被点击");
			}
		});
	}

	// AlPha 透明
	private void TxtAlpha() {
		ObjectAnimator mAnimator = ObjectAnimator.ofFloat(test_iv, "alpha", 1f,
				0f, 1f);
		mAnimator.setDuration(2 * 1000);
		mAnimator.start();
	}

	// rotation 旋转
	private void TxtRoTation(){
		ObjectAnimator animator=ObjectAnimator.ofFloat(buttt, "rotation", 0f,20f,50f,10f,90f,180f,360f);
		animator.setDuration(10*1000);
		animator.start();
	}
	//translation移动
	private void TxtTranslationx(){
		float CurrentTranslationX=test_iv.getTranslationX();
		ObjectAnimator mAnimator=ObjectAnimator.ofFloat(test_iv, "translationX", CurrentTranslationX,500f);
		mAnimator.setDuration(6*1000);
		mAnimator.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
			}
			
		});
		mAnimator.start();
		
	}
	//scaley 缩放
	private void TxtScaleY(){
		float currentScal=test_iv.getScaleY();
		ObjectAnimator animator=ObjectAnimator.ofFloat(test_iv, "scaleY", currentScal,2f,3f);
		animator.setDuration(3*1000);
		animator.start();
		
		
		
	}
	
	@Override
	protected void InitTile() {
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
	}

	@Override
	protected void DataError(String error, int LoadType) {
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

}

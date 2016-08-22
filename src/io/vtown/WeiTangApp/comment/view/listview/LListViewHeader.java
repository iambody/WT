package io.vtown.WeiTangApp.comment.view.listview;

import io.vtown.WeiTangApp.R;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * @author 王永奎 E-mail:wangyongkui@ucfgroup.com
 * @department 互联网金融部
 * @version 创建时间：2015-10-28 下午4:22:37
 */
public class LListViewHeader extends LinearLayout {
	private LinearLayout mContainer;
	private ImageView mArrowImageView;
	private TextView mHintTextView;
	private int mState = STATE_NORMAL;

	private Animation mRotateUpAnim;
	private Animation mRotateDownAnim;

	private final int ROTATE_ANIM_DURATION = 180;

	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_REFRESHING = 2;
	// private Animation mPBAnimation;
	/**
	 * 单独动画加载
	 */
	private SecondStepView secondStepView;
	private AnimationDrawable secondAnimation;

	public LListViewHeader(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public LListViewHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		// 初始情况，设置下拉刷新view高度
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 0);
		mContainer = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.xlistview_header, null);
		addView(mContainer, lp);
		setGravity(Gravity.BOTTOM);

		mArrowImageView = (ImageView) findViewById(R.id.xlistview_header_arrow);
		mHintTextView = (TextView) findViewById(R.id.xlistview_header_hint_textview);
		// mPBAnimation = AnimationUtils.loadAnimation(context,
		// R.anim.loadmore);
		LinearInterpolator lin = new LinearInterpolator();
		// mPBAnimation.setInterpolator(lin);

		secondStepView = (SecondStepView) findViewById(R.id.second_step_view);
		secondStepView.setBackgroundResource(R.drawable.second_step_animation);
		secondAnimation = (AnimationDrawable) secondStepView.getBackground();

		mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		if (isInEditMode()) {
			return;
		}
		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateUpAnim.setFillAfter(true);
		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateDownAnim.setFillAfter(true);
	}

	public void setState(int state) {
		if (state == mState)
			return;
		mArrowImageView.clearAnimation();
		if (state == STATE_REFRESHING) { // 显示进度
			mArrowImageView.setImageResource(R.drawable.icon_loading_selected);
			// mArrowImageView.startAnimation(mPBAnimation);
			// mArrowImageView.startAnimation(secondAnimation);
			mArrowImageView.setVisibility(View.GONE);
			secondStepView.setVisibility(View.VISIBLE);
			secondAnimation.start();
		} else { // 显示箭头图片
			mArrowImageView.setVisibility(View.VISIBLE);
			mArrowImageView.setImageResource(R.drawable.xlistview_arrow);
			secondStepView.setVisibility(View.GONE);
		}

		switch (state) {
		case STATE_NORMAL:
			if (mState == STATE_READY) {
				// mArrowImageView.startAnimation(mRotateDownAnim);
				mArrowImageView.setVisibility(View.GONE);
				secondStepView.setVisibility(View.VISIBLE);
				secondAnimation.start();
			}
			if (mState == STATE_REFRESHING) {
				// mArrowImageView.clearAnimation();
				secondAnimation.stop();
			}
			mHintTextView.setText(R.string.xlistview_header_hint_normal);
			break;
		case STATE_READY:
			if (mState != STATE_READY) {
				// mArrowImageView.clearAnimation();
				// mArrowImageView.startAnimation(mRotateUpAnim);
				// mArrowImageView.setVisibility(View.GONE);
				// secondAnimation.start();
				mHintTextView.setText(R.string.xlistview_header_hint_ready);
			}
			break;
		case STATE_REFRESHING:
			mArrowImageView.setVisibility(View.GONE);
			secondStepView.setVisibility(View.VISIBLE);
			secondAnimation.start();
			mHintTextView.setText(R.string.xlistview_header_hint_loading);
			break;
		}
		mState = state;
	}

	public void setVisiableHeight(int height) {
		if (height < 0)
			height = 0;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContainer
				.getLayoutParams();
		lp.height = height;
		mContainer.setLayoutParams(lp);
	}

	public int getVisiableHeight() {
		return mContainer.getHeight();
	}

}

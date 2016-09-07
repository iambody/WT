package io.vtown.WeiTangApp.ui.afragment;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.comment.util.DimensionPixelUtil;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.fragment.FBase;
import io.vtown.WeiTangApp.fragment.FSellStatisticLine;
import io.vtown.WeiTangApp.ui.ABaseFragment;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-6-17 下午7:15:06
 * @see 销售统计
 */
public class ASellStatistics extends ABaseFragment implements OnClickListener,
		OnPageChangeListener {
	// 三个text
	private TextView sellstatistics_up_txt1, sellstatistics_up_txt2,
			sellstatistics_up_txt3;
	// texts的存放队列
	private List<TextView> textViews = new ArrayList<TextView>();
	// 需要移动的动画view
	private ImageView sellstatistics_up_line;
	// 动画图片偏移量
	private int offset = 0;
	// 当前页卡编号
	private int currIndex = 0;
	// 当前的pager
	// private ViewPager sellstatistics_page;
	private int CurrentIndex = 0;
	// 当前的fragment

	private FSellStatisticLine fSellStatisticLine1, fSellStatisticLine2,
			fSellStatisticLine3;
	// fragment的集合
	private List<FSellStatisticLine> sellStatisticLines = new ArrayList<FSellStatisticLine>();

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_sellstatistics);
		IBaseVv();
	}

	private void IBaseVv() {
		sellstatistics_up_txt1 = (TextView) findViewById(R.id.sellstatistics_up_txt1);
		sellstatistics_up_txt2 = (TextView) findViewById(R.id.sellstatistics_up_txt2);
		sellstatistics_up_txt3 = (TextView) findViewById(R.id.sellstatistics_up_txt3);

		sellstatistics_up_txt1.setOnClickListener(this);
		sellstatistics_up_txt2.setOnClickListener(this);
		sellstatistics_up_txt3.setOnClickListener(this);

		sellstatistics_up_line = (ImageView) findViewById(R.id.sellstatistics_up_line);

		textViews.add(sellstatistics_up_txt1);
		textViews.add(sellstatistics_up_txt2);
		textViews.add(sellstatistics_up_txt3);

		fSellStatisticLine1 = GetNewInstance(1);
		fSellStatisticLine2 = GetNewInstance(2);
		fSellStatisticLine3 = GetNewInstance(3);
		sellStatisticLines.add(fSellStatisticLine1);
		sellStatisticLines.add(fSellStatisticLine2);
		sellStatisticLines.add(fSellStatisticLine3);

		IViewLine();

		// NumberControl();
	}

	public static FSellStatisticLine GetNewInstance(int arg) {
		FSellStatisticLine fragment = new FSellStatisticLine();
		Bundle bundle = new Bundle();
		bundle.putInt("FSellStatisticLinekey", arg);
		fragment.setArguments(bundle);
		return fragment;
	}

	private void IViewLine() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				screenWidth / 5, DimensionPixelUtil.dip2px(BaseContext, 5));
		sellstatistics_up_line.setLayoutParams(params);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = screenW / 5;// 计算偏移量

		Matrix matrix = new Matrix();
		// matrix.postTranslate(offset, 0);
		matrix.setTranslate(offset * 1, 0);
		sellstatistics_up_line.setImageMatrix(matrix);// 设置动画初始位置
		AnimationSet animationSet = new AnimationSet(false);
		// 移动的动画
		Animation animation = new TranslateAnimation(offset * 0, offset * 1, 0,
				0);
		animation.setDuration(1);
		animation.setFillAfter(true);// True:图片停在动画结束位置
		// 渐变的动画
		Animation animation1 = new AlphaAnimation(0, 1);
		animation1.setDuration(100);

		animationSet.addAnimation(animation);
		animationSet.addAnimation(animation1);

		sellstatistics_up_line.startAnimation(animation);

		switchContent1(sellStatisticLines.get(CurrentIndex),
				R.id.sellstatistics_fragment);// s
	}

	private void TxtClick(int Postion) {
		for (int i = 0; i < textViews.size(); i++) {
			if (i == Postion) {
				textViews.get(i).setTextColor(
						getResources().getColor(R.color.white));
			} else {
				textViews.get(i).setTextColor(
						getResources().getColor(R.color.app_gray));
			}
		}
	}

	private void TxtChangeControl(int postion) {
		Animation animation = null;
		if (postion == currIndex)
			return;
		TxtClick(postion);

		switch (postion) {
			case 0:

				if (currIndex == 1) {
					animation = new TranslateAnimation(offset * 2, offset *1, 0, 0);

				} else if (currIndex == 2) {

					animation = new TranslateAnimation(offset * 3, offset * 1, 0, 0);
				}

				break;
			case 1:

				if (currIndex == 0) {
					animation = new TranslateAnimation(offset * 1, offset * 2, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(offset * 3, offset * 2, 0, 0);
				}

				break;
			case 2:

				if (currIndex == 0) {
					animation = new TranslateAnimation(offset * 1, offset * 3, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(offset * 2, offset * 3, 0, 0);
				}

				break;
			default:
				break;
		}
		currIndex = postion;
		animation.setFillAfter(true);// True:图片停在动画结束位置

		animation.setDuration(300);
		sellstatistics_up_line.startAnimation(animation);
		// sellstatistics_page.setCurrentItem(currIndex);
		// 需要刷新碎片里面的图形********************时刻刷新*******************************************
		switchContent(sellStatisticLines.get(CurrentIndex),
				sellStatisticLines.get(currIndex), R.id.sellstatistics_fragment);

		CurrentIndex = currIndex;



	}

	@Override
	protected void InitTile() {
		SetTitleTxt("销售统计");
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
			case R.id.sellstatistics_up_txt1:
				TxtChangeControl(0);
				break;
			case R.id.sellstatistics_up_txt2:
				TxtChangeControl(1);
				break;
			case R.id.sellstatistics_up_txt3:
				TxtChangeControl(2);
				break;

			default:
				break;
		}
	}

	/**
	 * 下边进行切换的ap
	 */

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		TxtChangeControl(arg0);
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

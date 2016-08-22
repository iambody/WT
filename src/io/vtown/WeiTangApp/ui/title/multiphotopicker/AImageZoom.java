package io.vtown.WeiTangApp.ui.title.multiphotopicker;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.comment.multiphoto.model.ImageItem1;
import io.vtown.WeiTangApp.comment.multiphoto.util.ImageDisplayer;
import io.vtown.WeiTangApp.comment.multiphoto.util.IntentConstants;
import io.vtown.WeiTangApp.ui.ABase;
import io.vtown.WeiTangApp.ui.ATitleBase;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-5-26 下午3:38:36
 * 
 */
public class AImageZoom extends ABase {

	private ViewPager pager;
	private MyPageAdapter adapter;
	private int currentPosition;
	private List<ImageItem1> mDataList = new ArrayList<ImageItem1>();

	private RelativeLayout photo_relativeLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		InItBaseView();
	}

	protected void InItBaseView() {
		setContentView(R.layout.multiphoto_act_zoom);
		IBase();

	}

	private void IBase() {
		photo_relativeLayout = (RelativeLayout) findViewById(R.id.photo_relativeLayout);
		photo_relativeLayout.setBackgroundColor(0x70000000);
		initData();
		Button photo_bt_exit = (Button) findViewById(R.id.photo_bt_exit);
		photo_bt_exit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		Button photo_bt_del = (Button) findViewById(R.id.photo_bt_del);
		photo_bt_del.setVisibility(View.GONE);
		photo_bt_del.setClickable(false);
		//如果想要删除的话使用事件总线！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
		photo_bt_del.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (mDataList.size() == 1) {
					removeImgs();
					finish();
				} else {
					removeImg(currentPosition);
					pager.removeAllViews();
					adapter.removeView(currentPosition);
					adapter.notifyDataSetChanged();
				}
			}
		});

		pager = (ViewPager) findViewById(R.id.viewpager);
		pager.setOnPageChangeListener(pageChangeListener);

		adapter = new MyPageAdapter(mDataList);
		pager.setAdapter(adapter);
		pager.setCurrentItem(currentPosition);
	}

	private void initData() {
		currentPosition = getIntent().getIntExtra(
				IntentConstants.EXTRA_CURRENT_IMG_POSITION, 0);

//		mDataList = baseApplication.getAddPicLs();
//		baseApplication.CleranAddPic();
		// mDataList = APublicGood.mDataList;
	}

	private void removeImgs() {
		mDataList.clear();
	}

	private void removeImg(int location) {
		if (location + 1 <= mDataList.size()) {
			mDataList.remove(location);
		}
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		public void onPageSelected(int arg0) {
			currentPosition = arg0;
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void onPageScrollStateChanged(int arg0) {

		}
	};

	class MyPageAdapter extends PagerAdapter {
		private List<ImageItem1> dataList = new ArrayList<ImageItem1>();
		private ArrayList<ImageView> mViews = new ArrayList<ImageView>();

		public MyPageAdapter(List<ImageItem1> dataList) {
			this.dataList = dataList;
			int size = dataList.size();
			for (int i = 0; i != size; i++) {
				ImageView iv = new ImageView(BaseActivity);
				ImageDisplayer.getInstance(BaseActivity).displayBmp(iv, null,
						dataList.get(i).sourcePath, false);
				iv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT));
				mViews.add(iv);
			}
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public Object instantiateItem(View arg0, int arg1) {
			ImageView iv = mViews.get(arg1);
			((ViewPager) arg0).addView(iv);
			return iv;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
			if (mViews.size() >= arg1 + 1) {
				((ViewPager) arg0).removeView(mViews.get(arg1));
			}
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return dataList.size();
		}

		public void removeView(int position) {
			if (position + 1 <= mViews.size()) {
				mViews.remove(position);
			}
		}

	}

}

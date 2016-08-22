package io.vtown.WeiTangApp.comment.view.pop;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcache.BHome;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.view.listview.lPopListview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-4-20 下午1:49:46 首页筛选条件的弹出框
 */
public class PHomeSelect extends PopupWindow {
	/**
	 * 上下文
	 */
	private Context pContext;

	/**
	 * 基view
	 */
	private View BaseView;

	/*
	 * listview对象
	 */
	private lPopListview pop_select_home_ls;
	/**
	 * AP
	 * 
	 * @param pContext
	 */
	private MyAdapter myAdapter;
	/**
	 * 列表数据
	 */
	private List<BHome> mBlComments;

	/**
	 * 回掉接口
	 * 
	 * @param pContext
	 */
	private SeleckClickListener mClickListener;

	/**
	 * 暴露接口
	 */
	public void GetSelectReult(SeleckClickListener listener) {
		this.mClickListener = listener;
	}

	public PHomeSelect(Context pContext, List<BHome> data) {
		super();
		this.pContext = pContext;
		mBlComments = data;
		BaseView = LayoutInflater.from(pContext).inflate(
				R.layout.pop_select_home, null);
		IPop();
		IView();
	}

	private void IView() {
		pop_select_home_ls = (lPopListview) BaseView
				.findViewById(R.id.pop_select_home_ls);
		myAdapter = new MyAdapter(pContext);
		pop_select_home_ls.setAdapter(myAdapter);
		myAdapter.FrfrashView(mBlComments);
		pop_select_home_ls.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				BHome data = (BHome) arg0.getItemAtPosition(arg2);
				mClickListener.GetResult(data);
			}
		});
	}

	/**
	 * 初始化
	 */
	private void IPop() {
		BaseView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		setContentView(BaseView);
		setWidth(LayoutParams.WRAP_CONTENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		setBackgroundDrawable(dw);
		// setBackgroundDrawable(pContext.getResources().getDrawable(
		// R.drawable.home_select_bg));

		this.setOutsideTouchable(true);
	}

	private class MyAdapter extends BaseAdapter {
		private Context mContext;
		private List<BHome> mBeans = new ArrayList<BHome>();

		public MyAdapter(Context mContext) {
			super();
			this.mContext = mContext;

		}

		public void FrfrashView(List<BHome> lsx) {
			this.mBeans.add(new BHome("0", "品牌"));
			this.mBeans.addAll(lsx);//；； = lsx;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
		 return mBeans.size();
			 
		}

		@Override
		public Object getItem(int position) {
			return mBeans.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Item mItem = null;
			if (null == convertView) {
				mItem = new Item();
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.item_home_select, null);
				mItem.item_home_select_txt = ViewHolder.get(convertView,
						R.id.item_home_select_txt);
				convertView.setTag(mItem);
			} else {
				mItem = (Item) convertView.getTag();
			}
			BHome da = mBeans.get(position);
			StrUtils.SetTxt(mItem.item_home_select_txt, da.getCate_name());
			return convertView;
		}

		private class Item {
			TextView item_home_select_txt;
		}

	}

	public interface SeleckClickListener {
		void GetResult(BHome data);

	}
}

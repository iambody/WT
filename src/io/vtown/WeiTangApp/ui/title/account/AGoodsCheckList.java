package io.vtown.WeiTangApp.ui.title.account;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BLDComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.net.NHttpBaseStr;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.event.interf.IHttpResult;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.title.AGoodDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.android.volley.Request.Method;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-6-12 下午1:46:51
 * @authors 商品清单
 */
public class AGoodsCheckList extends ATitleBase {
	private BLComment mBlComment = null;
	private ListView goodscheklist_ls;
	/**
	 * 对应的AP
	 */
	private CheckAp checkAp;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_goodschecklist);
		if (getIntent().getExtras() == null
				|| !getIntent().getExtras().containsKey("checklsbean"))
			BaseActivity.finish();

		mBlComment = (BLComment) getIntent()
				.getSerializableExtra("checklsbean");
		if (null == mBlComment)
			BaseActivity.finish();
		goodscheklist_ls = (ListView) findViewById(R.id.goodscheklist_ls);
		checkAp = new CheckAp( mBlComment.getStore_list(),
				R.layout.item_goodscheck);
		goodscheklist_ls.setAdapter(checkAp);
		goodscheklist_ls.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				BLDComment daBldComment = (BLDComment) arg0
						.getItemAtPosition(arg2);
				PromptManager.SkipActivity(BaseActivity, new Intent(
						BaseContext, AGoodDetail.class).putExtra("goodid",
						daBldComment.getGoods_id()));
			}
		});

	}

	private List<BLDComment> GetLs(BLComment da) {
		List<BLDComment> mList = new ArrayList<BLDComment>();
		List<String> Cid = new ArrayList<String>();
		for (int i = 0; i < da.getStore_list().size(); i++) {
			if (!Cid.contains(da.getStore_list().get(i).getCid())) {
				Cid.add(da.getStore_list().get(i).getCid());
				BLDComment mybean = da.getStore_list().get(i);
				mList.add(mybean);
			}
		}

		return da.getStore_list();
	}

	@Override
	protected void InitTile() {
		SetTitleTxt("商品清单");
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
	}

	@Override
	protected void DataError(String error, int LoadType) {
	}

	private class CheckAp extends BaseAdapter {
		 
		/**
		 * 填充器
		 */
		private LayoutInflater inflater;
		/**
		 * 资源id
		 */
		private int ResourceId;
		/**
		 * 数据
		 */
		private List<BLDComment> datas = new ArrayList<BLDComment>();

		public CheckAp(  List<BLDComment> d, int resourceId) {
			super();
			 
			ResourceId = resourceId;
			this.datas = d;
			this.inflater = LayoutInflater.from(BaseContext);
		}

		/**
		 * 数据变化进行刷新
		 */
		// public void FrashData(List<BLDComment> d) {
		// this.datas = d;
		// this.notifyDataSetChanged();
		// }

		@Override
		public int getCount() {
			return datas.size();
		}

		@Override
		public Object getItem(int arg0) {
			return datas.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {// item_goodscheck
			GoodsCheckItem checkItem = null;
			if (null == arg1) {
				checkItem = new GoodsCheckItem();
				arg1 = inflater.inflate(ResourceId, null);
				checkItem.item_goodscheck_iv = ViewHolder.get(arg1,
						R.id.item_goodscheck_iv);
				checkItem.item_goodscheck_name = ViewHolder.get(arg1,
						R.id.item_goodscheck_name);
				checkItem.item_goodscheck_guige = ViewHolder.get(arg1,
						R.id.item_goodscheck_guige);
				checkItem.item_goodscheck_price = ViewHolder.get(arg1,
						R.id.item_goodscheck_price);
				checkItem.item_goodscheck_number = ViewHolder.get(arg1,
						R.id.item_goodscheck_number);
				checkItem.item_goodscheck_iv = ViewHolder.get(arg1,
						R.id.item_goodscheck_iv);
				arg1.setTag(checkItem);
			} else {
				checkItem = (GoodsCheckItem) arg1.getTag();
			}
			BLDComment bldComment = datas.get(arg0);
			ImageLoaderUtil.Load2(bldComment.getCover(),
					checkItem.item_goodscheck_iv, R.drawable.error_iv2);
			StrUtils.SetTxt(checkItem.item_goodscheck_name,
					bldComment.getTitle());
			StrUtils.SetTxt(checkItem.item_goodscheck_guige,
					String.format("规格:  %s", bldComment.getGoods_attr_name()));
			StrUtils.SetTxt(checkItem.item_goodscheck_price,
					"￥" + StrUtils.SetTextForMony(bldComment.getSell_price()));
			StrUtils.SetTxt(checkItem.item_goodscheck_number,
					"×" + bldComment.getGoods_num());

			return arg1;
		}

		class GoodsCheckItem {
			ImageView item_goodscheck_iv;
			TextView item_goodscheck_name;
			TextView item_goodscheck_guige;
			TextView item_goodscheck_price;
			TextView item_goodscheck_number;
		}

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

package io.vtown.WeiTangApp.ui.title.shop.addgood;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.fragment.GoodCategoryFragment;
import io.vtown.WeiTangApp.fragment.GoodCategoryFragment.onClickIndexListener;
import io.vtown.WeiTangApp.ui.ATitleBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-6-7 下午9:14:38 添加商品之类目
 */
public class AAddGoodCategory extends ATitleBase implements OnItemClickListener {
	
	 
	 
	 private int mPosition = 0;

	/**
	 * 分类一级列表
	 */
	private ListView lv_category_list;
	/**
	 * AP
	 */
	private CategoryAdapter mCategoryAdapter;
	/**
	 * popup
	 */
	private PopupWindow mPopupWindow;

	/**
	 * popup里的View
	 */
	private View mView;


	private FragmentTransaction fragmentTransaction;


	private GoodCategoryFragment goodCategoryFragment;

	private List<BLComment> category_list;

	private LinearLayout good_category_outlay;

	private View good_category_nodata_lay;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_add_good_category);
		IView();
		IData(0 + "", 0);
	}

	private void IData(String pid, int type) {
		SetTitleHttpDataLisenter(this);
		if(type == 0){
			PromptManager.showtextLoading(BaseContext, getResources().getString(R.string.loading));
		}else{
			goodCategoryFragment.showLoading(true);
		}


		HashMap<String, String> map = new HashMap<String, String>();
		map.put("pid", pid);
		FBGetHttpData(map, Constants.Add_Good_Categoty, Method.GET, type,
				LOAD_INITIALIZE);
	}

	private void IView() {
		
		good_category_outlay = (LinearLayout) findViewById(R.id.good_category_outlay);
		good_category_nodata_lay = findViewById(R.id.good_category_nodata_lay);
		IDataView(good_category_outlay, good_category_nodata_lay, NOVIEW_INITIALIZE);

		lv_category_list = (ListView) findViewById(R.id.lv_category_list);

		mCategoryAdapter = new CategoryAdapter(BaseContext,
				R.layout.item_category_txt,1);

		lv_category_list.setAdapter(mCategoryAdapter);
		
		goodCategoryFragment = new GoodCategoryFragment(BaseContext);
		fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, goodCategoryFragment);  
          
        fragmentTransaction.commit();  

		lv_category_list.setOnItemClickListener(this);
		goodCategoryFragment.setOnClickIndexListener(new onClickIndexListener() {
			
			@Override
			public void onClickIndex(int index) {
				if(CheckNet(BaseContext))return;
				BLComment blComment = category_list.get(index);
				if(blComment.getAttrs().size() >= 2){
					blComment.setAdd_good_id(blComment.getId());
					blComment.setAdd_good_cate_name(blComment.getCate_name());
					blComment.setAdd_good_attrs_id_1(blComment.getAttrs().get(0).getId());
					blComment.setAdd_good_attrs_id_2(blComment.getAttrs().get(1).getId());
					blComment.setAdd_good_attrs_name_1(blComment.getAttrs().get(0).getName());
					blComment.setAdd_good_attrs_name_2(blComment.getAttrs().get(1).getName());
					
				}else{
					PromptManager.ShowMyToast(BaseContext, "选择的规格有误");
					return;
				}
				Intent intent = new Intent();
				intent.putExtra("category_data", blComment);
				setResult(RESULT_OK, intent);
				AAddGoodCategory.this.finish();
			}
		});
//		if(goodCategoryFragment.isClick){
//			//BLComment item = (BLComment) mCategory2Adapter.getItem(arg2);
//			BLComment blComment = category_list.get(goodCategoryFragment.getPosition());
//			if(blComment.getAttrs().size() >= 2){
//				blComment.setAdd_good_id(blComment.getId());
//				blComment.setAdd_good_cate_name(blComment.getCate_name());
//				blComment.setAdd_good_attrs_id_1(blComment.getAttrs().get(0).getId());
//				blComment.setAdd_good_attrs_id_2(blComment.getAttrs().get(1).getId());
//				blComment.setAdd_good_attrs_name_1(blComment.getAttrs().get(0).getName());
//				blComment.setAdd_good_attrs_name_2(blComment.getAttrs().get(1).getName());
//				
//			}else{
//				PromptManager.ShowMyToast(BaseContext, "选择的规格有误");
//				return;
//			}
//			Intent intent = new Intent();
//			intent.putExtra("category_data", blComment);
//			setResult(RESULT_OK, intent);
//			AAddGoodCategory.this.finish();
//		}

	}

	
	@Override
	protected void InitTile() {
		SetTitleTxt(getString(R.string.add_good_category));
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
		
		if (StrUtils.isEmpty(Data.getHttpResultStr())) {
			return;
		}

		category_list = new ArrayList<BLComment>();
		try {
			category_list = JSON.parseArray(Data.getHttpResultStr(),
					BLComment.class);

		} catch (Exception e) {
			DataError("解析失败", 0);
		}
		IDataView(good_category_outlay, good_category_nodata_lay, NOVIEW_RIGHT);
		switch (Data.getHttpResultTage()) {
		case 0:
			mCategoryAdapter.FrashData(category_list);
			IData(category_list.get(0).getId(), 1);
			break;
		case 1:
			//mCategory2Adapter.FrashData(category_list);
			goodCategoryFragment.SetData(category_list);
			goodCategoryFragment.showLoading(false);
			break;
		default:
			break;
		}

	}

	@Override
	protected void DataError(String error, int LoadType) {
		PromptManager.ShowMyToast(BaseContext, error);
		IDataView(good_category_outlay, good_category_nodata_lay, NOVIEW_RIGHT);
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

	class CategoryAdapter extends BaseAdapter {

		private Context context;
		private int ResourseId;
		private LayoutInflater inflater;
		private List<BLComment> datas = new ArrayList<BLComment>();

		public CategoryAdapter(Context context, int ResourseId, int type) {
			super();
			this.context = context;
			this.ResourseId = ResourseId;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {

			return datas.size();
		}

		/**
		 * 刷新数据
		 * 
		 * @param dass
		 */
		public void FrashData(List<BLComment> dass) {
			this.datas = dass;
			this.notifyDataSetChanged();
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
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			CategoryItem category = null;
			if (arg1 == null) {
				category = new CategoryItem();
				arg1 = inflater.inflate(ResourseId, null);
				category.tv_category_content = ViewHolder.get(arg1,
						R.id.tv_category_content);
				arg1.setTag(category);
			} else {
				category = (CategoryItem) arg1.getTag();
			}
			StrUtils.SetTxt(category.tv_category_content, datas.get(arg0)
					.getCate_name());
			if(arg0 == mPosition){
				arg1.setBackgroundResource(R.color.app_gray);
			}else{
				arg1.setBackgroundResource(R.color.white);
			}
			return arg1;
		}

	}

	class CategoryItem {
		TextView tv_category_content;
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		BLComment item = (BLComment) mCategoryAdapter.getItem(arg2);
		mPosition = arg2;
		IData(item.getId(), 1);
		mCategoryAdapter.notifyDataSetChanged();
		//goodCategoryFragment.showLoading(true);

	}

}


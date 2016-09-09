package io.vtown.WeiTangApp.fragment;

import java.util.List;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.addgood.BCategory;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-8-9 上午11:13:43
 *  
 */
public class GoodCategoryFragment extends Fragment {
	
	public static final String TAG = "MyFragment";  
    private String str;
	private View view;  
	
	private int FPosition = 0;
	public boolean isClick = false;
	private Category2Adapter adapter;
	private Context mContext;
	private onClickIndexListener mClickIndexListener;
	private View loading_now;
	
	public GoodCategoryFragment(Context context){
		super();
		mContext = context;
	}
	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
		try {
			mClickIndexListener = (onClickIndexListener)activity;
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
  
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        view = inflater.inflate(R.layout.category_fragment, null);  
        loading_now = view.findViewById(R.id.loading_now);
        loading_now.setVisibility(View.GONE);
       //PromptManager.showLoading(mContext);
        //得到数据  
        //str = getArguments().getString(TAG);  
        //fragment_category_title.setText(str);  
        return view;  
    }
    
    public void showLoading(boolean flag){
    	if(flag){
    		loading_now.setVisibility(View.VISIBLE);
    	}else{
    		loading_now.setVisibility(View.GONE);
    	}
    }

	
	
	public void SetData(List<BCategory> datas){
		GridView gv_good_2_category = (GridView) view.findViewById(R.id.gv_good_2_category);
		adapter = new Category2Adapter(datas);
		gv_good_2_category.setAdapter(adapter);
		
		//PromptManager.closeLoading();
		gv_good_2_category.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mClickIndexListener.onClickIndex(position);
			}
		});
	}
	
	public int getPosition(){
		return FPosition;
	}
	
	class Category2Adapter extends BaseAdapter{
		
		private List<BCategory> datas;
		
		public Category2Adapter(List<BCategory> datas){
			super();
			this.datas = datas;
		}

		@Override
		public int getCount() {
			
			return datas.size();
		}

		@Override
		public Object getItem(int position) {
			
			return datas.get(position);
		}

		@Override
		public long getItemId(int position) {
			
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Category2Item item = null;
			if(convertView == null){
				item = new Category2Item();
				convertView = View.inflate(getActivity(), R.layout.item_category_2, null);
				item.tv_category_content_2 = (TextView) convertView.findViewById(R.id.tv_category_content_2);
				convertView.setTag(item);
			}else{
				item = (Category2Item) convertView.getTag();
			}
			item.tv_category_content_2.setText(datas.get(position).getCate_name());
			return convertView;
		}
		
	}
	
	class Category2Item{
		TextView tv_category_content_2;
	}
	
	 public interface onClickIndexListener{  
	        public void onClickIndex(int index);
	    } 
	 
	 public void setOnClickIndexListener(onClickIndexListener listener){
		 mClickIndexListener = listener;
	 }


}

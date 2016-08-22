package io.vtown.WeiTangApp.ui.title.shop.addgood;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-5-26 上午10:49:27 增加规格页面
 */
public class AAddGoodRule extends ATitleBase {

	
	/**
	 * 添加规格列表
	 */
	private ListView lv_add_good_rule_ls;

	private int count = 1;

	private AddRuleAdapter addRuleAdapter;

	@Override
	protected void InItBaseView() {

		setContentView(R.layout.activity_add_good_add_rule);
		IView();
	}

	private void IView() {

		View view = LayoutInflater.from(BaseContext).inflate(
				R.layout.item_add_good_rule_foot, null);
		view.findViewById(R.id.ll_add_more).setOnClickListener(this);

		lv_add_good_rule_ls = (ListView) findViewById(R.id.lv_add_good_rule_ls);
		lv_add_good_rule_ls.addFooterView(view);
		addRuleAdapter = new AddRuleAdapter(BaseContext,
				R.layout.item_add_good_rule);
		lv_add_good_rule_ls.setAdapter(addRuleAdapter);

	}

	@Override
	protected void InitTile() {
		SetTitleTxt("添加规格");
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

		switch (V.getId()) {
		case R.id.ll_add_more:

			addRuleAdapter.AddFrash();

			break;

		default:
			break;
		}
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

	public boolean ItemEdCheck(AddGoodRuleItem d) {

		if (StrUtils.isEmpty(d.et_add_good_rule_size.getText().toString())) {
			PromptManager.ShowCustomToast(BaseContext, "请输入大小");
			return false;
		}
		if (StrUtils.isEmpty(d.et_add_good_rule_color.getText().toString())) {
			PromptManager.ShowCustomToast(BaseContext, "请输入颜色");
			return false;
		}
		if (StrUtils.isEmpty(d.et_add_good_rule_retail_price.getText()
				.toString())) {
			PromptManager.ShowCustomToast(BaseContext, "请输入建议零售价");
			return false;
		}
		if (StrUtils.isEmpty(d.et_add_good_rule_inventory.getText().toString())) {
			PromptManager.ShowCustomToast(BaseContext, "请输入库存");
			return false;
		}
		
		return true;

	}

	class AddRuleAdapter extends BaseAdapter {

		private Context context;
		private int ResourseId;
		private LayoutInflater inflater;
		private List<AddGoodRuleItem> mAddGoodRuleItems = new ArrayList<AddGoodRuleItem>();

		public AddRuleAdapter(Context context, int ResourseId) {
			super();
			this.context = context;
			this.ResourseId = ResourseId;
			this.inflater = LayoutInflater.from(context);
			mAddGoodRuleItems.add(new AddGoodRuleItem());
		}

		public void AddFrash() {
			AddGoodRuleItem iRuleItem = mAddGoodRuleItems.get(mAddGoodRuleItems
					.size() - 1);
			if (ItemEdCheck(iRuleItem)) {
				count += 1;
				mAddGoodRuleItems.add(new AddGoodRuleItem());
				this.notifyDataSetChanged();
			}

		}
		
		

		@Override
		public int getCount() {

			return count;
		}

		@Override
		public Object getItem(int arg0) {

			return null;
		}

		@Override
		public long getItemId(int arg0) {

			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {

			AddGoodRuleItem addGoodRule = null;
			if (arg1 == null) {
				addGoodRule = mAddGoodRuleItems.get(arg0);

				arg1 = inflater.inflate(ResourseId, null);
				addGoodRule.et_add_good_rule_size = ViewHolder.get(arg1,
						R.id.et_add_good_rule_size);
				addGoodRule.et_add_good_rule_color = ViewHolder.get(arg1,
						R.id.et_add_good_rule_color);
				addGoodRule.et_add_good_rule_retail_price = ViewHolder.get(
						arg1, R.id.et_add_good_rule_retail_price);
				addGoodRule.et_add_good_rule_inventory = ViewHolder.get(arg1,
						R.id.et_add_good_rule_inventory);
				
				arg1.setTag(addGoodRule);
			} else {
				addGoodRule = (AddGoodRuleItem) arg1.getTag();
			}

			return arg1;
		}

	}

	class AddGoodRuleItem {
		EditText et_add_good_rule_size, et_add_good_rule_color,
				et_add_good_rule_retail_price, et_add_good_rule_inventory;
	}
	
	


	
}

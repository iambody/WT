package io.vtown.WeiTangApp.ui.title.shop.odermanger;


import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.BAddress;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.view.pop.PAddSelect;
import io.vtown.WeiTangApp.comment.view.pop.PAddSelect.AddSelectInterface;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.title.center.set.ASelectAddress;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-7-19 下午7:11:10
 * 修改收货人地址页面
 *  
 */
public class AModifyDeliveryAddress extends ATitleBase {

	private EditText tv_modify_delivery_name;
	private EditText et_modify_delivery_contact_phone_numb;
	private View modify_delivery_address;
	private EditText et_detail_modify_delivery_address;
	private Button btn_modify_delivery_address;
	private TextView comment_txtarrow_content;
	private String space = "  ";
	private EditText tv_modify_delivery_contact_postcode;
	private BAddress ba;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_shop_order_manage_modify_address);
		EventBus.getDefault().register(this,"onGetMsg", BMessage.class);
		ba = (BAddress)getIntent().getSerializableExtra("address_info");
		if(ba == null)return;
		IView();
		IData();
	}



	/**
	 * 初始化控件
	 */
	private void IView() {
		tv_modify_delivery_name = (EditText) findViewById(R.id.tv_modify_delivery_name);
		et_modify_delivery_contact_phone_numb = (EditText) findViewById(R.id.et_modify_delivery_contact_phone_numb);
		modify_delivery_address = findViewById(R.id.modify_delivery_address);
		et_detail_modify_delivery_address = (EditText) findViewById(R.id.et_detail_modify_delivery_address);
		tv_modify_delivery_contact_postcode = (EditText) findViewById(R.id.tv_modify_delivery_contact_postcode);
		btn_modify_delivery_address = (Button) findViewById(R.id.btn_modify_delivery_address);
		
		
		((TextView) modify_delivery_address.findViewById(R.id.comment_txtarrow_title))
		.setText(getResources().getString(R.string.in_location));
		comment_txtarrow_content = (TextView) modify_delivery_address
				.findViewById(R.id.comment_txtarrow_content);
		modify_delivery_address.setOnClickListener(this);
		btn_modify_delivery_address.setOnClickListener(this);
		
	}

	private void IData() {

		StrUtils.SetTxt(tv_modify_delivery_name,ba.getUsrname());
		StrUtils.SetTxt(et_modify_delivery_contact_phone_numb,ba.getMobile());
		StrUtils.SetTxt(et_detail_modify_delivery_address,ba.getAddress());
		StrUtils.SetTxt(comment_txtarrow_content,ba.getProvince()+space+ba.getCity()+space+ba.getArea());

	}
	
	

	@Override
	protected void InitTile() {
		
		SetTitleTxt("修改收货人地址");
		
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
		case R.id.modify_delivery_address://选择地区
			hintKbTwo();
			//PromptManager.SkipActivity(BaseActivity,new Intent(BaseContext,ASelectAddress.class));
			Address();

			break;
			
		case R.id.btn_modify_delivery_address://确认地址
			SaveAddress();
			break;
			
		default:
			break;
		}
	}
	
	private void Address() {
		final PAddSelect m = new PAddSelect(BaseContext,false);

		m.GetAddressResult(new AddSelectInterface() {

			@Override
			public void GetAddResult(String ProviceName, String CityName,
					String DistrictName, String ZipCode) {

				StrUtils.SetTxt(comment_txtarrow_content, ProviceName + space
						+ CityName + space + DistrictName);
//				tv_modify_delivery_contact_postcode.setText(ZipCode);
				et_detail_modify_delivery_address.setText("");
				et_detail_modify_delivery_address
						.setHint(getString(R.string.enter_detail_address));

				m.dismiss();
			}
		});
		m.showAtLocation(modify_delivery_address, Gravity.BOTTOM, 0, 0);
	}
	
	
	private void SaveAddress() {
		String name = tv_modify_delivery_name.getText().toString().trim();

		String mobile = et_modify_delivery_contact_phone_numb.getText().toString().trim();

		String addressInfo = comment_txtarrow_content.getText().toString()
				.trim();

		String address = et_detail_modify_delivery_address.getText().toString().trim();

		if (StrUtils.isEmpty(name)) {
			PromptManager.ShowMyToast(BaseContext, "请输入姓名");
			return;
		}
		if(name.length()>10){
			PromptManager.ShowMyToast(BaseContext, "姓名最多10个汉字");
			return;
		}
		if (!StrUtils.checkMobile(BaseContext, mobile)) {
			return;
		}

		if (StrUtils.isEmpty(addressInfo)) {
			PromptManager.ShowMyToast(BaseContext, "请选择收货地址");
			return;
		}

		String[] split = addressInfo.split(space);

		String province = split[0];
		String city = split[1];
		String county = split[2];
		
//		String postcode = tv_modify_delivery_contact_postcode.getText().toString().trim();
//		if(!StrUtils.CheckPostCode(BaseContext, postcode)){
//			return;
//		}

		if (StrUtils.isEmpty(address)) {
			PromptManager.ShowMyToast(BaseContext, "请输入详细地址");
			return;
		}
		
		

		Bundle bundle = new Bundle();
		bundle.putString("name", name);
		bundle.putString("mobile", mobile);
		bundle.putString("province", province);
		bundle.putString("city", city);
		bundle.putString("county", county);
		bundle.putString("address", address);
		bundle.putString("postcode", "");
		
		Intent intent = new Intent();
		intent.putExtra("AddressInfo", bundle);
		setResult(RESULT_OK, intent);
		this.finish();

	}
	
	

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

	public void onGetMsg(BMessage event){
		int msg_type = event.getMessageType();
		if(BMessage.Tage_Select_Address == msg_type){
			List<String> address_infos = event.getAddress_infos();
			if(address_infos != null && address_infos.size()>0){
				StrUtils.SetTxt(comment_txtarrow_content, address_infos.get(0) + space
						+  address_infos.get(1) + space +  address_infos.get(2));
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try{
			EventBus.getDefault().unregister(this);
		}catch (Exception e){
			return;
		}
	}

}

package io.vtown.WeiTangApp.ui.title.center.set;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import cn.jpush.a.a.i;

import com.android.volley.Request.Method;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.view.pop.PAddSelect;
import io.vtown.WeiTangApp.comment.view.pop.PAddSelect.AddSelectInterface;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-6-8 下午1:22:21 编辑地址
 */
public class AEditAddress extends ATitleBase {

	/**
	 * 收货人姓名输入框
	 */
	private EditText et_edit_address_consignee_name;
	/**
	 * 详细地址输入框
	 */
	private EditText et_edit_addres_detail_address_content;
	/**
	 * 联系电话
	 */
	private RelativeLayout rl_edit_addres_contact_phone_numb;
	/**
	 * 所在地区
	 */
	private View edit_address_location;
	/**
	 * 保存按钮
	 */
	private Button btn_edit_addres_save;
	/**
	 * 查找联系人按钮
	 */
	private ImageView iv_edit_addres_look_connect;
	/**
	 * 联系电话
	 */
	private EditText et_edit_addres_contact_phone_numb;
	/**
	 * 显示地址
	 */
	private TextView comment_txtarrow_title;
	/**
	 * 传进来的bean
	 */
	private BLComment bl;

	private String space = "  ";
	/**
	 * 地区显示
	 */
	private TextView comment_txtarrow_content;
	/**
	 * 用户信息
	 */
	private BUser user_Get;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_center_set_personal_data_addressmanage_edit);
		EventBus.getDefault().register(this,"onGetMsg", BMessage.class);
		user_Get = Spuit.User_Get(getApplicationContext());
		IView();
		IData();

	}

	/**
	 * 网络请求
	 * 
	 * @param address_id
	 * @param member_id
	 * @param name
	 * @param mobile
	 * @param tel
	 * @param province
	 * @param city
	 * @param county
	 * @param address
	 */
	private void IHttp(String address_id, String member_id, String name,
			String mobile, String tel, String province, String city,
			String county, String address, String street_address) {
		SetTitleHttpDataLisenter(this);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("address_id", address_id);
		map.put("member_id", member_id);
		map.put("name", name);
		map.put("mobile", mobile);
		map.put("tel", tel);
		map.put("province", province);
		map.put("city", city);
		map.put("county", county);
		map.put("address", address);
		map.put("street_address", street_address);
		FBGetHttpData(map, Constants.Modify_Address, Method.PUT, 0,
				LOAD_INITIALIZE);
	}

	private void IData() {

		bl = (BLComment) getIntent().getSerializableExtra("data");
		StrUtils.SetTxt(et_edit_address_consignee_name, bl.getName());
		StrUtils.SetTxt(et_edit_addres_contact_phone_numb, bl.getMobile());

		
		StrUtils.SetTxt(
				comment_txtarrow_content,
				 bl.getProvince() + space + bl.getCity()
						+ space + bl.getCounty());
		StrUtils.SetTxt(et_edit_addres_detail_address_content, bl.getAddress());

	}

	private void IView() {

		et_edit_address_consignee_name = (EditText) findViewById(R.id.et_edit_address_consignee_name);
		et_edit_addres_detail_address_content = (EditText) findViewById(R.id.et_edit_addres_detail_address_content);
		et_edit_addres_contact_phone_numb = (EditText) findViewById(R.id.et_edit_addres_contact_phone_numb);
		rl_edit_addres_contact_phone_numb = (RelativeLayout) findViewById(R.id.rl_edit_addres_contact_phone_numb);
		iv_edit_addres_look_connect = (ImageView) findViewById(R.id.iv_edit_addres_look_connect);
		edit_address_location = findViewById(R.id.edit_address_location);
		btn_edit_addres_save = (Button) findViewById(R.id.btn_edit_addres_save);

		comment_txtarrow_title = (TextView) edit_address_location
				.findViewById(R.id.comment_txtarrow_title);
		comment_txtarrow_title.setText(getResources().getString(R.string.in_location));
		comment_txtarrow_content = (TextView) edit_address_location.findViewById(R.id.comment_txtarrow_content);
		
		// comment_txtarrow_title.setText(getResources().getString(R.string.in_location));

		btn_edit_addres_save.setOnClickListener(this);
		iv_edit_addres_look_connect.setOnClickListener(this);
		edit_address_location.setOnClickListener(this);
	}

	@Override
	protected void InitTile() {
		SetTitleTxt("编辑");
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
		if (200 == Code) {
			btn_edit_addres_save.setEnabled(true);
			DataError("修改成功", 1);
			Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			finish();
		}
	}

	@Override
	protected void DataError(String error, int LoadType) {
		btn_edit_addres_save.setEnabled(true);
		PromptManager.ShowMyToast(BaseContext, error);

	}

	@Override
	protected void NetConnect() {
		NetError.setVisibility(View.GONE);
		btn_edit_addres_save.setEnabled(true);
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
		switch (V.getId()) {
		case R.id.btn_edit_addres_save:
			if(CheckNet(BaseContext))return;
			
			ShowCustomDialog("是否保存地址？", "取消", "保存", new IDialogResult() {
				
				@Override
				public void RightResult() {
					btn_edit_addres_save.setEnabled(false);
					ModityAddress();

				}
				
				@Override
				public void LeftResult() {
					btn_edit_addres_save.setEnabled(true);
				}
			});
			
			

			break;

		case R.id.iv_edit_addres_look_connect:
			goContacts();
			break;

		case R.id.edit_address_location:

			hintKbTwo();
			//PromptManager.SkipActivity(BaseActivity,new Intent(BaseContext,ASelectAddress.class));
			Address();
			break;

		}
	}

	private void ModityAddress() {

		String name = et_edit_address_consignee_name.getText().toString()
				.trim();

		String mobile = et_edit_addres_contact_phone_numb.getText().toString()
				.trim();

		String addressInfo = comment_txtarrow_content.getText().toString().trim();

		String address = et_edit_addres_detail_address_content.getText()
				.toString().trim();


		String[] split = addressInfo.split(space);

		String province = split[0];
		String city = split[1];
		String county = split[2];
		

		if (StrUtils.isEmpty(name)) {
			PromptManager.ShowMyToast(BaseContext, "请输入姓名");
			btn_edit_addres_save.setEnabled(true);
			return;
		}
		if (!StrUtils.checkMobile(BaseContext,mobile)) {
			btn_edit_addres_save.setEnabled(true);
			return;
		}

		if (StrUtils.isEmpty(addressInfo)) {
			PromptManager.ShowMyToast(BaseContext, "请选择收货地址");
			btn_edit_addres_save.setEnabled(true);
			return;
		}

		if (StrUtils.isEmpty(address)) {
			PromptManager.ShowMyToast(BaseContext, "请输入详细地址");
			btn_edit_addres_save.setEnabled(true);
			return;
		}

		IHttp(bl.getAddress_id(), user_Get.getId(), name, mobile, "",
				province, city, county, address, ".");
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

	/**
	 * 从联系人中选择
	 */
	private void goContacts() {
		Uri uri = Uri.parse("content://contacts/people");
		Intent intent = new Intent(Intent.ACTION_PICK, uri);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case 0:
			if (data == null) {
				return;
			}
			// 处理返回的data,获取选择的联系人信息
			Uri uri = data.getData();
			String[] contacts = getPhoneContacts(uri);
			StrUtils.SetTxt(et_edit_address_consignee_name, contacts[0]);
			StrUtils.SetTxt(et_edit_addres_contact_phone_numb, contacts[1]);
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private String[] getPhoneContacts(Uri uri) {

		String[] contact = new String[2];
		// 得到ContentResolver对象
		ContentResolver cr = getContentResolver();
		// 取得电话本中开始一项的光标
		Cursor cursor = cr.query(uri, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			// 取得联系人姓名
			int nameFieldColumnIndex = cursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
			contact[0] = cursor.getString(nameFieldColumnIndex);
			// 取得电话号码
			String ContactId = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			Cursor phoneCur = cr.query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID
							+ "=?", new String[] { ContactId }, null);
			if (phoneCur != null) {
				phoneCur.moveToFirst();
				contact[1] = phoneCur
						.getString(phoneCur
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			}
			phoneCur.close();
			cursor.close();
		} else {
			return null;
		}
		return contact;

	}

	private void Address() {
		final PAddSelect m = new PAddSelect(BaseContext,false);

		m.GetAddressResult(new AddSelectInterface() {

			@Override
			public void GetAddResult(String ProviceName, String CityName,
					String DistrictName, String ZipCode) {

				StrUtils.SetTxt(comment_txtarrow_content, ProviceName + "  "
						+ CityName + "  " + DistrictName);
				et_edit_addres_detail_address_content.setText("");
				et_edit_addres_detail_address_content
						.setHint(getString(R.string.enter_detail_address));

				m.dismiss();
			}
		});
		m.showAtLocation(edit_address_location, Gravity.BOTTOM, 0, 0);
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

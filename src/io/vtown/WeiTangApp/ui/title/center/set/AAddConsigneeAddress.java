package io.vtown.WeiTangApp.ui.title.center.set;

import java.util.HashMap;

import com.android.volley.Request.Method;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.view.pop.PAddSelect;
import io.vtown.WeiTangApp.comment.view.pop.PAddSelect.AddSelectInterface;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-5-22 下午4:55:51
 * 
 *          新建收货人地址页面
 * 
 */
public class AAddConsigneeAddress extends ATitleBase {

	/**
	 * 收货人姓名输入框
	 */
	private EditText et_consignee_name;
	/**
	 * 详细地址输入框
	 */
	private EditText et_detail_address_content;
	/**
	 * 联系电话
	 */
	private RelativeLayout rl_contact_phone_numb;
	/**
	 * 所在地区
	 */
	private View location;
	/**
	 * 保存按钮
	 */
	private Button btn_save;
	/**
	 * 查找联系人按钮
	 */
	private ImageView iv_look_connect;
	/**
	 * 联系电话
	 */
	private EditText et_contact_phone_numb;

	private String space = "  ";
	/**
	 * 显示地址
	 */
	private TextView comment_txtarrow_content;

	@Override
	protected void InItBaseView() {

		setContentView(R.layout.activity_center_set_personal_data_addressmanage_add_newaddress);
		IView();
	}

	private void IView() {
		et_consignee_name = (EditText) findViewById(R.id.et_consignee_name);
		et_detail_address_content = (EditText) findViewById(R.id.et_detail_address_content);
		et_contact_phone_numb = (EditText) findViewById(R.id.et_contact_phone_numb);
		rl_contact_phone_numb = (RelativeLayout) findViewById(R.id.rl_contact_phone_numb);

		iv_look_connect = (ImageView) findViewById(R.id.iv_look_connect);
		location = findViewById(R.id.location);

		comment_txtarrow_content = (TextView) location
				.findViewById(R.id.comment_txtarrow_addshouhuo_content);

		btn_save = (Button) findViewById(R.id.btn_save);

		((TextView) location.findViewById(R.id.comment_txtarrow_addshouhuo_title))
				.setText(getResources().getString(R.string.in_location));

		btn_save.setOnClickListener(this);
		iv_look_connect.setOnClickListener(this);
		location.setOnClickListener(this);
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
	private void IHttp(String member_id, String name, String mobile,
			String tel, String province, String city, String county,
			String address, String street_address) {
		SetTitleHttpDataLisenter(this);
		PromptManager.showLoading(BaseContext);
		HashMap<String, String> map = new HashMap<String, String>();

		map.put("member_id", member_id);
		map.put("name", name);
		map.put("mobile", mobile);
		map.put("tel", tel);
		map.put("province", province);
		map.put("city", city);
		map.put("county", county);
		map.put("address", address);
		map.put("street_address", street_address);
		FBGetHttpData(map, Constants.Add_New_Address, Method.POST, 0,
				LOAD_INITIALIZE);
	}

	@Override
	protected void InitTile() {

		SetTitleTxt(getString(R.string.new_consignee_address));
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
		if (200 == Code) {
			btn_save.setEnabled(true);
			DataError("保存成功", 1);
			Intent intent = new Intent();
			setResult(RESULT_OK, intent);
			finish();
		}
	}

	@Override
	protected void DataError(String error, int LoadTyp) {
		PromptManager.ShowMyToast(BaseContext, error);
		btn_save.setEnabled(true);
	}

	@Override
	protected void NetConnect() {
		NetError.setVisibility(View.GONE);
		btn_save.setEnabled(true);
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
		case R.id.btn_save:
			if (CheckNet(BaseContext)) {
				return;
			}
			SaveAddress();
			break;

		case R.id.iv_look_connect:
			goContacts();
			break;

		case R.id.location:
			//隐藏系统键盘
			InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			im.hideSoftInputFromWindow(getCurrentFocus()
					.getApplicationWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
			Address();
			break;

		}

	}

	private void SaveAddress() {
		btn_save.setEnabled(false);
		String name = et_consignee_name.getText().toString().trim();

		String mobile = et_contact_phone_numb.getText().toString().trim();

		String addressInfo = comment_txtarrow_content.getText().toString()
				.trim();

		String address = et_detail_address_content.getText().toString().trim();

		if (StrUtils.isEmpty(name)) {
			PromptManager.ShowMyToast(BaseContext, "请输入姓名");
			btn_save.setEnabled(true);
			return;
		}
		if (!StrUtils.checkMobile(BaseContext, mobile)) {
			btn_save.setEnabled(true);
			return;
		}

		if (StrUtils.isEmpty(addressInfo)) {
			PromptManager.ShowMyToast(BaseContext, "请选择收货地址");
			btn_save.setEnabled(true);
			return;
		}

		String[] split = addressInfo.split(space);

		String province = split[0];
		String city = split[1];
		String county = split[2];

		if (StrUtils.isEmpty(address)) {
			PromptManager.ShowMyToast(BaseContext, "请输入详细地址");
			btn_save.setEnabled(true);
			return;
		}

		BUser user_Get = Spuit.User_Get(getApplicationContext());
		IHttp(user_Get.getId(), name, mobile, "", province, city, county,
				address, ".");

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
			StrUtils.SetTxt(et_consignee_name, contacts[0]);
			StrUtils.SetTxt(et_contact_phone_numb, contacts[1]);

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

				StrUtils.SetTxt(comment_txtarrow_content, ProviceName + space
						+ CityName + space + DistrictName);
				et_detail_address_content.setText("");
				et_detail_address_content
						.setHint(getString(R.string.enter_detail_address));

				m.dismiss();
			}
		});
		m.showAtLocation(location, Gravity.BOTTOM, 0, 0);
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}
}

package io.vtown.WeiTangApp.ui.title.center.set;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.alibaba.fastjson.JSON;
import com.android.volley.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.BLSelectAddress;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * Created by Yihuihua on 2016/9/18.
 */
public class ASelectAddress extends ATitleBase implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ImageView go_back;
    private ListView address_select_list_province;
    private List<String> address = new ArrayList<String>();
    private ListView address_select_list_city;
    private ListView address_select_list_address;
    private static final int TAG_PROVINCE = 112;
    private static final int TAG_CITY = 113;
    private static final int TAG_ADDRESS = 114;
    private int Current_Tag = TAG_PROVINCE;
    private AddressSelectAdapter addressSelectAdapter_province;
    private AddressSelectAdapter addressSelectAdapter_city;
    private AddressSelectAdapter addressSelectAdapter_area;
    private String province_str = "";
    private String city_str = "";
    private String area_str = "";


    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_select_address);
        IView();
        IProvinceData();

    }

    //获取省
    private void IProvinceData() {
        SetTitleHttpDataLisenter(this);
        PromptManager.showLoading(BaseContext);
        HashMap<String, String> map = new HashMap<String, String>();
        FBGetHttpData(map, Constants.Get_Province, Request.Method.GET, 1, LOAD_INITIALIZE);
    }

    //获取市
    private void ICityData(String provinceid) {
        PromptManager.showLoading(BaseContext);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("seller_id", Spuit.User_Get(BaseActivity).getSeller_id());
        map.put("provinceid", provinceid);
        FBGetHttpData(map, Constants.Get_City, Request.Method.GET, 2, LOAD_INITIALIZE);
    }

    //获取区
    private void IAreaData(String cityid) {
        PromptManager.showLoading(BaseContext);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("cityid", cityid);
        FBGetHttpData(map, Constants.Get_Area, Request.Method.GET, 3, LOAD_INITIALIZE);
    }

    @Override
    protected void InitTile() {
        SetTitleTxt("");
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {

        if (StrUtils.isEmpty(Data.getHttpResultStr())) {
            return;
        }
        List<BLSelectAddress> address_info = new ArrayList<BLSelectAddress>();
        try {
            address_info = JSON.parseArray(Data.getHttpResultStr(), BLSelectAddress.class);
            switch (Data.getHttpResultTage()) {
                case 1:
                    Current_Tag = TAG_PROVINCE;

                    break;
                case 2:
                    Current_Tag = TAG_CITY;

                    break;

                case 3:
                    Current_Tag = TAG_ADDRESS;

                    break;
            }

            IAddressList(address_info, Current_Tag);
        } catch (Exception e) {
            return;
        }


    }

    @Override
    protected void DataError(String error, int LoadType) {
        PromptManager.ShowMyToast(BaseContext, error);
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
        goBack();
    }

    @Override
    protected void InItBundle(Bundle bundle) {

    }

    @Override
    protected void SaveBundle(Bundle bundle) {

    }


    private void goBack(){
        switch (Current_Tag) {
            case TAG_PROVINCE:
                this.finish();
                Current_Tag = TAG_PROVINCE;
                province_str = "";
                city_str ="";
                area_str = "";
                break;

            case TAG_CITY:
                Current_Tag = TAG_PROVINCE;
                province_str = "";
                address_select_list_city.setVisibility(View.GONE);
                address_select_list_province.setVisibility(View.VISIBLE);
                break;

            case TAG_ADDRESS:
                Current_Tag = TAG_CITY;
                city_str ="";
                address_select_list_city.setVisibility(View.VISIBLE);
                address_select_list_address.setVisibility(View.GONE);
                address_select_list_province.setVisibility(View.GONE);
                break;

        }
    }


    private void IAddressList(List<BLSelectAddress> address_info, int loadtype) {


        switch (loadtype) {
            case TAG_PROVINCE:
                addressSelectAdapter_province = new AddressSelectAdapter(R.layout.item_address_select, address_info, TAG_PROVINCE);
                address_select_list_province = (ListView) findViewById(R.id.address_select_list_province);
                address_select_list_province.setVisibility(View.VISIBLE);
                address_select_list_province.setAdapter(addressSelectAdapter_province);
                address_select_list_province.setOnItemClickListener(this);
                break;

            case TAG_CITY:
                address_select_list_province.setVisibility(View.GONE);
                address_select_list_city = (ListView) findViewById(R.id.address_select_list_city);
                address_select_list_city.setVisibility(View.VISIBLE);
                addressSelectAdapter_city = new AddressSelectAdapter(R.layout.item_address_select, address_info, TAG_CITY);
                address_select_list_city.setAdapter(addressSelectAdapter_city);
                address_select_list_city.setOnItemClickListener(this);
                break;

            case TAG_ADDRESS:
                address_select_list_city.setVisibility(View.GONE);
                address_select_list_province.setVisibility(View.GONE);
                address_select_list_address = (ListView) findViewById(R.id.address_select_list_address);
                address_select_list_address.setVisibility(View.VISIBLE);
                addressSelectAdapter_area = new AddressSelectAdapter(R.layout.item_address_select, address_info, TAG_ADDRESS);
                address_select_list_address.setAdapter(addressSelectAdapter_area);
                address_select_list_address.setOnItemClickListener(this);
                break;
        }


    }


    private void IView() {
        go_back = (ImageView) findViewById(R.id.go_back);
        go_back.setOnClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (Current_Tag) {
            case TAG_PROVINCE:
                address_select_list_province.setVisibility(View.GONE);
                address_select_list_city.setVisibility(View.GONE);
                BLSelectAddress province = (BLSelectAddress) addressSelectAdapter_province.getItem(position);
                ICityData(province.getProvinceid());
                province_str = province.getProvince();
                break;

            case TAG_CITY:
                address_select_list_province.setVisibility(View.GONE);
                address_select_list_city.setVisibility(View.GONE);
                address_select_list_address.setVisibility(View.GONE);
                BLSelectAddress city = (BLSelectAddress) addressSelectAdapter_city.getItem(position);
                IAreaData(city.getCityid());
                city_str = city.getCity();
                break;

            case TAG_ADDRESS:
                BLSelectAddress area = (BLSelectAddress) addressSelectAdapter_area.getItem(position);
                area_str = area.getArea();
                address.add(province_str);
                address.add(city_str);
                address.add(area_str);
                BMessage event = new BMessage(BMessage.Tage_Select_Address);
                event.setAddress_infos(address);
                EventBus.getDefault().post(event);
                this.finish();
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    class AddressSelectAdapter extends BaseAdapter {

        private int ResourseID;
        private LayoutInflater inflater;
        private List<BLSelectAddress> datas = new ArrayList<BLSelectAddress>();

        private int loadtype;

        public AddressSelectAdapter(int ResourseID, List<BLSelectAddress> datas, int loadtype) {
            super();
            this.loadtype = loadtype;
            this.ResourseID = ResourseID;
            this.datas = datas;
            this.inflater = LayoutInflater.from(BaseContext);
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
            AddressSelectItem holder = null;
            if (convertView == null) {
                holder = new AddressSelectItem();
                convertView = inflater.inflate(ResourseID, null);
                holder.adress_content_layout = (LinearLayout) convertView.findViewById(R.id.adress_content_layout);
                holder.address_content = (TextView) convertView.findViewById(R.id.address_content);

                convertView.setTag(holder);
            } else {
                holder = (AddressSelectItem) convertView.getTag();
            }
            switch (loadtype) {
                case TAG_PROVINCE:
                    StrUtils.SetTxt(holder.address_content, datas.get(position).getProvince());
                    break;

                case TAG_CITY:
                    StrUtils.SetTxt(holder.address_content, datas.get(position).getCity());
                    break;

                case TAG_ADDRESS:
                    StrUtils.SetTxt(holder.address_content, datas.get(position).getArea());
                    break;
            }
            return convertView;
        }
    }

    class AddressSelectItem {
        LinearLayout adress_content_layout;
        TextView address_content;
    }
}

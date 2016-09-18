package io.vtown.WeiTangApp.ui.title.center.set;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.comment.util.StrUtils;

/**
 * Created by Yihuihua on 2016/9/18.
 */
public class ASelectAddress extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ImageView go_back;
    private ListView address_select_list_province;
    private List<String> provinceList = new ArrayList<String>();
    private List<String> cityList = new ArrayList<String>();
    private List<String> addressList = new ArrayList<String>();
    private ListView address_select_list_city;
    private ListView address_select_list_address;
    private static final int TAG_PROVINCE = 112;
    private static final int TAG_CITY = 113;
    private static final int TAG_ADDRESS = 114;
    private int Current_Tag = TAG_PROVINCE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_address);
             String[] names = getResources().getStringArray(R.array.address);
        for(int i = 0; i < names.length; i++){
            provinceList.add(names[i]);
        }

        String[] usr_names = getResources().getStringArray(R.array.usr);
        for(int i = 0; i < usr_names.length; i++){
            cityList.add(usr_names[i]);
        }
        IView();
        IProvinceList();

    }

    private void IProvinceList() {
        Current_Tag = TAG_PROVINCE;


        address_select_list_province = (ListView) findViewById(R.id.address_select_list_province);
        address_select_list_province.setVisibility(View.VISIBLE);
        AddressSelectAdapter addressSelectAdapter = new AddressSelectAdapter(R.layout.item_address_select, provinceList);
        address_select_list_province.setAdapter(addressSelectAdapter);
        address_select_list_province.setOnItemClickListener(this);


    }

    private void ICityList(){

        Current_Tag = TAG_CITY;
        address_select_list_city = (ListView) findViewById(R.id.address_select_list_city);
        address_select_list_city.setVisibility(View.VISIBLE);
        address_select_list_province.setVisibility(View.GONE);
        AddressSelectAdapter addressSelectAdapter = new AddressSelectAdapter(R.layout.item_address_select, cityList);
        address_select_list_city.setAdapter(addressSelectAdapter);
        address_select_list_city.setOnItemClickListener(this);
    }

    private void IAddressList(){
        Current_Tag = TAG_ADDRESS;
        address_select_list_address = (ListView) findViewById(R.id.address_select_list_address);
        address_select_list_address.setVisibility(View.VISIBLE);
        address_select_list_city.setVisibility(View.GONE);
        address_select_list_province.setVisibility(View.GONE);
        AddressSelectAdapter addressSelectAdapter = new AddressSelectAdapter(R.layout.item_address_select, cityList);
        address_select_list_address.setAdapter(addressSelectAdapter);
        address_select_list_address.setOnItemClickListener(this);
    }

    private void IView() {
        go_back = (ImageView) findViewById(R.id.go_back);
        go_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {


                switch (Current_Tag){
                    case TAG_PROVINCE:
                        this.finish();
                        break;

                    case TAG_CITY:
                        Current_Tag = TAG_PROVINCE;
                        address_select_list_city.setVisibility(View.GONE);
                        //address_select_list_address.setVisibility(View.GONE);
                        address_select_list_province.setVisibility(View.VISIBLE);
                        break;

                    case TAG_ADDRESS:
                        Current_Tag = TAG_CITY;
                        address_select_list_city.setVisibility(View.VISIBLE);
                        address_select_list_address.setVisibility(View.GONE);
                        address_select_list_province.setVisibility(View.GONE);
                        break;

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (Current_Tag){
            case TAG_PROVINCE:
                ICityList();
                break;

            case TAG_CITY:

                break;

            case TAG_ADDRESS:

                break;
        }
    }

    class AddressSelectAdapter extends BaseAdapter {

        private int ResourseID;
        private LayoutInflater inflater;
        private List<String> addressess = new ArrayList<String>();
//        private List<String> city = new ArrayList<String>();
//        private List<String> address = new ArrayList<String>();


        public AddressSelectAdapter(int ResourseID,List<String> address) {
            super();
            this.ResourseID = ResourseID;
            this.addressess = address;
            this.inflater = LayoutInflater.from(ASelectAddress.this);
        }

        @Override
        public int getCount() {
            return addressess.size();
        }

        @Override
        public Object getItem(int position) {
            return addressess.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AddressSelectItem holder = null;
            if(convertView == null){
                holder = new AddressSelectItem();
                convertView = inflater.inflate(ResourseID,null);
                holder.adress_content_layout = (LinearLayout) convertView.findViewById(R.id.adress_content_layout);
                holder.address_content = (TextView) convertView.findViewById(R.id.address_content);

                convertView.setTag(holder);
            }else {
                holder = (AddressSelectItem) convertView.getTag();
            }
            StrUtils.SetTxt( holder.address_content,addressess.get(position));

            return convertView;
        }
    }

    class AddressSelectItem {
        LinearLayout adress_content_layout;
        TextView address_content;
    }
}

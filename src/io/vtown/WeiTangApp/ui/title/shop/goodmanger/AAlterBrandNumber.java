package io.vtown.WeiTangApp.ui.title.shop.goodmanger;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.BGoodDetail;
import io.vtown.WeiTangApp.bean.bcomment.easy.gooddetail.BDataGood;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.AddAndSubView;
import io.vtown.WeiTangApp.comment.view.AddAndSubView.OnNumChangeListener;
import io.vtown.WeiTangApp.comment.view.custom.CompleteListView;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.ui.ATitleBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

import de.greenrobot.event.EventBus;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-8-15 下午2:00:12
 */
public class AAlterBrandNumber extends ATitleBase {

    /**
     * 用户信息
     */
    private BUser user_Get;

    /**
     * 是否是采购
     */
    private Boolean IsCaiGou = false;

    /**
     * 传递的String
     */
    private String GoodsId;

    public final static String Tage_Good_Manager_CaiGou = "iscaigou";

    /**
     * 商品图片
     */
    private ImageView iv_shop_good_manager_modify_store_icon;

    /**
     * 商品名称
     */
    private TextView tv_shop_good_manager_modify_store_good_name;

    /**
     * 卖家名称
     */
    private TextView tv_shop_good_manager_modify_store_good_seller;

    /**
     * 代理等级
     */
    private TextView tv_shop_good_manager_modify_store_good_level;

    /**
     * 修改完成按钮
     */
    private TextView tv_shop_good_manager_modify_store_modify;

    /**
     * 规格列表
     */
    private CompleteListView lv_content_list;

    /**
     * AP
     */
    private ContentAdapter mAdapter;

    private LinearLayout shop_good_manager_modify_content_outlay;

    private View shop_good_manager_modify_content_nodata_lay;

    /**
     * 保存规格的数据列表
     */
    private List<BDataGood> myDataGoods;

    /**
     * 传递进来修改的fragment的记录位置
     */
    private int Postion;
    private int PostNumber;


    @Override
    protected void InItBaseView() {

        setContentView(R.layout.activity_shop_good_manager_modify_store);
        user_Get = Spuit.User_Get(BaseContext);
        IsCaiGou = getIntent().getBooleanExtra(Tage_Good_Manager_CaiGou, false);
        Postion = getIntent().getIntExtra("postion", -1);
        EventBus.getDefault().register(this, "ReciverListen", BMessage.class);

        IIBundle();
        IView();
        IData();
    }

    private void IIBundle() {
        if (!getIntent().getExtras().containsKey("goodid"))
            BaseActivity.finish();
        GoodsId = getIntent().getStringExtra("goodid");
    }

    private void IView() {
        shop_good_manager_modify_content_outlay = (LinearLayout) findViewById(R.id.shop_good_manager_modify_content_outlay);
        shop_good_manager_modify_content_nodata_lay = findViewById(R.id.shop_good_manager_modify_content_nodata_lay);
        IDataView(shop_good_manager_modify_content_outlay,
                shop_good_manager_modify_content_nodata_lay, NOVIEW_INITIALIZE);
        iv_shop_good_manager_modify_store_icon = (ImageView) findViewById(R.id.iv_shop_good_manager_modify_store_icon);
        // iv_shop_good_manager_modify_store_good_is_agent = (ImageView)
        // findViewById(R.id.iv_shop_good_manager_modify_store_good_is_agent);
        tv_shop_good_manager_modify_store_good_name = (TextView) findViewById(R.id.tv_shop_good_manager_modify_store_good_name);
        tv_shop_good_manager_modify_store_good_seller = (TextView) findViewById(R.id.tv_shop_good_manager_modify_store_good_seller);
        tv_shop_good_manager_modify_store_good_level = (TextView) findViewById(R.id.tv_shop_good_manager_modify_store_good_level);
        tv_shop_good_manager_modify_store_modify = (TextView) findViewById(R.id.tv_shop_good_manager_modify_store_modify);
        lv_content_list = (CompleteListView) findViewById(R.id.lv_content_list);
        tv_shop_good_manager_modify_store_modify.setOnClickListener(this);
        lv_content_list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                BDataGood dataGood = (BDataGood) arg0.getItemAtPosition(arg2);
                PromptManager.SkipActivity(BaseActivity, new Intent(
                        BaseActivity, ABrandNumberEdit.class).putExtra(
                        "edpostion", arg2));

            }
        });
    }

    /**
     * 修改数量后进行的监听 需要修改ap
     *
     * @param message
     */
    public void ReciverListen(BMessage message) {
        if (message.getMessageType() == BMessage.Good_Manger_Edit) {
            String GoodNumber = message.getGoodMangeAlterNUmbe();
            int Postion = message.getGoodMangeAlterPostion();

            if (!StrUtils.isEmpty(GoodNumber)) {
                mAdapter.SetPostionAlter(Postion, GoodNumber);
            }

        }
    }

    private void RefreshView(BGoodDetail data) {
        ImageLoaderUtil.Load2(data.getCover(),
                iv_shop_good_manager_modify_store_icon, R.drawable.error_iv2);

        StrUtils.SetTxt(tv_shop_good_manager_modify_store_good_name,
                data.getTitle());
        StrUtils.SetColorsTxt(BaseContext,
                tv_shop_good_manager_modify_store_good_seller,
                R.color.app_gray, "卖        家：", data.getSeller_name());
        int level = Integer.parseInt(data.getSlevel());
        String level_str = "";
        switch (level) {
            case 0:
                level_str = "特级代理";
                break;
            case 1:
                level_str = "一级代理";
                break;
            case 2:
                level_str = "二级代理";
                break;
            case 3:
                level_str = "三级代理";
                break;
            case 4:
                level_str = "四级代理";
                break;
            case 5:
                level_str = "五级代理";
                break;
            default:
                level_str = "";
                break;
        }
        StrUtils.SetColorsTxt(BaseContext,
                tv_shop_good_manager_modify_store_good_level, R.color.app_gray,
                "代卖等级：", level_str);

        myDataGoods = data.getGoods_attr();
        IList(myDataGoods);

    }

    private void IList(List<BDataGood> datas) {
        mAdapter = new ContentAdapter(
                R.layout.item_shop_manager_modify_content, datas);
        lv_content_list.setAdapter(mAdapter);
    }

    private void IData() {
        PromptManager.showtextLoading(BaseContext,
                getResources()
                        .getString(R.string.xlistview_header_hint_loading));
        SetTitleHttpDataLisenter(this);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("goods_id", GoodsId);
        map.put("extend", "1");
        map.put("member_id", user_Get.getId());
        map.put("buy_type", IsCaiGou ? "1" : "0");
        map.put("seller_id", user_Get.getSeller_id());
        FBGetHttpData(map, Constants.GoodDetail, Method.GET, 0, LOAD_INITIALIZE);
    }

    private void ModifyContent(String attrs) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("seller_id", user_Get.getSeller_id());
        map.put("goods_id", GoodsId);
        map.put("attrs", attrs);
        FBGetHttpData(map, Constants.Modify_Content, Method.PUT, 1, 111);

    }

    @Override
    protected void InitTile() {
        SetTitleTxt("修改库存");
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {

        switch (Data.getHttpResultTage()) {
            case 0:
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {

                    BaseActivity.finish();
                    PromptManager.ShowCustomToast(BaseContext, Msg);
                    return;
                }
                BGoodDetail data = new BGoodDetail();
                try {
                    data = JSON.parseObject(Data.getHttpResultStr(),
                            BGoodDetail.class);
                } catch (Exception e) {

                }
                RefreshView(data);
                IDataView(shop_good_manager_modify_content_outlay,
                        shop_good_manager_modify_content_nodata_lay, NOVIEW_RIGHT);
                break;

            case 1:
                PromptManager.ShowCustomToast(BaseContext, "修改成功");
                BMessage MyBmBMessage = new BMessage(BMessage.Good_Manger_ToEdit_num);
                MyBmBMessage.setPostEditPostion(Postion);
//
                MyBmBMessage.setAlterAllNumber(PostNumber);
                EventBus.getDefault().post(MyBmBMessage);
                this.finish();
                break;

            default:
                break;
        }

    }

    @Override
    protected void DataError(String error, int LoadType) {
        PromptManager.ShowCustomToast(BaseContext, error);
        if (LOAD_INITIALIZE == LoadType && 111 != LoadType) {
            IDataView(shop_good_manager_modify_content_outlay,
                    shop_good_manager_modify_content_nodata_lay, NOVIEW_ERROR);
        }
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
            case R.id.tv_shop_good_manager_modify_store_modify:

                final String content = getModifyContentAttr().toString().trim();
                if (StrUtils.isEmpty(content)) {
                    return;
                }
                ShowCustomDialog("确定修改库存?",
                        getResources().getString(R.string.cancle), getResources()
                                .getString(R.string.queding), new IDialogResult() {
                            @Override
                            public void RightResult() {
                                ModifyContent(content);

                            }

                            @Override
                            public void LeftResult() {
                            }
                        });

                break;

            default:
                break;
        }
    }

    private JSONObject getModifyContentAttr() {
        JSONObject jsonObject = new JSONObject();
        List<BDataGood> apComments = mAdapter.getDatas();
        PostNumber = 0;
        try {
            for (int i = 0; i < mAdapter.getCount(); i++) {

                jsonObject.put(apComments.get(i).getAttr_id(), apComments
                        .get(i).getStore());
                PostNumber = PostNumber + StrUtils.toInt(apComments.get(i).getStore());

            }
        } catch (Exception e) {
        }
        return jsonObject;
    }

    @Override
    protected void InItBundle(Bundle bundle) {
    }

    @Override
    protected void SaveBundle(Bundle bundle) {
    }

    class ContentAdapter extends BaseAdapter {
        private int ResoureId;
        private LayoutInflater inflater;
        private List<BDataGood> datas = new ArrayList<BDataGood>();

        public ContentAdapter(int ResoureId, List<BDataGood> datas) {
            super();
            this.ResoureId = ResoureId;
            for (int i = 0; i < datas.size(); i++) {
                datas.get(i).setOdlestore(datas.get(i).getStore());
            }
            this.datas = datas;

            this.inflater = LayoutInflater.from(BaseContext);
        }

        public List<BDataGood> getDatas() {
            return datas;
        }

        public void SetPostionAlter(int Postopn, String alNNumber) {
            for (int i = 0; i < datas.size(); i++) {
                if (Postopn == i) {
                    datas.get(i).setStore(alNNumber);
                    break;
                }
            }
            this.notifyDataSetChanged();
        }

        /**
         * 刷新
         *
         * @param dd
         */
        public void frashDatas(List<BDataGood> dd) {
            this.datas = dd;
            this.notifyDataSetChanged();
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
            ContentItem item = null;
            if (convertView == null) {
                convertView = inflater.inflate(ResoureId, null);
                item = new ContentItem();
                item.good_manager_modify_content_name = ViewHolder.get(
                        convertView, R.id.good_manager_modify_content_name);
                item.good_manager_modify_content_number_before = ViewHolder
                        .get(convertView,
                                R.id.good_manager_modify_content_number_before);

                item.good_manager_modify_content_number_after = (TextView) convertView
                        .findViewById(R.id.good_manager_modify_content_number_after);
                item.good_manager_modify_content_number_after_display = ViewHolder
                        .get(convertView,
                                R.id.good_manager_modify_content_number_after_display);

                // item.good_manager_modify_content_number_after = (EditText)
                // convertView
                // .findViewById(R.id.good_manager_modify_content_number_after);

                convertView.setTag(item);
            } else {
                item = (ContentItem) convertView.getTag();
            }

            StrUtils.SetTxt(item.good_manager_modify_content_name, ""
                    + datas.get(position).getAttr_name());
            // datas.get(position).getAttr_name());
            StrUtils.SetColorsTxt(BaseContext,
                    item.good_manager_modify_content_number_before,
                    R.color.app_gray, "修改前库存：", datas.get(position)
                            .getOdlestore());
            item.good_manager_modify_content_number_after.setText(datas.get(
                    position).getStore());
            // item.good_manager_modify_content_number_after_display.setText(String.format("您修改后的库存：%1$s",
            // attr_dataMap.get(position)));
            item.good_manager_modify_content_number_after_display
                    .setVisibility(View.GONE);
            item.good_manager_modify_content_number_after.setTag(datas.get(
                    position).getAttr_id());
            return convertView;
        }

    }

    // class MyListener implements OnNumChangeListener {
    // private Integer Postion;
    // private ContentItem item;
    //
    // public MyListener(ContentItem item, int position) {
    // this.Postion = position;
    // this.item = item;
    // }
    //
    // @Override
    // public void onNumChange(View view, int num) {
    //
    // //
    // item.good_manager_modify_content_number_after_display.setVisibility(View.VISIBLE);
    // //
    // item.good_manager_modify_content_number_after_display.setText(String.format("您修改后的库存：%1$s",
    // // num+""));
    // //
    // item.good_manager_modify_content_number_after_display.setFocusable(false);
    // attr_dataMap.put(Postion, num);
    // }
    //
    // }

    class ContentItem {
        public TextView good_manager_modify_content_name,
                good_manager_modify_content_number_before,
                good_manager_modify_content_number_after_display;
        // public EditText good_manager_modify_content_number_after;
        public TextView good_manager_modify_content_number_after;

    }

}

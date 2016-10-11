package io.vtown.WeiTangApp.ui.ui;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcache.BSouRecord;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.LogUtils;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.comment.view.custom.CompleteListView;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog.onConfirmClick;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog.oncancelClick;
import io.vtown.WeiTangApp.db.DBHelper;
import io.vtown.WeiTangApp.db.dao.DaoSouRecord;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.ui.ATitileNoBase;
import io.vtown.WeiTangApp.ui.comment.ACommentList;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @author 商品搜索
 * @version 创建时间：2016-4-20 上午10:07:36
 */
public class ASouSouGood extends ATitileNoBase {

    /**
     * 搜索输入框
     */
    private EditText sousou_sou_ed;
    /**
     * 搜索按钮
     */
//	private ImageView sousou_sou_iv;

    /**
     * 取消按钮
     */
    private TextView sousou_cancle_txt;
    /**
     * 返回按钮
     */
    private ImageView sousou_back_iv;

    /**
     * 搜索的UP布局
     */
    private LinearLayout sousou_up_lay;
    /**
     * 外层的scrollview
     */
    private ScrollView sousou_scrollview;

    /**
     * 热门gridview
     */
    private CompleteGridView sousou_remen_grid;
    /**
     * 历史搜素记录
     */
    private CompleteListView sousou_history_ls;
    // 搜索历史的
    private HistoryAdapter historyAdapter;
    // 清除历史记录
    private TextView sousou_history_ls_clearn;
    // 历史纪录的文字
    private TextView sousou_history_txt;
    /**
     * 搜索的结果
     */
    private CompleteListView sousou_sousou_ls;
    /**
     * Gradview的AP
     */
    private MyGradAp myGradAp;
    /**
     * 搜搜的AP
     */
    private SouSouAdapter souSouAdapter;
    /**
     * FootView 跳转到搜索店铺的界面
     */
    private View FootShopView;
    /**
     * GOTO商铺搜索
     */
    private TextView GoShopSouTxt;

    /**
     * 热门搜索
     */
    private final int Http_Tage_Remen = 101;
    /**
     * 搜索结果
     */
    private final int Http_Tage_SouResult = 102;

    /**
     * 缓存数据的
     */
    private DaoSouRecord mDaoSouRecord;// BSouRecord缓存bean中 预留一个type字段
    /**
     * 获取到的缓存历史纪录
     */
    private List<BSouRecord> CachDatas = new ArrayList<BSouRecord>();
    /**
     * 搜索下边提示跳转到搜索店铺
     */
    private TextView goodsousou_toshop_sousou_txt;
    private RelativeLayout goodsousou_toshop_sousou_txt_lay;

//    private ImageView goodsousou_toshop_sousou_txt_downbg;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_sousou);
        mDaoSouRecord = DaoSouRecord.getInstance(BaseContext);
        IView();
        ICacherRecommend();
    }

    /**
     * 热门搜索的缓存
     */
    private void ICacherRecommend() {
        String RecommendStr = Spuit.SousouRecommend_Get(BaseContext);
        if (StrUtils.isEmpty(RecommendStr)) {//没有缓存
            IData(LOAD_INITIALIZE);
        } else {//存在缓存
            List<BLComment> data = JSON.parseArray(RecommendStr, BLComment.class);
            myGradAp.Refrsh(data);
            sousou_scrollview.setVisibility(View.VISIBLE);
            IData(LOAD_HindINIT);

        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        ICacheData();

    }

    // 初始进来需要进行本地缓存数据的获取 并且刷新历史列表s
    private void ICacheData() {

        CachDatas = mDaoSouRecord.findAll();// 每次进来就要获取全部数据并且重新展示
        // 注意 添加时候要判断下 id是不是存在
        historyAdapter.Refrsh(CachDatas);
    }

    // 添加bean
    private void AddCacheData(BSouRecord addbean) {
        List<BSouRecord> s = mDaoSouRecord.findAll();
        boolean IsAdd = true;// 是否需要添加
        for (int i = 0; i < s.size(); i++) {// 判断本地缓存是否已经
            if (s.get(i).getTitle().equals(addbean.getTitle()))
                IsAdd = false;
        }
        if (IsAdd)
            mDaoSouRecord.insert(addbean);

        // if (mDaoSouRecord.findAll().size() == 3) {
        // mDaoSouRecord.DeletItem("_id", 0 + "");
        // }
        List<BSouRecord> sddss = mDaoSouRecord.findAll();
        List<BSouRecord> newlist = new ArrayList<BSouRecord>();
        if (sddss.size() > Constants.SouSouHeistory) {// 超出规定条数
            for (int i = sddss.size() - 1; i >= sddss.size() -  Constants.SouSouHeistory; i--) {
                newlist.add(sddss.get(i));

            }

            mDaoSouRecord.clearn();
            for (int i = 0; i < newlist.size(); i++) {
                mDaoSouRecord.insert(newlist.get(i));
            }

        } else {// 未超出规定条数 但是需要改变顺序

            for (int i = sddss.size() - 1; i >= 0; i--) {
                newlist.add(sddss.get(i));

            }
            mDaoSouRecord.clearn();
            for (int i = 0; i < newlist.size(); i++) {
                mDaoSouRecord.insert(newlist.get(i));
            }
        }

    }

    // 删除bean
    private void DeletCacheData(BSouRecord addbean) {
        List<BSouRecord> s = mDaoSouRecord.findAll();
        boolean IsAdd = false;// 是否需要添加
        for (int i = 0; i < s.size(); i++) {// 判断本地缓存是否已经
            if (s.get(i).getTitle().equals(addbean.getTitle()))
                IsAdd = true;
        }
        if (IsAdd)
            mDaoSouRecord.DeletItem(DBHelper.SouRecordTab_Title,
                    addbean.getTitle());
    }

    private void IView() {
        goodsousou_toshop_sousou_txt_lay = (RelativeLayout) findViewById(R.id.goodsousou_toshop_sousou_txt_lay);
        goodsousou_toshop_sousou_txt = (TextView) findViewById(R.id.goodsousou_toshop_sousou_txt);
        // goodsousou_toshop_sousou_txt.setOnClickListener(this);
        goodsousou_toshop_sousou_txt_lay.setOnClickListener(this);

        // 清除历史
        sousou_history_txt = (TextView) findViewById(R.id.sousou_history_txt);
        sousou_history_ls_clearn = (TextView) findViewById(R.id.sousou_history_ls_clearn);
        sousou_history_ls_clearn.setOnClickListener(this);

        sousou_scrollview = (ScrollView) findViewById(R.id.sousou_scrollview);
        sousou_scrollview.setVisibility(View.GONE);
        sousou_up_lay = (LinearLayout) findViewById(R.id.sousou_up_lay);
        sousou_remen_grid = (CompleteGridView) findViewById(R.id.sousou_remen_grid);
        sousou_history_ls = (CompleteListView) findViewById(R.id.sousou_history_ls);
        sousou_sousou_ls = (CompleteListView) findViewById(R.id.sousou_sousou_ls);

        FootShopView = ViewHolder.ToView(BaseContext,
                R.layout.view_sousou_good_footview);
        GoShopSouTxt = (TextView) FootShopView
                .findViewById(R.id.view_sousou_good_footview_goshopsou);
//        goodsousou_toshop_sousou_txt_downbg = (ImageView) findViewById(R.id.goodsousou_toshop_sousou_txt_downbg);
        myGradAp = new MyGradAp(R.layout.item_sousou_remen);
        sousou_remen_grid.setAdapter(myGradAp);

        // 搜索结果列表的处理
        sousou_sousou_ls.addFooterView(FootShopView);
        souSouAdapter = new SouSouAdapter(BaseContext,
                R.layout.item_sousougood_result);
        sousou_sousou_ls.setAdapter(souSouAdapter);

        // 搜索历史的列表处理
        historyAdapter = new HistoryAdapter(BaseContext,
                R.layout.item_sousougood_result, 1);
        sousou_history_ls.setAdapter(historyAdapter);

        sousou_sousou_ls.setVisibility(View.GONE);
        sousou_sousou_ls.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg3 <= -1) {// 点击的是head和foot
                    // PromptManager.ShowCustomToast(BaseContext, "搜索店铺");

                } else {// 点击正常
                    // PromptManager.ShowCustomToast(BaseContext, "位置：" + arg2);
                    BLComment DATA = (BLComment) arg0.getItemAtPosition(arg2);
                    BComment data = new BComment(DATA.getId(), DATA.getTitle());

                    AddCacheData(new BSouRecord(DATA.getId(), DATA.getTitle(),
                            1 + ""));
                    PromptManager
                            .SkipActivity(
                                    BaseActivity,
                                    new Intent(BaseActivity, ACommentList.class)
                                            .putExtra(
                                                    ACommentList.Tage_ResultKey,
                                                    ACommentList.Tage_SouGoodResultItem)
                                            .putExtra(
                                                    ACommentList.Tage_BeanKey,
                                                    data));
                    BaseActivity.finish();
                }

            }
        });
        sousou_history_ls.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                BSouRecord DATA = (BSouRecord) arg0.getItemAtPosition(arg2);
                BComment data = new BComment(DATA.getId(), DATA.getTitle());
                PromptManager.SkipActivity(
                        BaseActivity,
                        new Intent(BaseActivity, ACommentList.class).putExtra(
                                ACommentList.Tage_ResultKey,
                                ACommentList.Tage_SouGoodResultItem).putExtra(
                                ACommentList.Tage_BeanKey, data));
                AddCacheData(DATA);
                // BaseActivity.finish();
            }
        });
        sousou_history_ls
                .setOnItemLongClickListener(new OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0,
                                                   View arg1, int arg2, long arg3) {
                        BSouRecord DATA = (BSouRecord) arg0
                                .getItemAtPosition(arg2);

                        HistryOnLongClick(DATA);
                        return true;
                    }
                });
        GoShopSouTxt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (StrUtils.IsOnlyNumber(sousou_sou_ed.getText().toString()
                        .trim())) {
                    PromptManager.SkipActivity(BaseActivity, new Intent(
                            BaseActivity, ASouSouShop.class).putExtra("id",
                            sousou_sou_ed.getText().toString().trim()));
                } else {
                    PromptManager.SkipActivity(BaseActivity, new Intent(
                            BaseActivity, ASouSouShop.class));
                }

            }
        });
    }

    private void HistryOnLongClick(final BSouRecord da) {
        ShowCustomDialog("是否删除?", "取消", "确定", new IDialogResult() {

            @Override
            public void RightResult() {
                DeletCacheData(da);
                ICacheData();
            }

            @Override
            public void LeftResult() {
            }
        });


    }

    @Override
    protected void InitTile() {
        sousou_back_iv = (ImageView) findViewById(R.id.sousou_back_iv);
        sousou_sou_ed = (EditText) findViewById(R.id.sousou_sou_ed);
//		sousou_sou_iv = (ImageView) findViewById(R.id.sousou_sou_iv);
        sousou_cancle_txt = (TextView) findViewById(R.id.sousou_cancle_txt);

//		sousou_sou_iv.setOnClickListener(this);
        sousou_cancle_txt.setOnClickListener(this);
        sousou_sou_ed.addTextChangedListener(EdListener);
    }

    private void IData(int LoadType) {
        // 热门商品的获取
        if (LOAD_INITIALIZE == LoadType)
            PromptManager.showtextLoading(BaseContext,
                    getResources().getString(R.string.loading));

        SetTitleHttpDataLisenter(this);
        JSONObject obj = new JSONObject();

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("size", Constants.SouGoodSize);
        FBGetHttpData(map, Constants.SouHotGood, Method.GET, Http_Tage_Remen,
                LOAD_INITIALIZE);
    }

    /**
     * 输入关键字时候的调用的数据请求方法
     */
    private void DataEding(String data) {
        // JSONObject obj = new JSONObject();

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("title", data);// keyword
        FBGetHttpData(map, Constants.SouGood, Method.GET, Http_Tage_SouResult,
                LOAD_LOADMOREING);
    }

    private TextWatcher EdListener = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
            if (!StrUtils.isEmpty(s.toString().trim())) {
                DataEding(s.toString());
                if (StrUtils.IsOnlyNumber(s.toString().trim())) {

                    // goodsousou_toshop_sousou_txt.setVisibility(View.VISIBLE);
                    goodsousou_toshop_sousou_txt_lay
                            .setVisibility(View.VISIBLE);
//                    goodsousou_toshop_sousou_txt_downbg
//                            .setVisibility(View.VISIBLE);
                    StrUtils.SetColorsTxt(BaseContext,
                            goodsousou_toshop_sousou_txt, R.color.red,
                            "去搜索店铺:", s.toString().trim());
                    // goodsousou_toshop_sousou_txt.setText("去搜索店铺:"
                    // + s.toString().trim());
                }
            } else {
                sousou_up_lay.setVisibility(View.VISIBLE);
                sousou_sousou_ls.setVisibility(View.GONE);

                goodsousou_toshop_sousou_txt_lay.setVisibility(View.GONE);
                // goodsousou_toshop_sousou_txt.setVisibility(View.GONE);
//                goodsousou_toshop_sousou_txt_downbg.setVisibility(View.GONE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }

    };

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
    protected void DataResult(int Code, String Msg, BComment Data) {
        // LogUtils.i(Data.getHttpResultStr());
        switch (Data.getHttpResultTage()) {
            case Http_Tage_Remen:// 热门搜索
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    return;
                }
                List<BLComment> data = JSON.parseArray(Data.getHttpResultStr(),
                        BLComment.class);
                myGradAp.Refrsh(data);
                Spuit.SousouRecommend_Save(BaseContext, Data.getHttpResultStr());
                sousou_scrollview.setVisibility(View.VISIBLE);
                break;
            case Http_Tage_SouResult://

                List<BLComment> dataa = StrUtils.isEmpty(Data.getHttpResultStr()) ? new ArrayList<BLComment>()
                        : JSON.parseArray(Data.getHttpResultStr(), BLComment.class);

                sousou_sousou_ls.setVisibility(dataa.size() == 0 ? View.GONE
                        : View.VISIBLE);
                souSouAdapter.Refrsh(dataa);
                break;
            default:
                break;
        }
    }

    @Override
    protected void DataError(String error, int LoadTyp) {
        sousou_up_lay.setVisibility(View.VISIBLE);
        sousou_sousou_ls.setVisibility(View.GONE);

    }

    /**
     * 搜索结果的列表AP
     */
    private class SouSouAdapter extends BaseAdapter {
        private Context mycContext;
        private LayoutInflater inflater;
        private int ResourceId;
        private List<BLComment> datas = new ArrayList<BLComment>();

        public SouSouAdapter(Context mycContext, int resourceId) {
            super();
            this.mycContext = mycContext;
            this.inflater = LayoutInflater.from(mycContext);
            this.ResourceId = resourceId;
            sousou_up_lay.setVisibility(View.VISIBLE);
            // ViewHolder.ShowHindView( true , FootShopView);
        }

        public void Refrsh(List<BLComment> da) {

            sousou_up_lay.setVisibility(da.size() == 0 ? View.VISIBLE
                    : View.GONE);
            // ViewHolder.ShowHindView(da.size() == 0, FootShopView);
            this.datas = da;
            if (da.size() > 0) {
                // goodsousou_toshop_sousou_txt.setVisibility(View.GONE);
                goodsousou_toshop_sousou_txt_lay.setVisibility(View.GONE);
//                goodsousou_toshop_sousou_txt_downbg.setVisibility(View.GONE);
                if (StrUtils.IsOnlyNumber(sousou_sou_ed.getText().toString()
                        .trim())) {
                    GoShopSouTxt.setText("搜索店铺:"
                            + sousou_sou_ed.getText().toString().trim());
                } else {
                    GoShopSouTxt.setText("搜索店铺 ");
                }

            }

            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            // ViewHolder.ShowHindView( datas.size() == 0 , FootShopView);
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
            MyItem myItem = null;
            if (convertView == null) {
                myItem = new MyItem();
                convertView = inflater.inflate(ResourceId, null);
                myItem.item_sousou_goodname = (TextView) convertView
                        .findViewById(R.id.item_sousou_goodname);

                convertView.setTag(myItem);
            } else {
                myItem = (MyItem) convertView.getTag();
            }
            BLComment data = datas.get(position);
            myItem.item_sousou_goodname.setText(data.getTitle());
            return convertView;
        }

        class MyItem {
            TextView item_sousou_goodname;
        }
    }

    /**
     * 搜索历史
     */
    /**
     * 搜索结果的列表AP
     */
    private class HistoryAdapter extends BaseAdapter {
        private Context mycContext;
        private LayoutInflater inflater;
        private int ResourceId;
        private List<BSouRecord> datas = new ArrayList<BSouRecord>();
        // 1表示历史；；；2表示搜索结果
        private int Type;

        public HistoryAdapter(Context mycContext, int resourceId, int myType) {
            super();
            this.mycContext = mycContext;
            this.inflater = LayoutInflater.from(mycContext);
            this.ResourceId = resourceId;
            sousou_up_lay.setVisibility(View.VISIBLE);
            this.Type = myType;
        }

        public void Refrsh(List<BSouRecord> da) {
            this.datas = da;
            notifyDataSetChanged();

            // if(Type==1)//表示的是历史记录
            sousou_history_ls_clearn.setVisibility(da.size() == 0 ? View.GONE
                    : View.VISIBLE);
            sousou_history_txt.setVisibility(da.size() == 0 ? View.GONE
                    : View.VISIBLE);
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
            MyItem myItem = null;
            if (convertView == null) {
                myItem = new MyItem();
                convertView = inflater.inflate(ResourceId, null);
                myItem.item_sousou_goodname = (TextView) convertView
                        .findViewById(R.id.item_sousou_goodname);

                convertView.setTag(myItem);
            } else {
                myItem = (MyItem) convertView.getTag();
            }
            BSouRecord data = datas.get(position);
            myItem.item_sousou_goodname.setText(data.getTitle());
            return convertView;
        }

        class MyItem {
            TextView item_sousou_goodname;
        }
    }

    /**
     * 热门搜索AP
     */
    private class MyGradAp extends BaseAdapter {

        private LayoutInflater inflater;
        private int ResourceId;
        private List<BLComment> datas = new ArrayList<BLComment>();

        public MyGradAp(int resourceId) {
            super();

            this.inflater = LayoutInflater.from(BaseContext);
            this.ResourceId = resourceId;
        }

        public void Refrsh(List<BLComment> da) {
            this.datas = da;
            notifyDataSetChanged();
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
            MyItem myItem = null;
            if (convertView == null) {
                myItem = new MyItem();
                convertView = inflater.inflate(ResourceId, null);
                myItem.item_sousou_remen_txt = (TextView) convertView
                        .findViewById(R.id.item_sousou_remen_txt);

                convertView.setTag(myItem);
            } else {
                myItem = (MyItem) convertView.getTag();
            }
            final BLComment DATA = datas.get(position);
            myItem.item_sousou_remen_txt.setText(DATA.getTitle());

            myItem.item_sousou_remen_txt
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            BComment data = new BComment(DATA.getId(), DATA
                                    .getTitle());
                            AddCacheData(new BSouRecord(data.getId(), data
                                    .getTitle(), 1 + ""));
                            PromptManager
                                    .SkipActivity(
                                            BaseActivity,
                                            new Intent(BaseActivity,
                                                    ACommentList.class)
                                                    .putExtra(
                                                            ACommentList.Tage_ResultKey,
                                                            ACommentList.Tage_SouGoodResultItem)
                                                    .putExtra(
                                                            ACommentList.Tage_BeanKey,
                                                            data));
                        }
                    });

            return convertView;
        }

        class MyItem {
            TextView item_sousou_remen_txt;
        }

    }

    @Override
    protected void MyClick(View V) {
        switch (V.getId()) {

            // case R.id.sousou_sou_iv:// 搜索按钮
            // if (!StrUtils.isEmpty(sousou_sou_ed.getText().toString().trim())) {
            // PromptManager.showtextLoading(BaseContext, getResources()
            // .getString(R.string.loading));
            // DataEding(sousou_sou_ed.getText().toString().trim());
            // }
            // break;
            case R.id.sousou_cancle_txt:// 取消txt===>确定搜索
                // 去搜索*********************************************************************************************************************************

                if (StrUtils.EditTextIsEmPty(sousou_sou_ed))
                    return;

                AddCacheData(new BSouRecord(GetRandomId(), sousou_sou_ed.getText()
                        .toString()));
                BComment data = new BComment("", sousou_sou_ed.getText().toString()
                        .trim());

                // AddCacheData(new BSouRecord("1", DATA.getTitle(),
                // 1 + ""));
                PromptManager.SkipActivity(
                        BaseActivity,
                        new Intent(BaseActivity, ACommentList.class).putExtra(
                                ACommentList.Tage_ResultKey,
                                ACommentList.Tage_SouGoodResultItem).putExtra(
                                ACommentList.Tage_BeanKey, data));

                break;
            case R.id.sousou_history_ls_clearn:// 清理全部历史记录
                ClearnCaCheHistory();
                break;
            case R.id.goodsousou_toshop_sousou_txt_lay:// 提示跳转到搜索店铺界面

                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        ASouSouShop.class).putExtra("id", sousou_sou_ed.getText()
                        .toString().trim()));
                break;
            default:
                break;
        }
    }

    private String GetRandomId() {
        Random dRandom = new Random();
        int ii = dRandom.nextInt(1000) + dRandom.nextInt(1000)
                + dRandom.nextInt(10000000);
        return ii + "";

    }

    private void ClearnCaCheHistory() {
        ShowCustomDialog("是否清除历史纪录?", "取消", "确定", new IDialogResult() {

            @Override
            public void RightResult() {
                IClreanAllCacheData();
            }

            @Override
            public void LeftResult() {
            }
        });


    }

    private void IClreanAllCacheData() {
        mDaoSouRecord.clearn();
        CachDatas = mDaoSouRecord.findAll();// 每次进来就要获取全部数据并且重新展示
        // 注意 添加时候要判断下 id是不是存在
        historyAdapter.Refrsh(CachDatas);
    }

    @Override
    protected void InItBundle(Bundle bundle) {

    }

    @Override
    protected void SaveBundle(Bundle bundle) {

    }

}

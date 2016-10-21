package io.vtown.WeiTangApp.ui.title.shop.addgood;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.BaseApplication;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.PicImageItem;
import io.vtown.WeiTangApp.bean.bcomment.easy.addgood.BCategory;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.net.qiniu.NUPLoadUtil;
import io.vtown.WeiTangApp.comment.net.qiniu.NUpLoadUtils;
import io.vtown.WeiTangApp.comment.util.DimensionPixelUtil;
import io.vtown.WeiTangApp.comment.util.NetUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.comment.view.custom.CompleteListView;
import io.vtown.WeiTangApp.comment.view.pop.PAddGoosSort;
import io.vtown.WeiTangApp.comment.view.pop.PAddSelect;
import io.vtown.WeiTangApp.comment.view.select_pic.PicSelActivity;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.AEditInfBack;
import io.vtown.WeiTangApp.ui.comment.ALoactePhotoPager;
import io.vtown.WeiTangApp.ui.comment.AVidemplay;
import io.vtown.WeiTangApp.ui.comment.recordervido.ARecoderVido;

/**
 * Created by datutu on 2016/9/9.
 */
public class ANewAddGood extends ATitleBase {
    private View AddBaseView;
    //
    private LinearLayout good_upload_vido_lay, good_upload_roll_lay, good_upload_desc_lay;


    private ImageView good_upload_vido_add_bt, good_upload_roll_add_bt, good_upload_desc_add_bt;

    private CompleteGridView good_upload_roll_gridview, good_upload_desc_gridview;

    private ImageView good_upload_vido_cover, good_upload_vido_play;
//两个gradview的ap

    private MyAddGridAdapter upgridAdapter, downgridAdapter;
    //下边的选择view
    //商品填写标题
    private View good_upload_good_title;
    private TextView good_upload_good_title_txt;//商品标题
    //商品选择类目
    private View good_upload_category;
    private TextView good_upload_category_txt;//商品类目
    /**
     * 当前的类目规格保存数据
     */
    private BCategory CurrentSortData = null;
    //商品的规格ls
    private CompleteListView good_upload_rule_ls;
    private AddGoodRuleAdapter CategoryAdapter;//商品规格适配器
    //点击添加规格
    private LinearLayout good_upload_add_rule_lay;
    //发货地
    private View good_upload_address;
    private TextView good_upload_address_txt;//发货地

    //运费
    private EditText good_upload_post_price;
    //商品提交按钮
    private TextView good_upload_commint;

    private BUser MyUser;
    private int PicWidth;

    //默认情况下是选择上传图片
    private boolean IsPice = true;

    //保存视频录制时候获取的视频的地址
    private String GoodVidoPath;
    //记录描述上传的数据
    private List<PicImageItem> DescUpsData = new ArrayList<PicImageItem>();
    //记录商品轮播图的上传数据
    private List<PicImageItem> RollUpsData = new ArrayList<PicImageItem>();
    //记录视频封面上传时候的Uel
    private String VidoCoverUrl;
    //记录视频上传获取的url
    private String VidoWebUrl;


    @Override

    protected void InItBaseView() {
        setContentView(R.layout.activity_newaddgood);
        AddBaseView = LayoutInflater.from(this).inflate(R.layout.activity_newaddgood, null);
        MyUser = Spuit.User_Get(this);
        EventBus.getDefault().register(this, "ReciverBroad", BMessage.class);
        IBase();

    }

    /**
     * 初始化
     */
    private void IBase() {
        IPiceVido();
        IDown();
    }

    private void IDown() {
        good_upload_commint = (TextView) findViewById(R.id.good_upload_commint);

        PicWidth = (screenWidth - DimensionPixelUtil.dip2px(BaseContext, 16)) / 4;
        //标题的view
        good_upload_good_title = findViewById(R.id.good_upload_good_title);
        good_upload_good_title.setOnClickListener(this);
        good_upload_good_title_txt = (TextView) good_upload_good_title.findViewById(R.id.comment_txtarrow_content);
        StrUtils.SetTxt((TextView) good_upload_good_title.findViewById(R.id.comment_txtarrow_title), "商品标题");

        //选择类目的view
        good_upload_category = findViewById(R.id.good_upload_category);
        good_upload_category.setOnClickListener(this);
        good_upload_category_txt = (TextView) good_upload_category.findViewById(R.id.comment_txtarrow_content);
        StrUtils.SetTxt((TextView) good_upload_category.findViewById(R.id.comment_txtarrow_title), "选择类目");
        //商品规格的ls
        good_upload_rule_ls = (CompleteListView) findViewById(R.id.good_upload_rule_ls);
        CategoryAdapter = new AddGoodRuleAdapter(R.layout.item_add_good_rule_lv);
        good_upload_rule_ls.setAdapter(CategoryAdapter);
        //点击添加商品规格
        good_upload_add_rule_lay = (LinearLayout) findViewById(R.id.good_upload_add_rule_lay);
        good_upload_add_rule_lay.setOnClickListener(this);
        //发货地view
        good_upload_address = findViewById(R.id.good_upload_address);
        good_upload_address.setOnClickListener(this);
        good_upload_address_txt = (TextView) good_upload_address.findViewById(R.id.comment_txtarrow_content);
        StrUtils.SetTxt((TextView) good_upload_address.findViewById(R.id.comment_txtarrow_title), "发货地址");
        ((ImageView) good_upload_address.findViewById(R.id.iv_comment_right_arrow)).setVisibility(View.GONE);
        //运费输入框
        good_upload_post_price = (EditText) findViewById(R.id.good_upload_post_price);
        good_upload_commint.setOnClickListener(this);

        //商品规格删除
        good_upload_rule_ls.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0,
                                           View arg1, int arg2, long arg3) {
                final int Postio = arg2;
                ShowCustomDialog("是否删除该规格", "取消", "删除", new IDialogResult() {
                    @Override
                    public void LeftResult() {

                    }

                    @Override
                    public void RightResult() {
                        CategoryAdapter.DeletItem(Postio);
                    }
                });
                return true;
            }
        });

    }

    //初始化视频和图片选择
    private void IPiceVido() {

        upgridAdapter = new MyAddGridAdapter(BaseContext, new ArrayList<PicImageItem>());
        downgridAdapter = new MyAddGridAdapter(BaseContext, new ArrayList<PicImageItem>());


        good_upload_vido_lay = (LinearLayout) findViewById(R.id.good_upload_vido_lay);
        good_upload_roll_lay = (LinearLayout) findViewById(R.id.good_upload_roll_lay);
        good_upload_desc_lay = (LinearLayout) findViewById(R.id.good_upload_desc_lay);

        //gradview
        good_upload_roll_gridview = (CompleteGridView) findViewById(R.id.good_upload_roll_gridview);
        good_upload_desc_gridview = (CompleteGridView) findViewById(R.id.good_upload_desc_gridview);

        good_upload_roll_gridview.setAdapter(upgridAdapter);
        good_upload_desc_gridview.setAdapter(downgridAdapter);
        //视频
        good_upload_vido_cover = (ImageView) findViewById(R.id.good_upload_vido_cover);
        good_upload_vido_play = (ImageView) findViewById(R.id.good_upload_vido_play);
        good_upload_vido_play.setOnClickListener(this);
        //按钮
        good_upload_vido_add_bt = (ImageView) findViewById(R.id.good_upload_vido_add_bt);
        good_upload_roll_add_bt = (ImageView) findViewById(R.id.good_upload_roll_add_bt);
        good_upload_desc_add_bt = (ImageView) findViewById(R.id.good_upload_desc_add_bt);

        good_upload_vido_add_bt.setOnClickListener(this);
        good_upload_roll_add_bt.setOnClickListener(this);
        good_upload_desc_add_bt.setOnClickListener(this);


        good_upload_roll_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(BaseContext,
                        ALoactePhotoPager.class);
                mIntent.putExtra("position", position);
                BaseApplication.GetInstance().setPicImages(upgridAdapter.GetDatas());
                PromptManager.SkipActivity(BaseActivity, mIntent);
            }
        });
        good_upload_desc_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(BaseContext,
                        ALoactePhotoPager.class);
                mIntent.putExtra("position", position);
                BaseApplication.GetInstance().setPicImages(downgridAdapter.GetDatas());
                PromptManager.SkipActivity(BaseActivity, mIntent);
            }
        });


    }

    @Override
    protected void InitTile() {
        SetRightText(IsPice ? getResources().getString(R.string.pic) : getResources().getString(R.string.vido));
        right_txt.setOnClickListener(this);
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        PromptManager.closeTextLoading3();
        PromptManager.ShowCustomToast(this, getResources().getString(R.string.addgoodover));
        BaseActivity.finish();


    }

    @Override
    protected void DataError(String error, int LoadType) {
        PromptManager.closeTextLoading3();
        PromptManager.ShowCustomToast(this, getResources().getString(R.string.addgooderr));
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

    private void PicVidoChange() {
        good_upload_vido_lay.setVisibility(IsPice ? View.GONE : View.VISIBLE);
        good_upload_roll_lay.setVisibility(!IsPice ? View.GONE : View.VISIBLE);

        if (!IsPice && !StrUtils.isEmpty(GoodVidoPath)) {//是视频而且视频不为空  //就显示视频的Cover
            good_upload_vido_cover.setVisibility(View.VISIBLE);
            good_upload_vido_play.setVisibility(View.VISIBLE);
        } else {//非视频或者视频但还没有拍摄视频
            good_upload_vido_cover.setVisibility(View.GONE);
            good_upload_vido_play.setVisibility(View.GONE);
        }
    }

    @Override
    protected void MyClick(View V) {
        switch (V.getId()) {
            case R.id.good_upload_commint://提交按钮
                CommintGoods();
                break;
            case R.id.right_txt://切换视频
                IsPice = !IsPice;
                SetRightText(IsPice ? getResources().getString(R.string.pic) : getResources().getString(R.string.vido));
                PicVidoChange();
                break;
            case R.id.good_upload_good_title://添加商品标题
                Intent mIntent = new Intent(BaseActivity, AEditInfBack.class)
                        .putExtra(AEditInfBack.Tag_key,
                                AEditInfBack.Tag_AddGoods_Title).putExtra("title",
                                StrUtils.TextStrGet(good_upload_good_title_txt));
                PromptManager.SkipActivity(BaseActivity, mIntent);
                break;
            case R.id.good_upload_category://添加商品类目
                if (CurrentSortData == null)
                    PromptManager.SkipActivity(BaseActivity, new Intent(
                            BaseActivity, AAddGoodCategory.class));
                else
                    ShowCustomDialog(getResources().getString(R.string.changesort),
                            getResources().getString(R.string.cancle),
                            getResources().getString(R.string.sure),
                            new IDialogResult() {
                                @Override
                                public void RightResult() {
                                    PromptManager.SkipActivity(BaseActivity, new Intent(
                                            BaseActivity, AAddGoodCategory.class));

                                }

                                @Override
                                public void LeftResult() {
                                }
                            });
                break;
            case R.id.good_upload_add_rule_lay://添加商品规格
                if (null == CurrentSortData) {// 表示还没有选择分类的数据
                    PromptManager.ShowMyToast(BaseContext, "请先选择类目");
                    return;
                }
                ShowSortPop(AddBaseView);

                break;
            case R.id.good_upload_address://添加商品地址
                hintKbTwo();
                Address();
                break;
            case R.id.good_upload_vido_play://视频播放
                if (StrUtils.isEmpty(GoodVidoPath)) {
                    PromptManager.ShowCustomToast(this, getResources().getString(R.string.picurlerror));
                    return;
                }
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        AVidemplay.class).putExtra(AVidemplay.VidoKey, GoodVidoPath)
                        .putExtra("issd", true));
                break;
            case R.id.good_upload_vido_add_bt://视频替换视频
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        ARecoderVido.class));
                break;
            case R.id.good_upload_roll_add_bt://轮播图添加图片
                toPicSelect(upgridAdapter.GetDatas(), PicSelActivity.Tage_Good_Pics);
                break;
            case R.id.good_upload_desc_add_bt://商品描述添加图片
                toPicSelect(downgridAdapter.GetDatas(), PicSelActivity.Tage_Good_Desc);
                break;


        }

    }


    // 填写规格的的POp输入框**************************************************
    private void ShowSortPop(View addbaseView2) {
        PAddGoosSort mAddGoosSort = new PAddGoosSort(BaseContext,
                CurrentSortData);
        mAddGoosSort.showAtLocation(addbaseView2, Gravity.TOP, 0, 20);
        mAddGoosSort.SetOnPopupListener(new PAddGoosSort.OnPopupListener() {

            @Override
            public void sendData(BCategory ResouceData) {
                CategoryAdapter.AddFrashData(ResouceData);

            }
        });
    }

    /**
     * 选择地区
     */
    private void Address() {
        final PAddSelect m = new PAddSelect(BaseContext, true);
        m.GetAddressResult(new PAddSelect.AddSelectInterface() {
            @Override
            public void GetAddResult(String ProviceName, String CityName,
                                     String DistrictName, String ZipCode) {
                StrUtils.SetTxt(good_upload_address_txt, ProviceName);
                m.dismiss();
            }
        });
        m.showAtLocation(AddBaseView, Gravity.BOTTOM, 0, 0);
    }

    public void ReciverBroad(BMessage message) {
        switch (message.getMessageType()) {

            case BMessage.Tage_AddGood_SelectCategory://添加商品类目
                if (CurrentSortData != null && !CurrentSortData.getId().equals(message.getAddGoods_SelectCatory().getId())) {//标识换分类了
                    CategoryAdapter.RemoveDatas();
                    CurrentSortData = null;
                    StrUtils.SetTxt(good_upload_category_txt, "");
                }

                CurrentSortData = message.getAddGoods_SelectCatory();
                StrUtils.SetTxt(good_upload_category_txt, CurrentSortData.getCate_name());
                good_upload_add_rule_lay.setVisibility(View.VISIBLE);
                break;
            case BMessage.Tage_AddGood_EditTitle://填写商品完毕
                good_upload_good_title_txt.setText(message.getAddGood_GoodTitle());
                break;
            case BMessage.Tage_Select_Pic_Good_Pics:
                List<String> imgs1 = message.getTmpArrayList();
                updataList(good_upload_roll_gridview, upgridAdapter, imgs1);
                break;
            case BMessage.Tage_Select_Pic_Good_Desc:
                List<String> imgs2 = message.getTmpArrayList();
                updataList(good_upload_desc_gridview, downgridAdapter, imgs2);
                break;
            case 290://标识已经录制完毕视频返回来
                GoodVidoPath = message.getReCordVidoPath();
                good_upload_vido_cover.setImageBitmap(createVideoThumbnail(GoodVidoPath));
                good_upload_vido_cover.setVisibility(View.VISIBLE);
                good_upload_vido_play.setVisibility(View.VISIBLE);
                break;
        }


    }

    /**
     * 从选择图片页面返回数据后需要 进行刷新adapter
     *
     * @param good_upload_roll_gridview
     * @param
     * @param imgs1
     */
    private void updataList(CompleteGridView good_upload_roll_gridview, MyAddGridAdapter mygridAdapter, List<String> imgs1) {
        {
            List<PicImageItem> newdata = new ArrayList<PicImageItem>();
            newdata = mygridAdapter.GetDatas();
            if (imgs1 != null && imgs1.size() > 0) {
                for (int i = 0; i < imgs1.size(); i++) {
                    PicImageItem item = new PicImageItem("", imgs1.get(i));
                    newdata.add(item);
                }
                mygridAdapter = null;
                mygridAdapter = new MyAddGridAdapter(BaseContext, newdata);
                good_upload_roll_gridview.setAdapter(mygridAdapter);
//            AP.update();
            }

        }

    }

    private void toPicSelect(List<PicImageItem> datas, int type) {
        if (9 - datas.size() > 0) {
            Intent intent = new Intent(BaseContext, PicSelActivity.class);
            intent.putExtra(PicSelActivity.Select_Img_Size_str, 9 - datas.size());
            intent.putExtra(PicSelActivity.Select_Img_Type, type);
            startActivity(intent);
        } else {
            PromptManager.ShowCustomToast(BaseContext, "亲，最多9张图片哦");
            return;
        }
    }

    //选择图片生成的图片适配器
    public class MyAddGridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<PicImageItem> mShow_datas = new ArrayList<PicImageItem>();
        boolean IsFristHaShow;

        public MyAddGridAdapter(Context context, List<PicImageItem> show_datas) {
            inflater = LayoutInflater.from(context);
            this.mShow_datas = show_datas;
        }

        public void SetData(List<PicImageItem> saa) {
            this.mShow_datas = saa;
        }

        public List<PicImageItem> GetDatas() {
            return mShow_datas;
        }

        public void update() {
            Log.i("s", "s");
            this.notifyDataSetChanged();
        }

        public int getCount() {
            return mShow_datas.size();
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }


        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {

                convertView = inflater.inflate(R.layout.item_good_manager_pics,
                        parent, false);
                GridView.LayoutParams params = new GridView.LayoutParams(PicWidth, PicWidth);
                convertView.setLayoutParams(params);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.good_manager_item_grida_image);
                holder.image_delete = (ImageView) convertView.findViewById(R.id.good_manager_item_gride_delete);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (!StrUtils.isEmpty(mShow_datas.get(position).getPathurl())) {
                holder.image.setImageBitmap(StrUtils.GetBitMapFromPath(mShow_datas.get(position).getPathurl()));
            } else {
                String path = mShow_datas.get(position).getWeburl();
                ImageLoaderUtil.Load2(path, holder.image, R.drawable.error_iv2);
            }
            final int MyPostion = position;
            holder.image_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mShow_datas.remove(MyPostion);
                    update();
                }
            });
            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
            public ImageView image_delete;
        }

    }

    //生成商品规格的适配器
    class AddGoodRuleAdapter extends BaseAdapter {

        private int ResourseId;
        private LayoutInflater inflater;
        private List<BCategory> datas = new ArrayList<BCategory>();

        public AddGoodRuleAdapter(int ResourseId) {
            super();
            this.ResourseId = ResourseId;
            this.inflater = LayoutInflater.from(BaseContext);

        }

        /**
         * 加载更多
         */
        public void AddFrashData(BCategory dass) {

            this.datas.add(dass);

            this.notifyDataSetChanged();
        }

        /**
         * 获取类别的数据源
         */
        public List<BCategory> GetShortDatas() {
            return datas;
        }

        // 长按删除***********************************************
        public void DeletItem(int Postion) {
            datas.remove(Postion);
            this.notifyDataSetChanged();
        }

        /**
         * 当换类目时候需要清除之前老的数据
         */
        public void RemoveDatas() {
            datas = new ArrayList<BCategory>();
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return datas.size();
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
            AddGoodItem addGoodRule = null;
            if (arg1 == null) {
                addGoodRule = new AddGoodItem();
                arg1 = inflater.inflate(ResourseId, null);
                addGoodRule.tv_add_good_value1 = ViewHolder.get(arg1,
                        R.id.tv_add_good_value1);
                addGoodRule.tv_add_good_value2 = ViewHolder.get(arg1,
                        R.id.tv_add_good_value2);
                addGoodRule.tv_add_good_value3 = ViewHolder.get(arg1,
                        R.id.tv_add_good_value3);
                addGoodRule.tv_add_good_value4 = ViewHolder.get(arg1,
                        R.id.tv_add_good_value4);

                addGoodRule.tv_add_good_rule_lable1 = ViewHolder.get(arg1,
                        R.id.tv_add_good_rule_lable1);

                addGoodRule.tv_add_good_rule_lable2 = ViewHolder.get(arg1,
                        R.id.tv_add_good_rule_lable2);

                arg1.setTag(addGoodRule);
            } else {
                addGoodRule = (AddGoodItem) arg1.getTag();
            }

            StrUtils.SetTxt(addGoodRule.tv_add_good_rule_lable1, datas
                    .get(arg0).getAdd_good_attrs_name_1());
            StrUtils.SetTxt(addGoodRule.tv_add_good_rule_lable2, datas
                    .get(arg0).getAdd_good_attrs_name_2());

            StrUtils.SetTxt(addGoodRule.tv_add_good_value1, datas.get(arg0)
                    .getAdd_good_attrs_value_1());
            StrUtils.SetTxt(addGoodRule.tv_add_good_value2, datas.get(arg0)
                    .getAdd_good_attrs_value_2());
            StrUtils.SetTxt(
                    addGoodRule.tv_add_good_value3,
                    (StrUtils.toFloat(datas.get(arg0)
                            .getAdd_good_attrs_value_3()) / 100) + "元");
            StrUtils.SetTxt(addGoodRule.tv_add_good_value4, datas.get(arg0)
                    .getAdd_good_attrs_value_4());

            return arg1;
        }

        public class AddGoodItem {

            TextView tv_add_good_rule_lable1, tv_add_good_rule_lable2,
                    tv_add_good_value1, tv_add_good_value2, tv_add_good_value3,
                    tv_add_good_value4;
        }
    }

    //开始上传*********************************************************************************
//提交按钮
    private void CommintGoods() {
        if (StrUtils.IsTextViewEmpty(good_upload_good_title_txt)) {
            PromptManager.ShowCustomToast(this, getResources().getString(R.string.plese_add_title));
            return;
        }
        if (CategoryAdapter.getCount() == 0) {
            PromptManager.ShowCustomToast(this, getResources().getString(R.string.plese_add_category));
            return;
        }
        if (StrUtils.IsTextViewEmpty(good_upload_address_txt)) {
            PromptManager.ShowCustomToast(this, getResources().getString(R.string.plese_add_add
            ));
            return;
        }
        if (StrUtils.EditTextIsEmPty(good_upload_post_price)) {
            PromptManager.ShowCustomToast(this, getResources().getString(R.string.plese_add_postmony
            ));
            return;
        }
        if (downgridAdapter.getCount() == 0) {
            PromptManager.ShowCustomToast(this, getResources().getString(R.string.plese_add_desc
            ));
            return;
        }

        if (IsPice && upgridAdapter.getCount() == 0) {
            PromptManager.ShowCustomToast(this, getResources().getString(R.string.plese_add_goodpic
            ));
            return;
        }
        if (!IsPice && StrUtils.isEmpty(GoodVidoPath)) {
            PromptManager.ShowCustomToast(this, getResources().getString(R.string.plese_add_goodvido
            ));
            return;
        }
        if (CheckNet(BaseContext))
            return;
        PromptManager.showtextLoading3(this, getResources().getString(R.string.addgooding));
        //开始上传图片
        BeginUpDatas();


    }

    private int DescPicNeedUpNumber;
    private int DescPicCountNumber;

    //上传==>先上传商品描述==》如果图片上传轮播图&&如果视频上传视频
    private void BeginUpDatas() {
        if (!NetUtil.isConnected(BaseContext)) {//检查网络
            PromptManager.ShowCustomToast(BaseContext, getResources().getString(R.string.network_not_connected));
            return;
        }
        DescPicNeedUpNumber = 0;
        DescPicCountNumber = 0;

        DescUpsData = downgridAdapter.GetDatas();

        for (int i = 0; i < DescUpsData.size(); i++) {
            if (StrUtils.isEmpty(DescUpsData.get(i).getWeburl()))
                DescPicNeedUpNumber = DescPicNeedUpNumber + 1;
        }

        if (DescPicNeedUpNumber == 0) {
            //商品描述不需要本地图片上传
            BeginUpRollVidoData();
            return;
        }
        for (int i = 0; i < DescUpsData.size(); i++) {
            final int Postion = i;
            if (!StrUtils.isEmpty(DescUpsData.get(Postion).getWeburl())) {
                continue;
            }
            NUPLoadUtil dLoadUtils = new NUPLoadUtil(BaseContext, new File(
                    DescUpsData.get(Postion).getPathurl()), StrUtils.UploadQNName("photo"));

            dLoadUtils.SetUpResult1(new NUPLoadUtil.UpResult1() {

                @Override
                public void Progress(String arg0, double arg1) {

                }

                @Override
                public void Onerror() {
                    DescUpsData.get(Postion).setWeburl("");
                    DescPicCountNumber = DescPicCountNumber + 1;

                    if (DescPicNeedUpNumber == DescPicCountNumber) {
                        // 上传描述完毕
                        BeginUpRollVidoData();
                    }
                }

                @Override
                public void Complete(String HostUrl, String Url) {
                    DescUpsData.get(Postion).setWeburl(HostUrl);
                    DescPicCountNumber = DescPicCountNumber + 1;
                    if (DescPicCountNumber == DescPicNeedUpNumber) {
                        // 上传描述完毕
                        BeginUpRollVidoData();
                    }
                }
            });
            dLoadUtils.UpLoad();
        }


    }

    private int RollGoodNeedNumber;
    private int RollGoodCountNumber;


    //开始上传视频或者轮播图
    private void BeginUpRollVidoData() {
        if (IsPice) {//轮播图*************

            RollGoodNeedNumber = 0;
            RollGoodCountNumber = 0;
            RollUpsData = upgridAdapter.GetDatas();


            for (int i = 0; i < RollUpsData.size(); i++) {
                if (StrUtils.isEmpty(RollUpsData.get(i).getWeburl()))
                    RollGoodNeedNumber = RollGoodNeedNumber + 1;
            }
            if (RollGoodNeedNumber == 0) {
                //商品描述不需要本地图片上传
                CommintData();
                return;
            }

            for (int i = 0; i < RollUpsData.size(); i++) {
                final int Postion = i;
                if (!StrUtils.isEmpty(RollUpsData.get(Postion).getWeburl())) {
                    continue;
                }
                NUPLoadUtil dLoadUtils = new NUPLoadUtil(BaseContext, new File(
                        RollUpsData.get(Postion).getPathurl()), StrUtils.UploadQNName("photo"));

                dLoadUtils.SetUpResult1(new NUPLoadUtil.UpResult1() {

                    @Override
                    public void Progress(String arg0, double arg1) {

                    }

                    @Override
                    public void Onerror() {
                        RollUpsData.get(Postion).setWeburl("");
                        RollGoodCountNumber = RollGoodCountNumber + 1;

                        if (RollGoodNeedNumber == RollGoodCountNumber) {
                            // 上传描述完毕
                            CommintData();
                        }
                    }

                    @Override
                    public void Complete(String HostUrl, String Url) {
                        RollUpsData.get(Postion).setWeburl(HostUrl);
                        RollGoodCountNumber = RollGoodCountNumber + 1;
                        if (RollGoodNeedNumber == RollGoodCountNumber) {
                            // 上传描述完毕
                            CommintData();
                        }
                    }
                });
                dLoadUtils.UpLoad();
            }


        } else {//视频****先上传封面Cover*****************************

            NUpLoadUtils dLoadUtils = new NUpLoadUtils(BaseContext,
                    Bitmap2Bytes(createVideoThumbnail(GoodVidoPath)),
                    StrUtils.UploadQNName("photo"));
            dLoadUtils.SetUpResult(new NUpLoadUtils.UpResult() {
                @Override
                public void Progress(String arg0, double arg1) {
                }

                @Override
                public void Onerror() {
                    VidoCoverUrl = "";
                    BeginUpVido();
                }

                @Override
                public void Complete(String HostUrl, String Url) {
                    VidoCoverUrl = HostUrl;
                    BeginUpVido();
                }
            });
            dLoadUtils.UpLoad();


        }
    }

    //如果是视频 上传完毕视频Covew后需要上传mp4格式的视频
    private void BeginUpVido() {

        NUPLoadUtil dLoadUtils = new NUPLoadUtil(BaseContext, new File(
                GoodVidoPath), StrUtils.UploadVido("vid"));
        dLoadUtils.SetUpResult1(new NUPLoadUtil.UpResult1() {
            @Override
            public void Progress(String arg0, double arg1) {

            }

            @Override
            public void Onerror() {
                VidoWebUrl = "";
                // 上传视频完毕
                CommintData();
            }

            @Override
            public void Complete(String HostUrl, String Url) {
                VidoWebUrl = HostUrl;
                // 上传视频完毕
                CommintData();
            }
        });
        dLoadUtils.UpLoad();
    }

    /**
     * 开始与服务器交互进行上传
     */
    private void CommintData() {
        SetTitleHttpDataLisenter(this);
        FBGetHttpData(GetUpData(), Constants.Shop_AddGoods, Request.Method.POST, 1,
                LOAD_INITIALIZE);
    }

    private HashMap<String, String> GetUpData() {
        HashMap<String, String> mHashMap = new HashMap<String, String>();
        mHashMap.put("title", StrUtils.TextStrGet(good_upload_good_title_txt));// 标题
        mHashMap.put("category_id", CurrentSortData.getId());// 分类=》外层的id******************
        mHashMap.put("postage", String.valueOf(Integer.valueOf(StrUtils
                .EdStrGet(good_upload_post_price)) * 100));// 邮费******************

        if (IsPice) {// 传递图片
            mHashMap.put("cover", RollUpsData.get(0).getWeburl());// 视频或者图片地址
        } else {// 传递视频
            mHashMap.put("cover", VidoCoverUrl);// HavaUpVidoCover===&&&==HavaUpVido
        }
        mHashMap.put("intro", StrUtils.PicLsToStr(DescUpsData));// 商品简介
        mHashMap.put("ratio", GetRate(DescUpsData.size()));

        mHashMap.put("deliver", StrUtils.TextStrGet(good_upload_address_txt));// 发货地
        if (IsPice) {// 传递的是图片
            mHashMap.put("roll", StrUtils.PicLsToStr(RollUpsData));// 轮播图******************
            mHashMap.put("vid", "");// 小视频******************
        } else {// 传递的是视频
            mHashMap.put("roll", "");// 轮播图******************
            mHashMap.put("vid", VidoWebUrl);// 小视频******************
        }
        String SortStr = StrUtils.BeansToJson(
                CategoryAdapter.GetShortDatas()).toString();
        mHashMap.put("attrs", SortStr);// 商品规格信息******************
        mHashMap.put("seller_id", MyUser.getSeller_id());// 代理商id******************

        mHashMap.put("rtype", IsPice ? "0" : "1");// 轮播图内容类型 0-图片
        // 1-小视频******************
        mHashMap.put("itype", "0");// 内容类型 0-图片 1-文字******************
        mHashMap.put("sell_price", StrUtils.AddGoodPriceGet(CategoryAdapter.GetShortDatas(), 1));// 售价**************
        mHashMap.put("max_price", StrUtils.AddGoodPriceGet(CategoryAdapter.GetShortDatas(), 2));// 最大价格******************
        return mHashMap;
    }


    //上传*********************************************************************************************************************************

    @Override
    protected void onDestroy() {

        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
        }
        super.onDestroy();
    }

    private String GetRate(int MysIZE) {
        ArrayList<Float> goodsDescriptFloats = new ArrayList<Float>();
        for (int i = 0; i < MysIZE; i++) {
            goodsDescriptFloats.add(0.5f);
        }

        return StrUtils.GetStrs(goodsDescriptFloats);
    }

    @Override

    protected void InItBundle(Bundle bundle) {

    }

    @Override
    protected void SaveBundle(Bundle bundle) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && IsEdit()) {

            ShowCustomDialog(getResources().getString(R.string.add_good_exit_note), getResources().getString(R.string.cancle), getResources().getString(R.string.queding), new IDialogResult() {
                @Override
                public void LeftResult() {

                }

                @Override
                public void RightResult() {
                    BaseActivity.finish();
                }
            });

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private boolean IsEdit() {
        if (!StrUtils.IsTextViewEmpty(good_upload_good_title_txt)) {
//            PromptManager.ShowCustomToast(this, getResources().getString(R.string.add_good_exit_note));
            return true;
        }
        if (CategoryAdapter.getCount() > 0) {
//            PromptManager.ShowCustomToast(this, getResources().getString(R.string.add_good_exit_note));
            return true;
        }
        if (!StrUtils.IsTextViewEmpty(good_upload_address_txt)) {
//            PromptManager.ShowCustomToast(this, getResources().getString(R.string.add_good_exit_note
//            ));
            return true;
        }
        if (!StrUtils.EditTextIsEmPty(good_upload_post_price)) {
//            PromptManager.ShowCustomToast(this, getResources().getString(R.string.add_good_exit_note
//            ));
            return true;
        }
        if (downgridAdapter.getCount() > 0) {
//            PromptManager.ShowCustomToast(this, getResources().getString(R.string.add_good_exit_note
//            ));
            return true;
        }

        if (IsPice && upgridAdapter.getCount() > 0) {
//            PromptManager.ShowCustomToast(this, getResources().getString(R.string.add_good_exit_note
//            ));
            return true;
        }
        if (!IsPice && !StrUtils.isEmpty(GoodVidoPath)) {
//            PromptManager.ShowCustomToast(this, getResources().getString(R.string.add_good_exit_note
//            ));
            return true;
        }
        return false;
    }
}

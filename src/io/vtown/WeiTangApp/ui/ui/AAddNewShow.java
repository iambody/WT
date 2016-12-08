package io.vtown.WeiTangApp.ui.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.BaseApplication;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.PicImageItem;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.bean.bcomment.three_one.search.BLSearchShopAndGood;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.LogUtils;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.net.qiniu.NUPLoadUtil;
import io.vtown.WeiTangApp.comment.net.qiniu.NUpLoadUtils;
import io.vtown.WeiTangApp.comment.util.DimensionPixelUtil;
import io.vtown.WeiTangApp.comment.util.NetUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.comment.view.custom.switchButtonView.EaseSwitchButton;
import io.vtown.WeiTangApp.comment.view.select_pic.PicSelActivity;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.ALoactePhotoPager;
import io.vtown.WeiTangApp.ui.comment.AVidemplay;
import io.vtown.WeiTangApp.ui.comment.recordervido.ARecoderVido;

/**
 * Created by Yihuihua on 2016/12/5.
 */

public class AAddNewShow extends ATitleBase implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.tv_add_new_show_pic)
    TextView tvAddNewShowPic;
    @BindView(R.id.tv_add_new_show_vedio)
    TextView tvAddNewShowVedio;
    @BindView(R.id.iv_add_new_show_vedio_bg)
    ImageView ivAddNewShowVedioBg;
    @BindView(R.id.iv_add_new_show_vedio_control_icon)
    ImageView ivAddNewShowVedioControlIcon;
    @BindView(R.id.iv_add_new_show_goodinfo_arraw)
    ImageView ivAddNewShowGoodInfoArraw;
    @BindView(R.id.rl_add_new_show_vedio_layout)
    RelativeLayout rlAddNewShowVedioLayout;
    @BindView(R.id.gv_add_new_show_pics)
    CompleteGridView gvAddNewShowPics;
    @BindView(R.id.et_add_new_show_txt_content)
    EditText etAddNewShowTxtContent;
    @BindView(R.id.sb_add_new_show_select_good)
    EaseSwitchButton sbAddNewShowSelectGood;
    @BindView(R.id.rl_add_new_show_add_good)
    RelativeLayout rlAddNewShowAddGood;
    @BindView(R.id.iv_add_new_show_good_icon)
    ImageView ivAddNewShowGoodIcon;
    @BindView(R.id.iv_add_new_show_good_name)
    TextView ivAddNewShowGoodName;
    @BindView(R.id.tv_add_new_show_good_sales)
    TextView tvAddNewShowGoodSales;
    @BindView(R.id.tv_add_new_show_good_score)
    TextView tvAddNewShowGoodScore;
    @BindView(R.id.tv_add_new_show_good_price)
    TextView tvAddNewShowGoodPrice;
    @BindView(R.id.tv_add_new_show_good_origprice)
    TextView tvAddNewShowGoodOrigprice;
    @BindView(R.id.ll_add_new_show_pic_vedio_layout)
    LinearLayout llAddNewShowPicVedioLayout;
    @BindView(R.id.ll_add_new_show_good)
    LinearLayout llAddNewShowGood;
    @BindView(R.id.fl_add_new_show_good)
    FrameLayout flAddNewShowGood;
    @BindView(R.id.fl_add_new_show_pic_vedio_layout)
    FrameLayout flAddNewShowPicVedioLayout;
    @BindView(R.id.tv_add_new_show_good_share)
    TextView tvAddNewShowGoodShare;
    private Unbinder mBinder;
    private static final int TYPE_PIC = 0;
    private static final int TYPE_VEDIO = 1;
    private int current_type = TYPE_PIC;
    List<PicImageItem> datas = new ArrayList<PicImageItem>();
    private int width = 0;
    private MyGridAdapter myGridAdapter;
    private String mCordVidoPath;
    private List<String> imgs = new ArrayList<String>();
    private List<String> upload_sucess_pics = new ArrayList<String>();
    public static final String KEY_CREATE_SHOW_TYPE = "key_create_show_type";//创建Show类型
    public static final String KEY_CREATE_SHOW_GOODINFO = "key_create_show_goodinfo";//从商品详情过来的商品数据
    public static final int CREATE_TYPE_GOODDETAIL_PIC = 888;//商品详情--图片
    public static final int CREATE_TYPE_GOODDETAIL_VEDIO = 777;//商品详情--视频
    public static final int CREATE_TYPE_SHOW = 999;//正常类型（在Show列表发Show）
    private int Create_Type;
    private BLSearchShopAndGood mGoodInfo;
    private String upload_qiniu_video_url = "";
    private String upload_qiniu_video_cover_url = "";

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_add_new_show);
        EventBus.getDefault().register(this, "getEventMsg", BMessage.class);
        mBinder = ButterKnife.bind(this);
        IBundle();
        IView();
        IGrid();
    }

    private void IBundle() {
        Create_Type = getIntent().getIntExtra(KEY_CREATE_SHOW_TYPE, CREATE_TYPE_SHOW);
        mGoodInfo = (BLSearchShopAndGood) getIntent().getSerializableExtra(KEY_CREATE_SHOW_GOODINFO);
    }

    private void IView() {
        if (Create_Type != CREATE_TYPE_SHOW) {
            sbAddNewShowSelectGood.setVisibility(View.GONE);
            ivAddNewShowGoodInfoArraw.setVisibility(View.GONE);
            llAddNewShowGood.setEnabled(false);
        }
        if (mGoodInfo != null) {
            setGoodInfo();
        }
        sbAddNewShowSelectGood.setChecked(true);
        sbAddNewShowSelectGood.setOnCheckedChangeListener(this);
    }

    @Override
    protected void InitTile() {
        SetTitleTxt("发布Show");
        SetRightText("添加");
    }

    private void submitShow(String intro) {
        SetTitleHttpDataLisenter(this);
        PromptManager.closeTextLoading3();
        PromptManager.showLoading(BaseContext);
        HashMap<String, String> map = new HashMap<>();
        if (mGoodInfo != null && sbAddNewShowSelectGood.isChecked()) {
            map.put("goods_id", mGoodInfo.getId());//id就是good_id
            map.put("is_add_url", "1");//是否只分享商品链接 0-否 1-允许
        } else {
            map.put("is_add_url", "0");//是否只分享商品链接 0-否 1-允许
        }

        map.put("seller_id", Spuit.User_Get(BaseContext).getSeller_id());
        if (current_type == TYPE_VEDIO && !StrUtils.isEmpty(upload_qiniu_video_url)) {
            map.put("vid", upload_qiniu_video_url);//视频地址
            map.put("pre_url", upload_qiniu_video_cover_url);//缩略图地址
        }
        getUploadSuccessPics();
        if (current_type == TYPE_PIC && upload_sucess_pics.size() > 0) {
            map.put("pre_url", upload_sucess_pics.get(0));//缩略图地址
            for (int i = 0; i < upload_sucess_pics.size() ; i++) {
                map.put("cid" + i +1, upload_sucess_pics.get(i));
            }
        }

        map.put("intro", intro);//文字简介
        map.put("is_type", current_type + "");//图片还是视频0图片1视频

        if (datas.size() == 1) {
            map.put("ratio", "1");//图片比例，一张图片时，必传
        }

        FBGetHttpData(map, Constants.Create_Show, Request.Method.POST, 0, LOAD_INITIALIZE);

    }

    //获取所以上传成功的图片链接
    private void getUploadSuccessPics() {
        for (int i = 0; i < datas.size(); i++) {
            String weburl = datas.get(i).getWeburl();
            if (!StrUtils.isEmpty(weburl)) {
                upload_sucess_pics.add(weburl);
            }
        }
    }


    private void IGrid() {
        screenWidth = screenWidth - DimensionPixelUtil.dip2px(BaseContext, 16);
        width = screenWidth / 3;
        myGridAdapter = new MyGridAdapter(BaseContext);

        gvAddNewShowPics.setAdapter(myGridAdapter);
        gvAddNewShowPics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent mIntent = new Intent(BaseContext,
                        ALoactePhotoPager.class);
                mIntent.putExtra("position", arg2);
                BaseApplication.GetInstance().setPicImages(datas);
                PromptManager.SkipActivity(BaseActivity, mIntent);
            }
        });
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        AAddNewShow.this.finish();
    }

    @Override
    protected void DataError(String error, int LoadType) {
        PromptManager.ShowCustomToast(BaseContext,error);
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
    protected void InItBundle(Bundle bundle) {

    }

    @Override
    protected void SaveBundle(Bundle bundle) {

    }

    private void ControlClick(int ClickId) {
        tvAddNewShowPic
                .setBackground(R.id.tv_add_new_show_pic == ClickId ? getResources()
                        .getDrawable(R.drawable.shape_left_pre)
                        : getResources().getDrawable(R.drawable.shape_left_nor));
        tvAddNewShowPic
                .setTextColor(R.id.tv_add_new_show_pic == ClickId ? getResources()
                        .getColor(R.color.TextColorWhite) : getResources()
                        .getColor(R.color.app_fen));

        tvAddNewShowVedio
                .setBackground(R.id.tv_add_new_show_vedio == ClickId ? getResources()
                        .getDrawable(R.drawable.shape_right_pre)
                        : getResources()
                        .getDrawable(R.drawable.shape_right_nor));
        tvAddNewShowVedio
                .setTextColor(R.id.tv_add_new_show_vedio == ClickId ? getResources()
                        .getColor(R.color.TextColorWhite) : getResources()
                        .getColor(R.color.app_fen));

    }

    @OnClick({R.id.right_txt, R.id.tv_add_new_show_pic, R.id.tv_add_new_show_vedio, R.id.iv_add_new_show_vedio_control_icon, R.id.rl_add_new_show_add_good, R.id.ll_add_new_show_good, R.id.tv_add_new_show_good_share})
    protected void MyClick(View V) {
        switch (V.getId()) {
            case R.id.tv_add_new_show_pic:
                current_type = TYPE_PIC;
                ControlClick(R.id.tv_add_new_show_pic);
                rlAddNewShowVedioLayout.setVisibility(View.GONE);
                if (imgs != null && imgs.size() > 0) {
                    gvAddNewShowPics.setVisibility(View.VISIBLE);
                }


                break;
            case R.id.tv_add_new_show_vedio:
                current_type = TYPE_VEDIO;
                ControlClick(R.id.tv_add_new_show_vedio);
                gvAddNewShowPics.setVisibility(View.GONE);
                if (!StrUtils.isEmpty(mCordVidoPath)) {
                    SetRightText("重录");
                    rlAddNewShowVedioLayout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.iv_add_new_show_vedio_control_icon://播放视频
                if (StrUtils.isEmpty(mCordVidoPath)) {
                    PromptManager.ShowCustomToast(BaseContext, getResources().getString(R.string.picurlerror));
                    return;
                }
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        AVidemplay.class).putExtra(AVidemplay.VidoKey, mCordVidoPath)
                        .putExtra("issd", true));
                break;
            case R.id.rl_add_new_show_add_good://增加商品
            case R.id.ll_add_new_show_good:
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext, ASouSouGood.class).putExtra(ASouSouGood.From_Add_Show, true));
                break;
            case R.id.tv_add_new_show_good_share://发布Show按钮
                createShow();
                break;
            case R.id.right_txt:
                if (TYPE_PIC == current_type) {
                    toPicSelect(PicSelActivity.Tage_Add_New_Show);
                } else {
                    PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                            ARecoderVido.class));
                }
                break;
        }
    }

    /*
    * 发布Show
    * */
    private void createShow() {

        if (CheckNet(BaseContext))
            return;
        String content = etAddNewShowTxtContent.getText().toString().trim();
        if (StrUtils.isEmpty(content)) {
            PromptManager.ShowCustomToast(BaseContext, "请输入您要分享的内容");
            return;
        }
        if(current_type == TYPE_PIC && imgs.size() == 0){
            PromptManager.ShowCustomToast(BaseContext, "请添加您要分享的图片");
            return;
        }

        if(current_type ==TYPE_VEDIO && StrUtils.isEmpty(mCordVidoPath)){
            PromptManager.ShowCustomToast(BaseContext, "请添加您要分享的视频");
            return;
        }

        if (mGoodInfo == null && sbAddNewShowSelectGood.isChecked()) {
            PromptManager.ShowCustomToast(BaseContext, "点击“+”选择您要分享的商品");
            return;
        }
        PromptManager.showtextLoading3(this, getResources().getString(R.string.addgooding));

        if (current_type == TYPE_PIC) {
            uploadPics(content);
        } else {
            uploadVideo(content);
        }

    }

    private void toPicSelect(int type) {
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            flAddNewShowGood.setVisibility(View.VISIBLE);
        } else {
            flAddNewShowGood.setVisibility(View.GONE);
        }
    }

    public void getEventMsg(BMessage event) {
        int messageType = event.getMessageType();
        switch (messageType) {
            case BMessage.From_Search_Lv_Finish:
                mGoodInfo = event.getmSearchGood();
                setGoodInfo();
                break;

            case BMessage.Tage_Select_Pic_Add_Show://选择图片返回的数据
                imgs = event.getTmpArrayList();
                setGridViewPic();
                break;

            case 290://视频
                mCordVidoPath = event.getReCordVidoPath();
                if (!StrUtils.isEmpty(mCordVidoPath)) {
                    rlAddNewShowVedioLayout.setVisibility(View.VISIBLE);
                    ivAddNewShowVedioBg.setImageBitmap(createVideoThumbnail(mCordVidoPath));
                }
                break;
        }
    }

    /*
    * 设置GridView中的图片
    * */
    private void setGridViewPic() {
        if (imgs != null && imgs.size() > 0) {
            gvAddNewShowPics.setVisibility(View.VISIBLE);
            for (String path : imgs) {
                PicImageItem item = new PicImageItem("", path);
                datas.add(item);
            }
            myGridAdapter.update();
        }
    }

    private void setGoodInfo() {
        if (mGoodInfo != null) {
            rlAddNewShowAddGood.setVisibility(View.GONE);
            llAddNewShowGood.setVisibility(View.VISIBLE);
            ImageLoaderUtil.Load2(mGoodInfo.getAvatar(), ivAddNewShowGoodIcon, R.drawable.error_iv2);
            StrUtils.SetTxt(ivAddNewShowGoodName, mGoodInfo.getTitle());
            StrUtils.SetMoneyFormat(BaseContext, tvAddNewShowGoodPrice, mGoodInfo.getSell_price(), 15);
            if ("0".equals(mGoodInfo.getOrig_price()) || StrUtils.isEmpty(mGoodInfo.getOrig_price())) {
                tvAddNewShowGoodOrigprice.setVisibility(View.INVISIBLE);
            } else {
                tvAddNewShowGoodOrigprice.setVisibility(View.VISIBLE);
                StrUtils.SetTxt(tvAddNewShowGoodOrigprice, StrUtils.SetTextForMony(mGoodInfo.getOrig_price()));
                tvAddNewShowGoodOrigprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }

            if (mGoodInfo.getScore() > 0) {
                tvAddNewShowGoodScore.setVisibility(View.VISIBLE);
                StrUtils.SetTxt(tvAddNewShowGoodScore, "积分：" + mGoodInfo.getScore());
            } else {
                tvAddNewShowGoodScore.setVisibility(View.GONE);
            }

            if (mGoodInfo.getSales() > 0) {
                tvAddNewShowGoodSales.setVisibility(View.VISIBLE);
                StrUtils.SetTxt(tvAddNewShowGoodSales, "销量：" + mGoodInfo.getSales() + "件");
            } else {
                tvAddNewShowGoodSales.setVisibility(View.GONE);
            }
        } else {
            llAddNewShowGood.setVisibility(View.GONE);
            rlAddNewShowAddGood.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 点击左侧按钮的监听事件
     */
    public void title_left_bt(View v) {
        exitEdit();
    }

    @Override
    public void onBackPressed() {
        exitEdit();
    }

    private void exitEdit() {
        ShowCustomDialog("退出此次编辑？", "取消", "退出", new IDialogResult() {
            @Override
            public void LeftResult() {

            }

            @Override
            public void RightResult() {
                finish();
                overridePendingTransition(R.anim.push_rigth_in, R.anim.push_rigth_out);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinder.unbind();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {

        }
    }


    public class MyGridAdapter extends BaseAdapter {
        private LayoutInflater inflater;


        public MyGridAdapter(Context context) {

            inflater = LayoutInflater.from(context);


        }

        public void update() {

            this.notifyDataSetChanged();
        }

        public int getCount() {

            return datas.size();


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

                convertView = inflater.inflate(R.layout.item_add_new_show,
                        parent, false);
                GridView.LayoutParams params = new GridView.LayoutParams(width, width);
                convertView.setLayoutParams(params);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (!StrUtils.isEmpty(datas.get(position).getPathurl())) {
                holder.itemAddNewShowImage.setImageBitmap(StrUtils.GetBitMapFromPath(datas.get(position).getPathurl()));
            } else {
                String path = datas.get(position).getWeburl();
                ImageLoaderUtil.Load2(path, holder.itemAddNewShowImage, R.drawable.error_iv2);
            }
            final int MyPostion = position;
            holder.itemAddNewShowDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datas.remove(MyPostion);
                    notifyDataSetChanged();
                }
            });


            return convertView;
        }

    }

    class ViewHolder {
        @BindView(R.id.item_add_new_show_image)
        ImageView itemAddNewShowImage;
        @BindView(R.id.item_add_new_show_delete)
        ImageView itemAddNewShowDelete;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    /****************************************************
     * 上传七牛
     ***************************************************************/
    private int DescPicNeedUpNumber;
    private int DescPicCountNumber;

    private void uploadPics(final String content) {
        if (!NetUtil.isConnected(BaseContext)) {//检查网络
            PromptManager.ShowCustomToast(BaseContext, getResources().getString(R.string.network_not_connected));
            return;
        }
        DescPicNeedUpNumber = datas.size();
        DescPicCountNumber = 0;


        for (int i = 0; i < datas.size(); i++) {
            final int Postion = i;
            if (!StrUtils.isEmpty(datas.get(Postion).getWeburl())) {
                continue;
            }
            NUPLoadUtil dLoadUtils = new NUPLoadUtil(BaseContext, new File(
                    datas.get(Postion).getPathurl()), StrUtils.UploadQNName("show"));

            dLoadUtils.SetUpResult1(new NUPLoadUtil.UpResult1() {

                @Override
                public void Progress(String arg0, double arg1) {

                }

                @Override
                public void Onerror() {
                    datas.get(Postion).setWeburl("");
                    DescPicCountNumber = DescPicCountNumber + 1;

                    if (DescPicNeedUpNumber == DescPicCountNumber) {
                        // 上传描述完毕
                        submitShow(content);
                        LogUtils.i("***********************上传完毕***************************");
                    }
                }

                @Override
                public void Complete(String HostUrl, String Url) {
                    datas.get(Postion).setWeburl(HostUrl);
                    DescPicCountNumber = DescPicCountNumber + 1;
                    if (DescPicCountNumber == DescPicNeedUpNumber) {
                        // 上传描述完毕
                        submitShow(content);
                        LogUtils.i("====================上传完毕==================="+HostUrl);
                    }
                }
            });
            dLoadUtils.UpLoad();
        }


    }

    private void uploadVideo(final String content) {
        if (!NetUtil.isConnected(BaseContext)) {//检查网络
            PromptManager.ShowCustomToast(BaseContext, getResources().getString(R.string.network_not_connected));
            return;
        }
        //先上传封面
        NUpLoadUtils dLoadUtils = new NUpLoadUtils(BaseContext,
                Bitmap2Bytes(createVideoThumbnail(mCordVidoPath)),
                StrUtils.UploadQNName("photo"));
        dLoadUtils.SetUpResult(new NUpLoadUtils.UpResult() {
            @Override
            public void Progress(String arg0, double arg1) {
            }

            @Override
            public void Onerror() {
                upload_qiniu_video_cover_url = "";
            }

            @Override
            public void Complete(String HostUrl, String Url) {
                upload_qiniu_video_cover_url = HostUrl;
            }
        });
        dLoadUtils.UpLoad();


        NUPLoadUtil dLoadUtils1 = new NUPLoadUtil(BaseContext, new File(
                mCordVidoPath), StrUtils.UploadVido("vid"));
        dLoadUtils1.SetUpResult1(new NUPLoadUtil.UpResult1()

        {
            @Override
            public void Progress(String arg0, double arg1) {

            }

            @Override
            public void Onerror() {
                upload_qiniu_video_url = "";
                // 上传视频完毕
                submitShow(content);
            }

            @Override
            public void Complete(String HostUrl, String Url) {
                upload_qiniu_video_url = HostUrl;
                // 上传视频完毕
                submitShow(content);
            }
        });
        dLoadUtils.UpLoad();

    }

}

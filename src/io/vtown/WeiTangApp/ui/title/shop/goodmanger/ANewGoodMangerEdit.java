package io.vtown.WeiTangApp.ui.title.shop.goodmanger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.BaseApplication;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.BGoodDetail;
import io.vtown.WeiTangApp.bean.bcomment.easy.PicImageItem;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.net.qiniu.NUPLoadUtil;
import io.vtown.WeiTangApp.comment.net.qiniu.NUpLoadUtils;
import io.vtown.WeiTangApp.comment.util.DimensionPixelUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.comment.view.select_pic.PicSelActivity;
import io.vtown.WeiTangApp.event.interf.ICustomDialogResult;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.ALoactePhotoPager;
import io.vtown.WeiTangApp.ui.comment.AVidemplay;
import io.vtown.WeiTangApp.ui.comment.recordervido.ARecoderVido;

/**
 * Created by datutu on 2016/9/7.
 */
public class ANewGoodMangerEdit extends ATitleBase {

    private ScrollView new_good_manager_ed_scroll;
    //视频中使用到
    private ImageView new_good_manager_good_pics_pre, new_good_manager_good_pics_control_iv;
    //标识是否已经换成新的本地的视频
    private boolean IsNewVido;
    //  替换完毕视频后获取的版本地视频的路径
    private String NewLocationVidoLPath;


    private LinearLayout new_good_manager_good_vido_lay, new_good_manager_good_pics_lay,new_good_manager_good_des_lay;
    private TextView new_good_manager_good_ed_commint;

    private BUser mBUser;
    private String GoodId;
    private BGoodDetail datas;

    private boolean IsPic;
    private ImageView new_good_manager_ed_vid_add, tv_good_pics, tv_good_desc;
    private CompleteGridView new_good_manager_good_pics_gridview, new_good_manager_good_desc_gridview;

    //    private List<PicImageItem> good_pics11 = new ArrayList<PicImageItem>();
//    private List<PicImageItem> good_desc = new ArrayList<PicImageItem>();
    private int width = 0;
    private MyGridAdapter myAdapter1, myAdapter2;
    //上传时候需要记录下上传的数据
    private List<PicImageItem> Desc_Up_Reecords = new ArrayList<PicImageItem>();
    private List<PicImageItem> GoodsPic_Down_Reecords = new ArrayList<PicImageItem>();
    private String UpCover;
    private String VidoWebPath;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_new_good_manager_edit);
        EventBus.getDefault().register(this, "getEventMsg", BMessage.class);
        mBUser = Spuit.User_Get(BaseActivity);
        IBuund();
        IView();
        IData(GoodId);
    }

    private void IView() {
        new_good_manager_good_ed_commint = (TextView) findViewById(R.id.new_good_manager_good_ed_commint);

        new_good_manager_ed_scroll = (ScrollView) findViewById(R.id.new_good_manager_ed_scroll);
        new_good_manager_good_pics_pre = (ImageView) findViewById(R.id.new_good_manager_good_pics_pre);
        new_good_manager_good_pics_control_iv = (ImageView) findViewById(R.id.new_good_manager_good_pics_control_iv);

        new_good_manager_good_des_lay= (LinearLayout) findViewById(R.id.new_good_manager_good_des_lay);
        new_good_manager_ed_vid_add = (ImageView) findViewById(R.id.new_good_manager_ed_vid_add);
        new_good_manager_good_vido_lay = (LinearLayout) findViewById(R.id.new_good_manager_good_vido_lay);
        new_good_manager_good_pics_lay = (LinearLayout) findViewById(R.id.new_good_manager_good_pics_lay);


        screenWidth = screenWidth - DimensionPixelUtil.dip2px(BaseContext, 16);
        width = screenWidth / 3;
        tv_good_pics = (ImageView) findViewById(R.id.tv_good_pics);
        tv_good_desc = (ImageView) findViewById(R.id.tv_good_desc);
        new_good_manager_good_pics_gridview = (CompleteGridView) findViewById(R.id.new_good_manager_good_pics_gridview);
        new_good_manager_good_desc_gridview = (CompleteGridView) findViewById(R.id.new_good_manager_good_desc_gridview);

        new_good_manager_good_ed_commint.setOnClickListener(this);
        new_good_manager_good_pics_control_iv.setOnClickListener(this);
        new_good_manager_ed_vid_add.setOnClickListener(this);
        tv_good_pics.setOnClickListener(this);
        tv_good_desc.setOnClickListener(this);
        new_good_manager_good_pics_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(BaseContext,
                        ALoactePhotoPager.class);
                mIntent.putExtra("position", position);
                BaseApplication.GetInstance().setPicImages(myAdapter1.GetDatas());
                PromptManager.SkipActivity(BaseActivity, mIntent);
            }
        });
        new_good_manager_good_desc_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(BaseContext,
                        ALoactePhotoPager.class);
                mIntent.putExtra("position", position);
                BaseApplication.GetInstance().setPicImages(myAdapter2.GetDatas());
                PromptManager.SkipActivity(BaseActivity, mIntent);
            }
        });
    }

    // 获取商品详情的通道
    private void IData(String GoodId) {
        PromptManager.showtextLoading(BaseContext,
                getResources()
                        .getString(R.string.xlistview_header_hint_loading));
        SetTitleHttpDataLisenter(this);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("goods_id", GoodId);
        map.put("extend", "1");
        map.put("member_id", mBUser.getId());
        map.put("check_edit", "1");
        map.put("seller_id", mBUser.getSeller_id());
        FBGetHttpData(map, Constants.GoodDetail, Request.Method.GET, 0, LOAD_INITIALIZE);
    }

    private void IBuund() {
        if (getIntent().getExtras() != null
                && getIntent().getExtras().containsKey("goodid")) {
            GoodId = getIntent().getStringExtra("goodid");
            return;
        }
        BaseActivity.finish();

    }

    /**
     * 刷新view****************只能修改轮播图和宝贝描述***********************
     */
    private void FrashView(BGoodDetail datas2) {
        new_good_manager_good_ed_commint.setVisibility(View.VISIBLE);

        IsPic = datas2.getGoods_info().getRtype().equals("0") ? true : false;
        new_good_manager_ed_scroll.smoothScrollTo(0, 20);
        if (!IsPic) {//视频
            new_good_manager_good_vido_lay.setVisibility(View.VISIBLE);
            ImageLoaderUtil.Load2(datas2.getCover(), new_good_manager_good_pics_pre, R.drawable.error_iv2);
        } else {//图片
            new_good_manager_good_pics_lay.setVisibility(View.VISIBLE);
            List<String> pics_datas = datas2.getGoods_info().getRoll();
            if (pics_datas != null && pics_datas.size() > 0) {
//                good_pics = GetPicChange(pics_datas);
                myAdapter1 = new MyGridAdapter(BaseContext, GetPicChange(pics_datas));
                new_good_manager_good_pics_gridview.setAdapter(myAdapter1);
            }
        }
        new_good_manager_good_des_lay.setVisibility(View.VISIBLE);
        if (null != datas.getGoods_info().getIntro() && datas.getGoods_info().getIntro().size() > 0) { // 判断是否存在宝贝描述**************
//            good_desc = GetPicChange(datas2.getGoods_info().getIntro());
            myAdapter2 = new MyGridAdapter(BaseContext, GetPicChange(datas2.getGoods_info().getIntro()));
            new_good_manager_good_desc_gridview.setAdapter(myAdapter2);
        }
    }

    @Override
    protected void InitTile() {
        SetTitleTxt("商品编辑");
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        switch (Data.getHttpResultTage()) {
            case 0:
                // ==================>"is_edit":1 //0-不可编辑 1-自营发布人，可修改商品详情
                // 2-末级代卖，可修改代卖价格
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    // TODO未获取到数据后需要进行提示并且推出
                    PromptManager.ShowCustomToast1(BaseContext, Msg);
                    BaseActivity.finish();
                    return;
                }

                datas = new BGoodDetail();
                try {
                    datas = JSON.parseObject(Data.getHttpResultStr(),
                            BGoodDetail.class);

                } catch (Exception e) {
                    DataError("解析错误", 1);
                    return;
                }
                // 获取到数据后进行刷新数据
                FrashView(datas);
                break;

            case 10:
                PromptManager.closeTextLoading3();
                PromptManager.ShowCustomToast(BaseContext, "编辑完成");
                EventBus.getDefault().post(new BMessage(BMessage.Good_Manger_Frash_Hind));
                BaseActivity.finish();
                break;
        }

    }

    @Override
    protected void DataError(String error, int LoadType) {
        if (LoadType == 10) {
            PromptManager.closeTextLoading3();
            PromptManager.ShowCustomToast(BaseContext, "编辑失败");
        }

    }


    /**
     * 如果是图片将图片换成b
     */
    private List<PicImageItem> GetPicChange(List<String> pics) {
        List<PicImageItem> items = new ArrayList<PicImageItem>();
        // 需要图片转化内置的列表数据======》并且展示
        for (int i = 0; i < pics.size(); i++) {
            items.add(new PicImageItem(pics.get(i), ""));

        }
        return items;
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
            case R.id.new_good_manager_good_ed_commint://提交编辑
                if (IsPic && myAdapter1.GetDatas().size() == 0) {
                    PromptManager.ShowCustomToast(BaseContext, "商品图片不能清空");
                    return;

                }
                if (myAdapter2.GetDatas().size() == 0) {
                    PromptManager.ShowCustomToast(BaseContext, "商品描述不能清空");
                    return;

                }

                ShowCustomDialog("确定编辑商品?", "取消", "编辑", new IDialogResult() {
                    @Override
                    public void LeftResult() {

                    }

                    @Override
                    public void RightResult() {
                        BeginUp();
                    }
                });
                break;


            case R.id.new_good_manager_ed_vid_add://点击替换==>跳转发到录制视频界面
                PromptManager
                        .SkipActivity(BaseActivity, new Intent(
                                BaseActivity, ARecoderVido.class));
                break;
            case R.id.new_good_manager_good_pics_control_iv://视频点击播放
                if (CheckNet(BaseContext))
                    return;
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        AVidemplay.class).putExtra(AVidemplay.VidoKey, IsNewVido ? NewLocationVidoLPath : datas.getGoods_info().getVid()).putExtra("IsSdPath", IsNewVido));

                break;
            case R.id.tv_good_pics://上边图片点击添加
                toPicSelect(myAdapter1.GetDatas(), PicSelActivity.Tage_Good_Pics);
                break;

            case R.id.tv_good_desc:
                toPicSelect(myAdapter2.GetDatas(), PicSelActivity.Tage_Good_Desc);
                break;
        }

    }

    private void toPicSelect(List<PicImageItem> datas, int type) {
        if (9 - datas.size() > 0) {
            Intent intent = new Intent(BaseContext, PicSelActivity.class);
            intent.putExtra(PicSelActivity.Select_Img_Size_str, 9 - datas.size());
            intent.putExtra(PicSelActivity.Select_Img_Type, type);
            startActivity(intent);
        } else {
            PromptManager.ShowCustomToast(BaseContext, "亲，你已经有9张图片了");
            return;
        }
    }

    @Override
    protected void InItBundle(Bundle bundle) {

    }

    @Override
    protected void SaveBundle(Bundle bundle) {

    }


    public class MyGridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<PicImageItem> mShow_datas = new ArrayList<PicImageItem>();
        boolean IsFristHaShow;

        public MyGridAdapter(Context context, List<PicImageItem> show_datas) {
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
                GridView.LayoutParams params = new GridView.LayoutParams(width, width);
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
                    notifyDataSetChanged();
                }
            });
            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
            public ImageView image_delete;
        }

    }

    public void getEventMsg(BMessage event) {
        int msg_type = event.getMessageType();
        switch (msg_type) {
            case BMessage.Tage_Select_Pic_Good_Pics:
                List<String> imgs1 = event.getTmpArrayList();
                updataList(new_good_manager_good_pics_gridview, myAdapter1, imgs1, myAdapter1.GetDatas());
                break;
            case BMessage.Tage_Select_Pic_Good_Desc:
                List<String> imgs2 = event.getTmpArrayList();
                updataList(new_good_manager_good_desc_gridview, myAdapter2, imgs2, myAdapter2.GetDatas());
                break;
            case 290://标识已经录制完毕视频返回来
                IsNewVido = true;
                NewLocationVidoLPath = event.getReCordVidoPath();
                new_good_manager_good_pics_pre.setImageBitmap(createVideoThumbnail(NewLocationVidoLPath));
                break;
        }

    }

    private void updataList(CompleteGridView Gradvvvv, MyGridAdapter AP, List<String> data1, List<PicImageItem> data2) {
        List<PicImageItem> ddd = new ArrayList<PicImageItem>();
        ddd = AP.GetDatas();
        if (data1 != null && data1.size() > 0) {
            for (int i = 0; i < data1.size(); i++) {
                PicImageItem item = new PicImageItem("", data1.get(i));
                ddd.add(item);
            }
            AP = null;
            AP = new MyGridAdapter(BaseContext, ddd);
            Gradvvvv.setAdapter(AP);
//            AP.update();
        }

    }

    //开始上传图片*************************************************************************
    private void BeginUp() {
        //首先上传的是图文详情(无论视频还是图片=》全都包含商品描述)
        //上传完毕描述后再上传视频||商品轮播图
        PromptManager.showtextLoading3(BaseContext, "编辑中请耐心等待");
        UpDescPic();
    }

    //商品描述 上传时候记录上传数量
    private int DescCountNumber = 0;
    private int DescNeedNumber = 0;

    private void UpDescPic() {
        Desc_Up_Reecords = myAdapter2.GetDatas();
        DescNeedNumber = 0;
        DescCountNumber = 0;

        for (int i = 0; i < Desc_Up_Reecords.size(); i++) {
            if (StrUtils.isEmpty(Desc_Up_Reecords.get(i).getWeburl()))
                DescNeedNumber = DescNeedNumber + 1;
        }

        if (DescNeedNumber == 0) {
            //商品描述不需要本地图片上传
            UpUpData();
            return;
        }
        for (int i = 0; i < Desc_Up_Reecords.size(); i++) {
            final int Postion = i;
            if (!StrUtils.isEmpty(Desc_Up_Reecords.get(Postion).getWeburl())) {
                continue;
            }
            NUPLoadUtil dLoadUtils = new NUPLoadUtil(BaseContext, new File(
                    Desc_Up_Reecords.get(Postion).getPathurl()), StrUtils.UploadQNName("photo"));

            dLoadUtils.SetUpResult1(new NUPLoadUtil.UpResult1() {

                @Override
                public void Progress(String arg0, double arg1) {

                }

                @Override
                public void Onerror() {
                    Desc_Up_Reecords.get(Postion).setWeburl("");
                    DescCountNumber = DescCountNumber + 1;

                    if (DescCountNumber == DescNeedNumber) {
                        // 上传描述完毕
                        UpUpData();
                    }
                }

                @Override
                public void Complete(String HostUrl, String Url) {
                    Desc_Up_Reecords.get(Postion).setWeburl(HostUrl);
                    DescCountNumber = DescCountNumber + 1;
                    if (DescCountNumber == DescNeedNumber) {
                        // 上传描述完毕
                        UpUpData();
                    }
                }
            });
            dLoadUtils.UpLoad();
        }


    }

    //商品描述 上传时候记录上传数量
    private int GoodPicCountNumber = 0;
    private int GoodPicNeedNumber = 0;

    //开始上传上边的数据===》判断视频还是图片
    private void UpUpData() {
        if (IsPic) {//图片
            GoodsPic_Down_Reecords = myAdapter1.GetDatas();
            GoodPicNeedNumber = 0;
            GoodPicCountNumber = 0;
            for (int i = 0; i < GoodsPic_Down_Reecords.size(); i++) {
                if (StrUtils.isEmpty(GoodsPic_Down_Reecords.get(i).getWeburl()))
                    GoodPicNeedNumber = GoodPicNeedNumber + 1;
            }

            if (GoodPicNeedNumber == 0) {
                //商品描述不需要本地图片上传
                CommentEditData();
                return;
            }
            for (int i = 0; i < GoodsPic_Down_Reecords.size(); i++) {
                final int Postion = i;
                if (!StrUtils.isEmpty(GoodsPic_Down_Reecords.get(Postion).getWeburl())) {
                    continue;
                }
                NUPLoadUtil dLoadUtils = new NUPLoadUtil(BaseContext, new File(
                        GoodsPic_Down_Reecords.get(Postion).getPathurl()), StrUtils.UploadQNName("photo"));

                dLoadUtils.SetUpResult1(new NUPLoadUtil.UpResult1() {
                    @Override
                    public void Progress(String arg0, double arg1) {

                    }

                    @Override
                    public void Onerror() {
                        GoodsPic_Down_Reecords.get(Postion).setWeburl("");
                        GoodPicCountNumber = GoodPicCountNumber + 1;

                        if (GoodPicCountNumber == GoodPicNeedNumber) {
                            // 上传描述完毕
                            CommentEditData();
                        }
                    }

                    @Override
                    public void Complete(String HostUrl, String Url) {
                        GoodsPic_Down_Reecords.get(Postion).setWeburl(HostUrl);
                        GoodPicCountNumber = GoodPicCountNumber + 1;
                        if (GoodPicCountNumber == GoodPicNeedNumber) {
                            // 上传描述完毕
                            CommentEditData();
                        }
                    }
                });
                dLoadUtils.UpLoad();
            }
        } else {//视频封面开始上传*********************
            NUpLoadUtils dLoadUtils = new NUpLoadUtils(BaseContext,
                    Bitmap2Bytes(createVideoThumbnail(NewLocationVidoLPath)),
                    StrUtils.UploadQNName("photo"));
            dLoadUtils.SetUpResult(new NUpLoadUtils.UpResult() {
                @Override
                public void Progress(String arg0, double arg1) {
                }

                @Override
                public void Onerror() {
                    UpCover = "";
                    UpVidoData();
                }

                @Override
                public void Complete(String HostUrl, String Url) {
                    UpCover = HostUrl;
                    UpVidoData();
                }
            });
            dLoadUtils.UpLoad();
        }
    }

    //开始上传视频
    private void UpVidoData() {
        NUPLoadUtil dLoadUtils = new NUPLoadUtil(BaseContext, new File(
                NewLocationVidoLPath), StrUtils.UploadVido("vid"));
        dLoadUtils.SetUpResult1(new NUPLoadUtil.UpResult1() {
            @Override
            public void Progress(String arg0, double arg1) {

            }

            @Override
            public void Onerror() {
                VidoWebPath = "";
                // 上传视频完毕
                CommentEditData();
            }

            @Override
            public void Complete(String HostUrl, String Url) {
                VidoWebPath = HostUrl;
                // 上传视频完毕
                CommentEditData();
            }
        });
        dLoadUtils.UpLoad();
    }

    //开始调接口进行服务器交互****
    private void CommentEditData() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("goods_id", GoodId);
        // 商品详情 多个图片的json格式***********************************

        map.put("intro", JSON.toJSONString(GetLsFromDatass(Desc_Up_Reecords)));
        map.put("ratio", GetRate(Desc_Up_Reecords.size()));
        // 轮播图**************************************
        if (IsPic) {//图片
            map.put("roll", JSON.toJSONString(GetLsFromDatass(GoodsPic_Down_Reecords)));// 轮播图
        } else {//视频
            map.put("roll", "");// 轮播图
        }
        map.put("vid", !IsNewVido ? datas.getGoods_info().getVid()
                : VidoWebPath);// 小视频地址
        map.put("cover", !IsNewVido ?  GoodsPic_Down_Reecords.get(0).getWeburl() : UpCover);// 小视频封面
        map.put("seller_id", mBUser.getSeller_id());//
        map.put("rtype", IsPic ? "0" : "1");// 轮播图内容类型 0-图片 1-小视频
        FBGetHttpData(map, Constants.GoodAlter, Request.Method.PUT, 10, LOAD_INITIALIZE);

    }

    private List<String> GetLsFromDatass(List<PicImageItem> DATA) {
        List<String> Strss = new ArrayList<String>();
        for (int i = 0; i < DATA.size(); i++) {
            if (!StrUtils.isEmpty(DATA.get(i).getWeburl()))
                Strss.add(DATA.get(i).getWeburl());
        }
        return Strss;

    }

    private String GetRate(int MysIZE) {
        ArrayList<Float> goodsDescriptFloats = new ArrayList<Float>();
        for (int i = 0; i < MysIZE; i++) {
            goodsDescriptFloats.add(0.5f);
        }

        return StrUtils.GetStrs(goodsDescriptFloats);
    }
    //开始上传图片*************************************************************************


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            return;
        }
    }
}

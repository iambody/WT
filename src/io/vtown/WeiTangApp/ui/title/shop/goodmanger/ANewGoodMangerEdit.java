package io.vtown.WeiTangApp.ui.title.shop.goodmanger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.BGoodDetail;
import io.vtown.WeiTangApp.bean.bcomment.easy.PicImageItem;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.DimensionPixelUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.comment.view.select_pic.PicSelActivity;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * Created by datutu on 2016/9/7.
 */
public class ANewGoodMangerEdit extends ATitleBase {

    private BUser mBUser;
    private String GoodId;
    private BGoodDetail datas;

    private boolean IsPic = true;
    private TextView tv_good_pics, tv_good_desc;
    private CompleteGridView new_good_manager_good_pics_gridview, new_good_manager_good_desc_gridview;

    private List<PicImageItem> good_pics = new ArrayList<PicImageItem>();
    private List<PicImageItem> good_desc = new ArrayList<PicImageItem>();
    private int width = 0;
    private MyGridAdapter myAdapter1,myAdapter2;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_new_good_manager_edit);
        EventBus.getDefault().register(this,"getEventMsg", BMessage.class);
        mBUser = Spuit.User_Get(BaseActivity);
        IBuund();
        IView();
        IData(GoodId);
    }

    private void IView() {
        screenWidth = screenWidth - DimensionPixelUtil.dip2px(BaseContext, 16);
        width = screenWidth / 3;
        tv_good_pics = (TextView) findViewById(R.id.tv_good_pics);
        tv_good_desc = (TextView) findViewById(R.id.tv_good_desc);
        new_good_manager_good_pics_gridview = (CompleteGridView) findViewById(R.id.new_good_manager_good_pics_gridview);
        new_good_manager_good_desc_gridview = (CompleteGridView) findViewById(R.id.new_good_manager_good_desc_gridview);

        tv_good_pics.setOnClickListener(this);
        tv_good_desc.setOnClickListener(this);
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
        List<String> pics_datas = datas2.getGoods_info().getRoll();


        if (pics_datas != null && pics_datas.size() > 0) {
            good_pics = GetPicChange(pics_datas);
            myAdapter1 = new MyGridAdapter(BaseContext,good_pics);
            new_good_manager_good_pics_gridview.setAdapter(myAdapter1);
        }


        if (!StrUtils.isEmpty(datas.getGoods_info().getIntro())) { // 判断是否存在宝贝描述**************
            good_desc = GetPicChange(JSON.parseArray(datas2.getGoods_info().getIntro(), String.class));
            myAdapter2 = new MyGridAdapter(BaseContext,good_desc);
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
                BaseActivity.finish();
                break;
        }

    }

    @Override
    protected void DataError(String error, int LoadType) {
        PromptManager.closeTextLoading3();
        PromptManager.ShowCustomToast(BaseContext, "编辑失败");
    }


    /**
     * 如果是图片将图片换成bitmap数组&&&&&&&&&&&&&&如果是视频就不需要操作
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
            case R.id.tv_good_pics:
                toPicSelect(good_pics,PicSelActivity.Tage_Good_Pics);
                break;

            case R.id.tv_good_desc:
                toPicSelect(good_desc,PicSelActivity.Tage_Good_Desc);
                break;
        }

    }

    private void toPicSelect(List<PicImageItem> datas,int type){
        if(9-datas.size() >0){
            Intent intent = new Intent(BaseContext, PicSelActivity.class);
            intent.putExtra(PicSelActivity.Select_Img_Size_str,9-datas.size());
            intent.putExtra(PicSelActivity.Select_Img_Type,type);
            startActivity(intent);
        }else{
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


        public MyGridAdapter(Context context, List<PicImageItem> show_datas) {

            inflater = LayoutInflater.from(context);
            this.mShow_datas = show_datas;

        }

        public void update() {

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

    public void getEventMsg(BMessage event){
        int msg_type = event.getMessageType();
        switch (msg_type){
            case BMessage.Tage_Select_Pic_Good_Pics:
                List<String> imgs1 = event.getTmpArrayList();
                updataList(myAdapter1,imgs1,good_pics);
                break;
            case BMessage.Tage_Select_Pic_Good_Desc:
                List<String> imgs2 = event.getTmpArrayList();
                updataList(myAdapter2,imgs2,good_desc);
                break;
        }

    }

    private void updataList(MyGridAdapter AP,List<String> data1,List<PicImageItem> data2){
        if (data1 != null && data1.size() > 0) {

            for (String path : data1) {
                PicImageItem item = new PicImageItem("", path);
                data2.add(item);
            }

            AP.update();
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

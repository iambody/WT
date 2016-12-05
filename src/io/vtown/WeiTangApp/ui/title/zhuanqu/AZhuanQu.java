package io.vtown.WeiTangApp.ui.title.zhuanqu;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BDComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BLDComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.zhuanqu.BZhuan;
import io.vtown.WeiTangApp.bean.bcomment.easy.zhuanqu.BZhuanQuBean;
import io.vtown.WeiTangApp.bean.bcomment.easy.zhuanqu.BZhuanquGood;
import io.vtown.WeiTangApp.bean.bcomment.new_three.BActive;
import io.vtown.WeiTangApp.bean.bcomment.new_three.BNewHome;
import io.vtown.WeiTangApp.bean.bcomment.news.BNew;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.comment.view.custom.CompleteListView;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.AWeb;
import io.vtown.WeiTangApp.ui.title.AGoodDetail;
import io.vtown.WeiTangApp.ui.title.loginregist.bindcode_three.ANewBindCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-7-14 下午7:18:16
 * @author===》专区界面
 */
public class AZhuanQu extends ATitleBase {
    private View BaseView;
    private ScrollView zhuanqu_scrollview;
    //    private ImageCycleView imageCycleView;
    private View zhuan_nodata_lay;

    private ArrayList<String> d = new ArrayList<String>();

    private HuoDongAdapter huoDongAdapter;

    private CompleteListView zhuanqu_ls;
    private ImageView zhuanqu_banner_iv;
    private BZhuan bdComment;
    private BUser MuBUser;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_zhaunqu);
        BaseView = LayoutInflater.from(this).inflate(R.layout.activity_zhaunqu, null);
        MuBUser = Spuit.User_Get(this);
        IBunds();
        IBasV();
        SetTitleHttpDataLisenter(this);
        IData(baseBcBComment.getId());
    }

    private void IData(String ActivitId) {
        PromptManager.showtextLoading(BaseContext,
                getResources().getString(R.string.loading));
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("activity_id", ActivitId);
        map.put("invite_code", MuBUser.getInvite_code());
        FBGetHttpData(map, Constants.HuoDongZhuanQu, Method.GET, 0,
                LOAD_INITIALIZE);

    }

    private void IBasV() {
        zhuanqu_banner_iv = (ImageView) findViewById(R.id.zhuanqu_banner_iv);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth, screenWidth / 2);
        zhuanqu_banner_iv.setLayoutParams(params);

        zhuan_nodata_lay = findViewById(R.id.zhuan_nodata_lay);
        zhuanqu_scrollview = (ScrollView) findViewById(R.id.zhuanqu_scrollview);


//        imageCycleView = (ImageCycleView) findViewById(R.id.zhuanqu_banner);

        zhuanqu_ls = (CompleteListView) findViewById(R.id.zhuanqu_ls);
        huoDongAdapter = new HuoDongAdapter(BaseContext);
        zhuanqu_ls.setAdapter(huoDongAdapter);

        IDataView(zhuanqu_scrollview, zhuan_nodata_lay, NOVIEW_INITIALIZE);
        zhuan_nodata_lay.setOnClickListener(this);
        ShowErrorCanLoad("点我重试哦");
    }


    private void IBunds() {
    }


    @Override
    protected void InitTile() {
        SetTitleTxt(baseBcBComment.getTitle());
        SetRightIv(R.drawable.detail_share);
        right_iv.setOnClickListener(this);
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        if (StrUtils.isEmpty(Data.getHttpResultStr())) {
            PromptManager.ShowCustomToast(BaseContext, Msg);
            IDataView(zhuanqu_scrollview, zhuan_nodata_lay, NOVIEW_ERROR);
            return;
        }
        IDataView(zhuanqu_scrollview, zhuan_nodata_lay, NOVIEW_RIGHT);

        bdComment = JSON.parseObject(Data.getHttpResultStr(),
                BZhuan.class);

        zhuanqu_scrollview.smoothScrollTo(0, 20);

        ImageLoaderUtil.Load2(bdComment.getPic_path(), zhuanqu_banner_iv, R.drawable.error_iv1);
        huoDongAdapter.FrashAp(bdComment.getCategory());
    }

    @Override
    protected void DataError(String error, int LoadType) {
        IDataView(zhuanqu_scrollview, zhuan_nodata_lay, NOVIEW_ERROR);
        ShowErrorCanLoad(error);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        imageCycleView.startImageCycle();
    }

    ;

    @Override
    protected void onPause() {
        super.onPause();
//        imageCycleView.pushImageCycle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        imageCycleView.pushImageCycle();
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
        switch (V.getId()) {
            case R.id.zhuan_nodata_lay:
                IData(baseBcBComment.getId());
                break;
            case R.id.right_iv:
                if (CheckNet(BaseContext))
                    return;

                //如果未绑定或者已绑定未激活的用户分享权限的判断***************************
                if (!Spuit.IsHaveBind_Get(BaseContext) && !Spuit.IsHaveBind_JiQi_Get(BaseContext)) {//未绑定邀请码
                    ShowCustomDialog(getResources().getString(R.string.no_bind_code),
                            getResources().getString(R.string.quxiao), getResources().getString(R.string.bind_code),
                            new IDialogResult() {
                                @Override
                                public void RightResult() {
                                    PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
                                            ANewBindCode.class));
                                    //
                                }

                                @Override
                                public void LeftResult() {
                                }
                            });
                    return;
                }
                if (!Spuit.IsHaveActive_Get(BaseContext)) {//绑定邀请码未激活
                    ShowCustomDialog(JSON.parseObject(CacheUtil.NewHome_Get(BaseContext), BNewHome.class).getIntegral() < 10000 ? getResources().getString(R.string.to_Jihuo_toqiandao1) : getResources().getString(R.string.to_Jihuo_toqiandao2),
                            getResources().getString(R.string.look_guize), getResources().getString(R.string.to_jihuo1),
                            new IDialogResult() {
                                @Override
                                public void RightResult() {
                                    IData(JSON.parseObject(CacheUtil.NewHome_Get(BaseContext), BNewHome.class).getActivityid());
                                    //刷新数据就可以！！！！！！！！！！！！！！！！！！！！！

                                }

                                @Override
                                public void LeftResult() {
                                    PromptManager.SkipActivity(BaseActivity, new Intent(
                                            BaseActivity, AWeb.class).putExtra(
                                            AWeb.Key_Bean,
                                            new BComment(Constants.Homew_JiFen, getResources().getString(R.string.jifenguize))));

                                }
                            });

                    return;
                }


                //如果未绑定或者已绑定未激活的用户分享权限的判断***************************
                // PromptManager.ShowCustomToast(BaseContext, "分享");
                BNew mBNew = new BNew();
                mBNew.setShare_url(bdComment.getUrl());
                mBNew.setShare_content(bdComment.getContent());
                mBNew.setShare_title(bdComment.getTitle());
                mBNew.setShare_log(bdComment.getPic_path());
                ShowP(BaseView, mBNew);
                break;
        }
    }

    @Override
    protected void InItBundle(Bundle bundle) {
    }

    @Override
    protected void SaveBundle(Bundle bundle) {
    }

    class HuoDongAdapter extends BaseAdapter {
        private List<BZhuanQuBean> data = new ArrayList<BZhuanQuBean>();

        private LayoutInflater layoutInflater;

        public HuoDongAdapter(Context myApContexst) {
            super();

            this.layoutInflater = LayoutInflater.from(BaseContext);

        }

        /**
         * 刷新数据
         *
         * @param mBlComments
         */
        public void FrashAp(List<BZhuanQuBean> mBlComments) {
            this.data = mBlComments;
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return data.size();
            // return 4;
        }

        @Override
        public Object getItem(int arg0) {
            return data.get(arg0);
            // return null;
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            ZhaunQuItem zhaunQuItem = null;
            if (null == arg1) {
                arg1 = layoutInflater.inflate(R.layout.item_zhuanqu, null);
                zhaunQuItem = new ZhaunQuItem();

                zhaunQuItem.item_zhuanqu_name = ViewHolder.get(arg1,
                        R.id.item_zhuanqu_name);

                zhaunQuItem.item_zhuanqu_grid = (CompleteGridView) arg1
                        .findViewById(R.id.item_zhuanqu_grid);

                arg1.setTag(zhaunQuItem);
            } else {
                zhaunQuItem = (ZhaunQuItem) arg1.getTag();
            }
            final BZhuanQuBean daComment = data.get(arg0);

            StrUtils.SetTxt(zhaunQuItem.item_zhuanqu_name,
                    daComment.getCategory_name());
            zhaunQuItem.item_zhuanqu_grid.setAdapter(new InAp(daComment
                    .getGoods(), BaseContext));

            final int Mypostion = arg0;
            zhaunQuItem.item_zhuanqu_grid
                    .setOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1,
                                                int arg2, long arg3) {

                            PromptManager.SkipActivity(BaseActivity,
                                    new Intent(BaseContext, AGoodDetail.class)
                                            .putExtra("goodid", daComment
                                                    .getGoods().get(arg2)
                                                    .getGoods_id()));
                        }
                    });
            return arg1;
        }

        class ZhaunQuItem {
            TextView item_zhuanqu_name;
            CompleteGridView item_zhuanqu_grid;
        }
    }

    /**
     * 内部的gridview的适配器
     */
    private class InAp extends BaseAdapter {
        private List<BZhuanquGood> comments = new ArrayList<BZhuanquGood>();
        private Context inContext;
        private LayoutInflater layoutInflater;

        public InAp(List<BZhuanquGood> commentssss, Context inContext) {
            super();
            this.comments = commentssss;
            this.inContext = inContext;
            this.layoutInflater = LayoutInflater.from(inContext);
        }

        @Override
        public int getCount() {
            return comments.size();
        }

        @Override
        public Object getItem(int position) {
            return comments.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HuoDongInItem dongInItem;
            if (convertView == null) {
                convertView = layoutInflater.inflate(
                        R.layout.item_zhuanqu_in_item, null);
                dongInItem = new HuoDongInItem();
                dongInItem.item_zhuanqu_in_iv = ViewHolder.get(convertView,
                        R.id.item_zhuanqu_in_iv);
                dongInItem.item_zhuanqu_in_name = ViewHolder.get(convertView,
                        R.id.item_zhuanqu_in_name);
                dongInItem.item_zhuanqu_in_price = ViewHolder.get(convertView,
                        R.id.item_zhuanqu_in_price);
                dongInItem.item_zhuanqu_odl_price = ViewHolder.get(convertView,
                        R.id.item_zhuanqu_odl_price);
                dongInItem.item_zhuanqu_in_sales = ViewHolder.get(convertView,
                        R.id.item_zhuanqu_in_sales);

                dongInItem.item_zhuanqu_in_score = ViewHolder.get(convertView,
                        R.id.item_zhuanqu_in_score);


                convertView.setTag(dongInItem);
            } else {
                dongInItem = (HuoDongInItem) convertView.getTag();
            }
            BZhuanquGood dddata = comments.get(position);

            ImageLoaderUtil.Load2(dddata.getCover(),
                    dongInItem.item_zhuanqu_in_iv, R.drawable.error_iv2);
            StrUtils.SetTxt(dongInItem.item_zhuanqu_in_name, dddata.getTitle());
            StrUtils.SetTxt(dongInItem.item_zhuanqu_in_price,
                    StrUtils.SetTextForMony(dddata.getSell_price()) + "元");

            if (!StrUtils.isEmpty(dddata.getOrig_price()) && !dddata.getOrig_price().equals("0")) {
                StrUtils.SetTxt(dongInItem.item_zhuanqu_odl_price,
                        "原价" + StrUtils.SetTextForMony(dddata.getOrig_price()));
                dongInItem.item_zhuanqu_odl_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
            //销量

            if (!StrUtils.isEmpty(dddata.getSales())) {
                dongInItem.item_zhuanqu_in_sales.setText(String.format("销量：%s", dddata.getSales()));
            }
            //积分
            if (!StrUtils.isEmpty(dddata.getScore())) {
                dongInItem.item_zhuanqu_in_score.setText(String.format("积分：%s", dddata.getScore()));
            }
            return convertView;
        }

        class HuoDongInItem {
            ImageView item_zhuanqu_in_iv;
            TextView item_zhuanqu_in_name;
            TextView item_zhuanqu_in_price;
            TextView item_zhuanqu_odl_price, item_zhuanqu_in_sales, item_zhuanqu_in_score;

        }
    }
}

package io.vtown.WeiTangApp.fragment.main;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.comment.contant.ImagePathConfig;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.util.ui.WaveHelper;
import io.vtown.WeiTangApp.comment.view.CircleImageView;
import io.vtown.WeiTangApp.comment.view.ImageCycleView;
import io.vtown.WeiTangApp.comment.view.WaveView;
import io.vtown.WeiTangApp.comment.view.custom.HomeScrollView;
import io.vtown.WeiTangApp.fragment.FBase;
import io.vtown.WeiTangApp.ui.comment.AphotoPager;
import io.vtown.WeiTangApp.ui.title.mynew.ANew;
import io.vtown.WeiTangApp.ui.ui.ANewHome;
import io.vtown.WeiTangApp.ui.ui.ASouSouGood;
import io.vtown.WeiTangApp.ui.ui.CaptureActivity;

/**
 * Created by datutu on 2016/10/9.
 */

public class FMainNewHome extends FBase implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, HomeScrollView.OnScrollListener {
    @BindView(R.id.fragment_newhome_iv_sao)
    ImageView fragmentNewhomeIvSao; //扫一扫
    @BindView(R.id.fragment_newhome_iv_sou)
    ImageView fragmentNewhomeIvSou; // 搜索
    @BindView(R.id.fragment_newhome_iv_new)
    ImageView fragmentNewhomeIvNew; // 搜索
    @BindView(R.id.fragment_newhome_bg)
    ImageView fragmentNewhomeBg; //我的背景图片
    @BindView(R.id.fragment_newhome_waveview)
    WaveView fragmentNewhomeWaveview; //波浪view
    @BindView(R.id.fragment_newhome_head_iv)
    CircleImageView fragmentNewhomeHeadIv;//头像
    @BindView(R.id.fragment_newhome_banner)
    ImageCycleView fragmentNewhomeBanner;//Banner
    @BindView(R.id.fragment_newhome_srollviw)
    SwipeRefreshLayout fragmentNewhomeSrollviw;
    @BindView(R.id.fragment_newhome_insrollviw)
    HomeScrollView fragmentNewhomeInsrollviw;

    private LinearLayout fragmentNewhomeIvLaya;
    //上边的title
    private View fragment_newhome_putitle_lay;
    //邀请人数，我的积分，返佣金额
    private View fragment_newhome_renshu_lay, fragment_newhome_jifen_lay, fragment_newhome_yongjin_lay;
    private TextView Text_fragment_newhome_RenShu, Text_fragment_newhome_JiFen, Text_fragment_newhome_YongJin;
    //ButterKnife句柄
    private Unbinder unbinder;
    //WaveHelper句柄
    private WaveHelper mWaveHelper;

    @Override
    public void InItView() {
        BaseView = LayoutInflater.from(BaseContext).inflate(R.layout.fragment_newmainhome, null);
        unbinder = ButterKnife.bind(this, BaseView);
        IBase();
    }

    private void IBase() {
        fragmentNewhomeSrollviw.setOnRefreshListener(this);
        fragmentNewhomeInsrollviw.setScrolListener(this);
        fragment_newhome_putitle_lay = ViewHolder.get(BaseView, R.id.fragment_newhome_renshu_lay);
        fragment_newhome_renshu_lay = ViewHolder.get(BaseView, R.id.fragment_newhome_renshu_lay);
        fragment_newhome_jifen_lay = ViewHolder.get(BaseView, R.id.fragment_newhome_jifen_lay);
        fragment_newhome_yongjin_lay = ViewHolder.get(BaseView, R.id.fragment_newhome_yongjin_lay);
        SetDownTitle(fragment_newhome_renshu_lay, getResources().getString(R.string.newhome_renshu));
        SetDownTitle(fragment_newhome_jifen_lay, getResources().getString(R.string.newhome_myjifen));
        SetDownTitle(fragment_newhome_yongjin_lay, getResources().getString(R.string.newhome_fanyong));
        Text_fragment_newhome_RenShu.setText("20");
        Text_fragment_newhome_JiFen.setText("4220");
        Text_fragment_newhome_YongJin.setText("42,10");
        View VVV = ViewHolder.get(BaseView, R.id.fragment_newhome_putitle_lay);
        fragmentNewhomeIvLaya = (LinearLayout) VVV.findViewById(R.id.fragment_newhome_iv_layaa);//ViewHolder.get(BaseView, R.id.fragment_newhome_iv_lay);
        try {
            fragmentNewhomeIvLaya.getBackground().setAlpha(0);
        } catch (Exception e) {
            Log.i("sss", e.toString());
            Log.i("sss", e.toString());
        }

        IWave();
        IBindData();
        //解决冲突
        fragmentNewhomeInsrollviw.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                fragmentNewhomeSrollviw.setEnabled(fragmentNewhomeInsrollviw.getScrollY() == 0);
            }
        });
    }

    /**
     * 设置下边的标题
     */
    private void SetDownTitle(View V, String Downtitle) {
        V.setOnClickListener(this);
        ((TextView) V.findViewById(R.id.comment_fragment_newhome_updatalay_downtxt)).setText(Downtitle);
        switch (V.getId()) {
            case R.id.fragment_newhome_renshu_lay:
                Text_fragment_newhome_RenShu = (TextView) V.findViewById(R.id.comment_fragment_newhome_updatalay_uptxt);
                break;
            case R.id.fragment_newhome_jifen_lay:
                Text_fragment_newhome_JiFen = (TextView) V.findViewById(R.id.comment_fragment_newhome_updatalay_uptxt);
                break;
            case R.id.fragment_newhome_yongjin_lay:
                Text_fragment_newhome_YongJin = (TextView) V.findViewById(R.id.comment_fragment_newhome_updatalay_uptxt);
                break;
        }
    }

    //设置banner
    private void InItPaGeView(List<String> data) {
        ArrayList<String> ssss = (ArrayList<String>) data;
        fragmentNewhomeBanner.setImageResources(ssss, ssss, new ImageCycleView.ImageCycleViewListener() {
                    @Override
                    public void displayImage(String imageURL, ImageView imageView) {
                        ImageLoaderUtil.Load2(imageURL, imageView, R.drawable.error_iv1);
                    }

                    @Override
                    public void onImageClick(int position, View imageView) {
                        List<String> mStrings = fragmentNewhomeBanner.getMyImageUrlList();
                        Intent mIntent = new Intent(BaseContext, AphotoPager.class);
                        mIntent.putExtra("position", position);
                        mIntent.putExtra("urls", StrUtils.LsToArray(mStrings));
                        PromptManager.SkipActivity(BaseActivity, mIntent);
                    }
                },
                screenWidth);
    }

    private void IBindData() {
        //开始加载头像
        File CoverFile = new File(ImagePathConfig.ShopCoverPath(BaseContext));
        ImageLoaderUtil.Load2(StrUtils.NullToStr(Spuit.Shop_Get(BaseContext).getAvatar()),
                fragmentNewhomeHeadIv, R.drawable.testiv);
        if (CoverFile.exists()) {// 已经存在了
            fragmentNewhomeBg.setImageBitmap(BitmapFactory
                    .decodeFile(ImagePathConfig.ShopCoverPath(BaseContext)));
        } else {
            ImageLoaderUtil.LoadGaosi(BaseContext,
                    StrUtils.NullToStr(Spuit.Shop_Get(BaseContext).getCover()), fragmentNewhomeBg,
                    R.color.app_fen, 1);
        }
        //加载结束
        List<String> Url = new ArrayList<String>();
        Url.add("http://img0.imgtn.bdimg.com/it/u=2153708940,56816496&fm=21&gp=0.jpg");
        InItPaGeView(Url);
    }

    private void IWave() {
        fragmentNewhomeWaveview.setBorder(0, getResources().getColor(R.color.transparent));
        mWaveHelper = new WaveHelper(fragmentNewhomeWaveview);
    }

    @Override
    public void onResume() {
        super.onResume();
        mWaveHelper.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mWaveHelper.cancel();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void InitCreate(Bundle d) {

    }

    @Override
    public void getResult(int Code, String Msg, BComment Data) {

    }

    @Override
    public void onError(String error, int LoadType) {

    }

    @OnClick({R.id.fragment_newhome_iv_sao, R.id.fragment_newhome_iv_sou, R.id.fragment_newhome_iv_new, R.id.fragment_newhome_head_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_newhome_iv_sao:
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
                        CaptureActivity.class));
                break;
            case R.id.fragment_newhome_iv_sou:
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity, ASouSouGood.class));
                break;
            case R.id.fragment_newhome_iv_new:
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        ANew.class));
                break;
            case R.id.fragment_newhome_head_iv:
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity, ANewHome.class));
                break;
            case R.id.fragment_newhome_renshu_lay:
                PromptManager.ShowCustomToast(BaseContext, "跳转人数");
                break;
            case R.id.fragment_newhome_jifen_lay:
                PromptManager.ShowCustomToast(BaseContext, "跳转积分");
                break;
            case R.id.fragment_newhome_yongjin_lay:
                PromptManager.ShowCustomToast(BaseContext, "跳转佣金");
                break;


        }
    }


    //    @Override
//    public void BeginFrash() {
//
//    }
//
    @Override
    public void onScroll(int scrollY) {
        if (scrollY < 100) {
            fragmentNewhomeIvLaya.getBackground().setAlpha(0);
//            PromptManager.ShowCustomToast(BaseContext, "数据" + scrollY);
//            back.getBackground().setAlpha(255);
//            shopping_cart.getBackground().setAlpha(255);
        } else if (scrollY >= 100 && scrollY < 860) {
            fragmentNewhomeIvLaya.getBackground().setAlpha((scrollY - 500));
//            PromptManager.ShowCustomToast(BaseContext, "数据" + scrollY);
//            back.getBackground().setAlpha(255 - (scrollY-100)/3);
//            shopping_cart.getBackground().setAlpha(255 - (scrollY-100)/3);
        } else {
            fragmentNewhomeIvLaya.getBackground().setAlpha(255);
//            back.getBackground().setAlpha(0);
//            shopping_cart.getBackground().setAlpha(0);
        }
    }
//    Scrollview和swip滑动冲突问题

    @Override
    public void onRefresh() {

    }


}

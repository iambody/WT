package io.vtown.WeiTangApp.fragment.main;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.comment.contant.ImagePathConfig;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.util.ui.WaveHelper;
import io.vtown.WeiTangApp.comment.view.CircleImageView;
import io.vtown.WeiTangApp.comment.view.WaveView;
import io.vtown.WeiTangApp.fragment.FBase;

/**
 * Created by datutu on 2016/10/9.
 */

public class FMainNewHome extends FBase {

    CircleImageView fragmentNewhomeHeadIv;
    private WaveHelper mWaveHelper;
    WaveView waveView;
    private int mBorderColor = Color.parseColor("#44FFFFFF");
    private int mBorderWidth = 1;
    ImageView fragment_newhome_bg;

    @Override
    public void InItView() {
        BaseView = LayoutInflater.from(BaseContext).inflate(R.layout.fragment_newmainhome, null);


        IBase();
    }

    private void IBase() {
        IWave();
        fragment_newhome_bg = (ImageView) BaseView.findViewById(R.id.fragment_newhome_bg);
        fragmentNewhomeHeadIv = (CircleImageView) BaseView.findViewById(R.id.fragment_newhome_head_iv);
        IBindData();
    }

    private void IBindData() {
        //开始加载头像
        File CoverFile = new File(ImagePathConfig.ShopCoverPath(BaseContext));
        ImageLoaderUtil.Load2(StrUtils.NullToStr(Spuit.Shop_Get(BaseContext).getAvatar()),
                fragmentNewhomeHeadIv, R.drawable.testiv);
        if (CoverFile.exists()) {// 已经存在了
            fragment_newhome_bg.setImageBitmap(BitmapFactory
                    .decodeFile(ImagePathConfig.ShopCoverPath(BaseContext)));
        } else {
            ImageLoaderUtil.LoadGaosi(BaseContext,
                    StrUtils.NullToStr(Spuit.Shop_Get(BaseContext).getCover()), fragment_newhome_bg,
                    R.color.app_fen, 1);

        }
    }

    private void IWave() {
        waveView = (WaveView) BaseView.findViewById(R.id.fragment_newhome_waveview);
        waveView.setBorder(mBorderWidth, getResources().getColor(R.color.transparent));
        mWaveHelper = new WaveHelper(waveView);
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.fragment_newhome_head_iv)
    public void onClick() {
    }
}

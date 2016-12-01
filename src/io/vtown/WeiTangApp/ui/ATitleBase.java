package io.vtown.WeiTangApp.ui;

import org.w3c.dom.Text;

import com.jauker.widget.BadgeView;

import de.greenrobot.event.EventBus;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.NetUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.event.ConnectivityReceiver.OnNetworkAvailableListener;
import io.vtown.WeiTangApp.event.interf.IHttpResult;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-4-11 下午2:38:37
 */
public abstract class ATitleBase extends ABase implements
        IHttpResult<BComment>, OnClickListener {
    protected Activity BaseActivity;
    protected Context BaseContext;
    /**
     *
     */
    protected View titlebase_lay;
    /**
     * title的左边返回按钮
     */
    protected ImageView lback;
    /**
     * 左边的文字
     */
    protected TextView left_txt;
    /**
     * 左边的向下按钮
     */
    protected ImageView arrow_down;
    /**
     * title的中间文本
     */
    protected TextView title;
    /**
     * title的中间布局lay
     */
    protected RelativeLayout arrow_txt_lay;

    /**
     * title的中间文本的颜色
     */
    protected int title_color = R.color.app_red;
    /**
     * title右边的view按钮
     */
    protected ImageView right_iv;
    /**
     * 最右边
     */
    protected ImageView right_right_iv;
    /**
     * title右边的文本
     */
    protected TextView right_txt;
    /**
     * title右边的文本的颜色
     */
    protected int right_txt_color = R.color.app_red;
    /**
     * 网络错误的标示箭头
     */
    protected View NetError;

    /**
     * oncreate里面 初始化view控件
     */
    protected abstract void InItBaseView();

    /**
     * oncreate里面 初始化view控件
     */
    protected abstract void InitTile();

    /**
     * 获取网络数据成功时候的接口
     */
    protected abstract void DataResult(int Code, String Msg, BComment Data);

    /**
     * 获取网络失败时候的接口
     */
    protected abstract void DataError(String error, int LoadType);

    /**
     * 网络连接时候需要的DO()
     */

    protected abstract void NetConnect();

    /**
     * 网络断开时候需要的DO()
     */

    protected abstract void NetDisConnect();

    /**
     * 初始化网络状态
     */
    protected abstract void SetNetView();

    /**
     * onclick点击的回掉接口
     */
    protected abstract void MyClick(View V);

    /**
     * oncreate里面 进行Bundle数据重置
     */
    protected abstract void InItBundle(Bundle bundle);

    /**
     * onSaveInstanceState 里面 进行Bundle 保存
     */
    protected abstract void SaveBundle(Bundle bundle);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (null != savedInstanceState) {
            InItBundle(savedInstanceState);
        }

        BaseActivity = this;
        BaseContext = this;
        InItBaseView();
        NetError = findViewById(R.id.neterrorview);
        InitTile();
        /**
         * 判断网络状态
         */
        BaseReceiver
                .setOnNetworkAvailableListener(new OnNetworkAvailableListener() {

                    @Override
                    public void onNetworkUnavailable() {
                        NetDisConnect();
                    }

                    @Override
                    public void onNetworkAvailable() {
                        NetConnect();

                    }
                });
        NetError.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                PromptManager.GoToNetSeting(BaseContext);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        SetNetView();
        try {
            BaseReceiver.bind(BaseContext);
        } catch (Exception e) {

        }
    }

    /**
     * 隐藏
     */
    protected void HindBackIv() {
        findViewById(R.id.lback).setVisibility(View.INVISIBLE);
    }

    protected void SetTiitleColor(int Coloe) {
        findViewById(R.id.title_layout).setBackgroundColor(Coloe);
    }

    /**
     * 设置标题文本
     */
    protected void SetTitleTxt(String Str) {
        title = (TextView) findViewById(R.id.title);
        title.setText(Str);
    }

    /**
     * 设置最右边的iv
     */
    protected void SetRightTxtIv(int Resource) {
        right_txt = (TextView) findViewById(R.id.right_txt);
        right_txt.setBackgroundDrawable(getResources().getDrawable(Resource));
        right_txt.setVisibility(View.VISIBLE);
    }

    protected void SetLeftText(String TextContent) {
        left_txt = (TextView) findViewById(R.id.left_txt);
        left_txt.setText(TextContent);
        left_txt.setVisibility(View.VISIBLE);
        findViewById(R.id.lback).setVisibility(View.GONE);

    }

    /**
     * 给右边的TextView设置文字
     *
     * @param TextContent
     */
    protected void SetRightText(String TextContent) {
        right_txt = (TextView) findViewById(R.id.right_txt);
        right_txt.setText(TextContent);
        right_txt.setVisibility(View.VISIBLE);
        right_txt.setTextColor(getResources().getColor(R.color.TextColorWhite));

    }

    /**
     * 设置最右边的iv(又在右边添加了一个隐藏的imagview)
     */
    protected void SetRightRightIv(int Resource) {
        right_right_iv = (ImageView) findViewById(R.id.right_right_iv);
        right_right_iv.setImageResource(Resource);
        right_right_iv.setVisibility(View.VISIBLE);
    }

    /**
     * 设置最右边的iv(又在右边添加了一个隐藏的imagview)
     */
    protected void SetRightRightIvINVISIBLE() {
        right_right_iv = (ImageView) findViewById(R.id.right_right_iv);

        right_right_iv.setVisibility(View.INVISIBLE);
    }

    /**
     * 设置最右边的iv
     */
    protected void SetRightIv(int Resource) {
        right_iv = (ImageView) findViewById(R.id.right_iv);
        right_iv.setImageResource(Resource);
        right_iv.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            BaseReceiver.unbind(BaseContext);
        } catch (Exception e) {

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_rigth_in, R.anim.push_rigth_out);
    }

    /**
     * 点击左侧按钮的监听事件
     */
    public void title_left_bt(View v) {
        finish();
        overridePendingTransition(R.anim.push_rigth_in, R.anim.push_rigth_out);
    }

    ;

    protected void SetNetStatuse(View v) {
        if (NetUtil.NETWORN_NONE == NetUtil.getNetworkState(BaseContext)) {
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.GONE);
        }
    }
    @Override
    public void getResult(int Code, String Msg, BComment Data) {
        DataResult(Code, Msg, Data);
    }

    @Override
    public void onError(String error, int LoadType) {
        DataError(error, LoadType);
    }

    @Override
    public void onClick(View arg0) {
        MyClick(arg0);
    }


    /**
     * 上边是IV下边是文字的布局
     *
     * @param title
     * @param IvRource
     * @param V
     */
    public void SetCommentIV(String title, int IvRource, View V) {
        ImageView viessw;

        ((ImageView) V.findViewById(R.id.comment_ivtxt_iv))
                .setBackgroundResource(IvRource);
        ((TextView) V.findViewById(R.id.comment_ivtxt_txt)).setText(title);
        V.setOnClickListener(this);

    }
}

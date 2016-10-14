package io.vtown.WeiTangApp.fragment;

import java.util.HashMap;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.net.NHttpBaseStr;
import io.vtown.WeiTangApp.comment.net.delet.NHttpDeletBaseStr;
import io.vtown.WeiTangApp.comment.util.NetUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog.onConfirmClick;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog.oncancelClick;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.event.interf.IHttpResult;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 所有fragment依托于activity所以不需要分别建立basetitle方法
 *
 * @author datutu
 */

public abstract class FBase extends Fragment implements IHttpResult<BComment> {
    protected static final int INITIALIZE = 0;// 初次进入时候
    protected static final int REFRESHING = 1;// 刷新
    protected static final int LOADMOREING = 2;// 加载更多
    protected static final int LOADHind = 3;// 偷偷加载
    protected static final int DELETE = 4;// 偷偷加载

    //在首页几个fragment中 断网时候需要进行再fragment里面的title进行显示断网箭头view
    protected View neterrorview;
    // 进入UI里面 展示动画的标识
    protected static final int NOVIEW_INITIALIZE = 10;// 初次进入时候
    protected static final int NOVIEW_RIGHT = 11;// 获取数据成功
    protected static final int NOVIEW_ERROR = 12;// 获取数据失败
    /**
     * 获取HTtp数后的接口 供给子类暴露接口
     */
    protected IHttpResult<BComment> mHttpDataLisenter;
    protected NHttpBaseStr mBaseStr;
    protected Context BaseContext;

    protected Activity BaseActivity;
    /**
     * vbaseview
     */
    protected View BaseView;

    /**
     * onCreateView
     */
    public abstract void InItView();

    /**
     * onCreateView
     */
    public abstract void InitCreate(Bundle d);

    protected int screenWidth;
    //fragment传递出去的包含在intent里面的bean
    protected String BaseKey_Bean = "abasebeankey";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseContext = getActivity();
        BaseActivity = getActivity();
        Bundle Mbundle = getArguments();
        if (Mbundle != null && Mbundle.containsKey("screenwidth")) {
            screenWidth = Mbundle.getInt("screenwidth");
        }

        InitCreate(getArguments());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        InItView();
        return BaseView;

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mBaseStr != null)
            mBaseStr.CancleNet();
    }

    //    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mBaseStr != null)
            mBaseStr.CancleNet();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBaseStr != null)
            mBaseStr.CancleNet();
    }

    /**
     * (maintab的几个页面)显示或者隐藏断网的View 1标识刚进来就无网络 2标识有网络变成无网络 3标识无网络变成有网络
     */
    public void SetNetStatuse(int Stattuse) {
        if (null == neterrorview) return;
        switch (Stattuse) {
            case 1:
                neterrorview.setVisibility(View.VISIBLE);
                break;
            case 2:
                neterrorview.setVisibility(View.VISIBLE);
                break;
            case 3:
                neterrorview.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 刚进来时候需要判断是否有网络
     */
    public void CheckNet() {
        if (null == neterrorview) return;
        neterrorview.setVisibility(NetUtil.isConnected(BaseContext) ? View.GONE : View.VISIBLE);
    }

    public void callGoodHandle() {

    }

    /**
     * 获取数据前 加载图片
     */
    protected void IDataView(View ShowLay, View ErrorView, int type) {

        switch (type) {
            case NOVIEW_INITIALIZE:// 初始化进来为获取数据的Error是不显示的 需要把ShowLay也不显示状态
                ShowLay.setVisibility(View.INVISIBLE);
                ErrorView.findViewById(R.id.iv_error).setVisibility(View.GONE);
                break;
            case NOVIEW_RIGHT:// 获取到数据就正确显示ShowLay；；隐藏ErrorView
                ErrorView.setVisibility(View.GONE);
                ShowLay.setVisibility(View.VISIBLE);
                break;
            case NOVIEW_ERROR:// 获取数据失败后就显示ErrorView并且可以点击；；；隐藏ErrorView
                ShowLay.setVisibility(View.GONE);
                ErrorView.setVisibility(View.VISIBLE);
                ErrorView.findViewById(R.id.iv_error).setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    /**
     * 定义一个Http回调接口公用获取数据的返回值
     */
    public void SetTitleHttpDataLisenter(IHttpResult<BComment> Lisenter) {
        this.mHttpDataLisenter = Lisenter;
    }

    /**
     * 获取Http数据的开启方法
     */
    public void FBGetHttpData(HashMap<String, String> Map, String Host,
                              int Method, final int Tage, final int LoadType) {
        if (!NetUtil.isConnected(BaseContext)) {//检查网络 TODO需要无网络时候显示错误头像
            PromptManager.ShowCustomToast(BaseContext, getResources().getString(R.string.network_not_connected));
            PromptManager.closeLoading();
            PromptManager.closetextLoading();
            mHttpDataLisenter.onError("网络断开", LoadType);
            return;
        }
        if (Method == com.android.volley.Request.Method.DELETE) {
            NHttpDeletBaseStr mBasedelStrs = new NHttpDeletBaseStr(BaseContext);
            mBasedelStrs.setPostResult(new IHttpResult<String>() {

                @Override
                public void onError(String error, int LoadTypea) {
                    mHttpDataLisenter.onError(error, LoadType);
                }

                @Override
                public void getResult(int Code, String Msg, String Data) {
                    if (Code != 200)
                        mHttpDataLisenter.onError(Msg, LoadType);

                    else
                        mHttpDataLisenter.getResult(Code, Msg, new BComment(
                                StrUtils.isEmpty(Data) ? "" : Data, Tage,
                                LoadType));
                }
            });
            mBasedelStrs.getData(Host, Map, Method);
        } else {
            if (mBaseStr == null)
                mBaseStr = new NHttpBaseStr(BaseContext);
            mBaseStr.setPostResult(new IHttpResult<String>() {
                @Override
                public void onError(String error, int LoadTyp) {
                    mHttpDataLisenter.onError(error, LoadType);
                }

                @Override
                public void getResult(int Code, String Msg, String Data) {
                    if (Code != 200)
                        mHttpDataLisenter.onError(Msg, LoadType);

                    else
                        mHttpDataLisenter.getResult(Code, Msg, new BComment(
                                StrUtils.isEmpty(Data) ? "" : Data, Tage,
                                LoadType));
                }
            });
            mBaseStr.getData(Host, Map, Method);
        }
    }

    /**
     * 左右选择弹出框的封装
     */

    public void ShowCustomDialog(String title, String Left, String Right,
                                 final IDialogResult mDialogResult) {
        final CustomDialog dialog = new CustomDialog(BaseContext,
                R.style.mystyle, R.layout.dialog_purchase_cancel, 1, Left,
                Right);
        dialog.show();
        dialog.setTitleText(title);
        dialog.HindTitle2();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setcancelListener(new oncancelClick() {

            @Override
            public void oncancelClick(View v) {
                dialog.dismiss();
                mDialogResult.LeftResult();
            }
        });

        dialog.setConfirmListener(new onConfirmClick() {
            @Override
            public void onConfirmCLick(View v) {
                dialog.dismiss();
                mDialogResult.RightResult();
            }
        });
    }

    /**
     * 优化list
     *
     * @param scrollListener
     * @return
     */
    public PauseOnScrollListener getPauseOnScrollListener(
            OnScrollListener scrollListener) {
        PauseOnScrollListener listener = new PauseOnScrollListener(
                ImageLoader.getInstance(), false, true, scrollListener);
        return listener;
    }

    /**
     * 部分点击前检查网络
     */
    public boolean CheckNet(Context mContext) {
        if (!NetUtil.isConnected(mContext)) {
            PromptManager.ShowCustomToast(mContext,
                    mContext.getString(R.string.check_net));
            return true;
        }
        return false;

    }

    /**
     * 设置错误文字信息
     *
     * @param ErrorTxt
     */
    protected void ShowErrorCanLoad(String ErrorTxt) {
        ((TextView) BaseView.findViewById(R.id.error_kong)).setText(ErrorTxt);
    }

    /**
     * 显示错误或者空的信息
     *
     * @param ResouceId
     */
    protected void ShowErrorIv(int ResouceId) {
        ((ImageView) BaseView.findViewById(R.id.iv_error)).setImageResource(ResouceId);
    }
}

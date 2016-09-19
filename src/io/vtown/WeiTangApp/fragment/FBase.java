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
    /**
     * 获取HTtp数后的接口 供给子类暴露接口
     */
    protected IHttpResult<BComment> mHttpDataLisenter;

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
    protected   String BaseKey_Bean = "abasebeankey";
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


    public void callGoodHandle() {

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
        if (Method == com.android.volley.Request.Method.DELETE) {
            NHttpDeletBaseStr mBaseStr = new NHttpDeletBaseStr(BaseContext);
            mBaseStr.setPostResult(new IHttpResult<String>() {

                @Override
                public void onError(String error, int LoadType) {
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
        } else {

            NHttpBaseStr mBaseStr = new NHttpBaseStr(BaseContext);
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

}

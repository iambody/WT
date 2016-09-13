/**
 *
 */
package io.vtown.WeiTangApp.comment.net;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcache.BShop;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.view.dialog.PromptCustomDialog;
import io.vtown.WeiTangApp.comment.view.dialog.PromptCustomDialog.onDialogConfirmClick;
import io.vtown.WeiTangApp.event.interf.ICustomDialogResult;
import io.vtown.WeiTangApp.event.interf.IHttpResult;
import io.vtown.WeiTangApp.ui.title.loginregist.ALogin;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.android.volley.VolleyError;

/**
 * @author 王永奎 E-mail:wangyk@nsecurities.cn
 * @version 创建时间：2015-11-6 下午2:03:07
 * @department 互联网金融部
 */

public class NHttpBaseStr extends NHttpBase {

    public NHttpBaseStr(Context context) {
        super(context);
    }

    private IHttpResult<String> postResult;

    public void setPostResult(IHttpResult<String> postresult) {
        this.postResult = postresult;
    }

    @Override
    public void myOnResponse(String str) {
        if (StrUtils.isEmpty(str)) {
            if (postResult != null)
                postResult.getResult(201, "未获得服务器信息请重试.....", str);
        } else {
            int Status = 0;
            int Code = 0;
            String Msg = null;
            String Data = "";
            try {
                JSONObject obj = new JSONObject(str);

                try {
                    Code = obj.getInt("code");
                    // 判断如果是900就直接被踢下来
                    if (Code == Constants.NetCodeExit) {
                        ShowPromptCustomDialog(context, "账号已在其他设备登录请重新登录");
                    }

                } catch (Exception e) {
                    if (postResult != null)
                    postResult.onError(context.getResources().getString(R.string.error_fuwuqi), 0);
                    return;
                }
                try {
                    Msg = obj.getString("msg");
                } catch (Exception e) {
                    if (postResult != null)
                    postResult.onError(context.getResources().getString(R.string.error_fuwuqi), 0);
                    return;
                }

                try {
                    Data = obj.getString("data");
                } catch (Exception e) {
                    if (postResult != null)
                    postResult.onError(context.getResources().getString(R.string.error_fuwuqi), 0);
                    return;
                }

            } catch (Exception e) {
                if (postResult != null)
                    postResult.onError(context.getResources().getString(R.string.error_fuwuqi), 0);
                return;
            }

            if (postResult != null)
                postResult.getResult(Code, Msg, Data);
        }
    }

    @Override
    public void myonErrorResponse(VolleyError arg0) {
            if (postResult != null)
                postResult.onError(context.getResources().getString(R.string.error_fuwuqi), 0);


    }

    public void ShowPromptCustomDialog(final Context mContext, String content) {
        final PromptCustomDialog promptCustomDialog = new PromptCustomDialog(
                mContext, R.style.mystyle, content);
        promptCustomDialog.show();
        promptCustomDialog.setCanceledOnTouchOutside(false);
        promptCustomDialog.setConfirmListener(new onDialogConfirmClick() {

            @Override
            public void onConfirmCLick(View v) {
                Spuit.Login_Out(mContext);
                // 清理数据库
                Spuit.Shop_Save(mContext, new BShop());
                // PromptManager.ShowCustomToast(mContext, "退出成功");
                PromptManager.SkipActivity((Activity) mContext, new Intent(
                        mContext, ALogin.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

            }
        });
    }

}

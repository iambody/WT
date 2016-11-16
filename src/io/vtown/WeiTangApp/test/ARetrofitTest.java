package io.vtown.WeiTangApp.test;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.net.RetrofitNetWorks;
import io.vtown.WeiTangApp.ui.ABase;
import rx.Observer;

/**
 * Created by datutu on 2016/11/14.
 */

public class ARetrofitTest extends ABase {
    @BindView(R.id.retrofit_test_bt)
    Button retrofitTestBt;
    @BindView(R.id.retrofit_test_txt)
    TextView retrofitTestTxt;


    private BUser MyUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofittest);
        ButterKnife.bind(this);
        MyUser = Spuit.User_Get(this);
    }

    @OnClick(R.id.retrofit_test_bt)
    public void onClick() {
        RetrofitNetWorks.GetHome(MyUser.getMember_id(), MyUser.getSeller_id(), new Observer<String>() {
            @Override
            public void onCompleted() {
                PromptManager.ShowCustomToast(BaseContext, "完成");
            }

            @Override
            public void onError(Throwable e) {
//                PromptManager.ShowCustomToast(BaseContext, e.toString());
                retrofitTestTxt.setText(e.toString());
            }

            @Override
            public void onNext(String s) {
//                PromptManager.ShowCustomToast(BaseContext, s);
                retrofitTestTxt.setText(s );
            }
        });
    }
}

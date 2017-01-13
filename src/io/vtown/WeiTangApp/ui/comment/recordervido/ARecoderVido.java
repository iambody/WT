package io.vtown.WeiTangApp.ui.comment.recordervido;

import java.io.IOException;

import de.greenrobot.event.EventBus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.LogUtils;
import io.vtown.WeiTangApp.ui.ABase;
import io.vtown.WeiTangApp.ui.comment.recordervido.MovieRecorderView.OnRecordFinishListener;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-7-3 下午12:37:35
 */
public class ARecoderVido extends ABase {
    private MovieRecorderView mRecorderView;// 视频录制控件
    private Button mShootBtn;// 视频开始录制按钮
    private boolean isFinish = true;
    private boolean success = false;// 防止录制完成后出现多次跳转事件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recordvido);
        mRecorderView = (MovieRecorderView) findViewById(R.id.movieRecorderView);
        mShootBtn = (Button) findViewById(R.id.shoot_button);

        // 用户长按事件监听
        mShootBtn.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                    try {
                        mRecorderView.stop();
                    } catch (Exception e) {
                    }
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {// 用户按下拍摄按钮
                    mShootBtn
                            .setBackgroundResource(R.drawable.bg_movie_add_shoot_select);
                    mRecorderView.record(new OnRecordFinishListener() {
                        @Override
                        public void onRecordFinish() {
                            if (!success && mRecorderView.getTimeCount() < 10) {// 判断用户按下时间是否大于10秒
                            LogUtils.i("************** ACTION_DOWN  mTimeCount **************" + mRecorderView.getTimeCount());
                                success = true;
                                handler.sendEmptyMessage(1);
                            }
                        }
                    });
                } else if (event.getAction() == MotionEvent.ACTION_UP) {// 用户抬起拍摄按钮
                    mShootBtn
                            .setBackgroundResource(R.drawable.bg_movie_add_shoot);
                    if (mRecorderView.getTimeCount() > 3) {// 判断用户按下时间是否大于3秒

                        LogUtils.i("************** ACTION_UP  mTimeCount **************" + mRecorderView.getTimeCount());
                        if (!success) {
                            success = true;
                            handler.sendEmptyMessage(1);
                        }
                    } else {
                        success = false;
                        if (mRecorderView.getmVecordFile() != null)
                            mRecorderView.getmVecordFile().delete();// 删除录制的过短视频

                        mRecorderView.stop();// 停止录制

                        try {
                            mRecorderView.initCamera();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(BaseActivity, "视频录制时间太短",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        isFinish = true;
        if (mRecorderView.getmVecordFile() != null)
            mRecorderView.getmVecordFile().delete();// 视频使用后删除
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        isFinish = false;
        success = false;
        mRecorderView.stop();// 停止录制
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (success) {
                finishActivity1();
            }
        }
    };

    // 视频录制结束后，跳转的函数
    private void finishActivity1() {
        if (isFinish) {

            mRecorderView.stop();


//			 Intent intent = new Intent(this, ARecoderSuccess.class);
//			 Bundle bundle = new Bundle();
//			 bundle.putString("text",
//			 mRecorderView.getmVecordFile().toString());
//			 intent.putExtras(bundle);
//			 startActivity(intent);
            BMessage event = new BMessage(290);
            event.setReCordVidoPath(mRecorderView.getmVecordFile().toString());
            EventBus.getDefault().post(event);
            BaseActivity.finish();
        }
        success = false;
    }

    /**
     * 录制完成回调
     */
    public interface OnShootCompletionListener {
        public void OnShootSuccess(String path, int second);

        public void OnShootFailure();
    }


}

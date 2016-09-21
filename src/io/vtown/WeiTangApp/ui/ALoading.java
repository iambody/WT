package io.vtown.WeiTangApp.ui;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.ui.title.loginregist.ALogin;
import io.vtown.WeiTangApp.ui.ui.ALoadAd;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-6-22 上午11:58:00
 * @see 进入app加载页面
 */
public class ALoading extends ABase implements OnPageChangeListener,
        OnTouchListener {
    private ViewPagerAdapter adapter;
    private ViewPager Homepager;
    private List<View> views;// 视图数据
    private int[] imageVeiwResourceId = {R.drawable.a, R.drawable.b
    };// 显示图片的数据
    private ImageView[] point;// 底部小圆点
    private int currentId = 0;// 当前ID
    private int lastX = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MakeLogion();
        setContentView(R.layout.activity_load);
        initView();
        setPoint();// 第一次设置小圆点位置
    }

    private void MakeLogion() {
        if (Spuit.Frist_IsFrist(BaseActivity))
            // if (true)
            Spuit.Frist_Set(BaseActivity);
        else
        // 跳转界面
        {
            GoLogion();
            return;
        }
    }

    /**
     * 初始化view
     */
    private void initView() {
        Homepager = (ViewPager) findViewById(R.id.wtload_vp);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        views = new ArrayList<View>();
        for (int i = 0; i < imageVeiwResourceId.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(imageVeiwResourceId[i]);
            imageView.setScaleType(ScaleType.FIT_XY);
            views.add(imageView);
        }
        View view = inflater.inflate(R.layout.view_last_guide, null);
        views.add(view);
        Homepager.setOnTouchListener(this);
        adapter = new ViewPagerAdapter(views);
        Homepager.setAdapter(adapter);
        Homepager.setOnPageChangeListener(this);
    }

    /**
     * 设置小圆点
     */
    private void setPoint() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.viewpager_ll);
        point = new ImageView[ll.getChildCount()];
        for (int i = 0; i < ll.getChildCount(); i++) {
            if (currentId == i) {
                point[i] = (ImageView) ll.getChildAt(i);
                point[i].setImageResource(R.drawable.point_focus11);
            } else {
                point[i] = (ImageView) ll.getChildAt(i);
                point[i].setImageResource(R.drawable.point_normal);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if ((lastX - event.getX()) > 50 && (currentId == views.size() - 1)) {
                    PromptManager.SkipActivity(BaseActivity, new Intent(
                            BaseActivity, ALogin.class));
                    this.finish();
                }
                break;
            default:
                break;
        }
        return false;

    }

    private void GoLogion() {

        PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                ALoadAd.class));

        ALoading.this.finish();
    }

    class ViewPagerAdapter extends PagerAdapter {
        private static final String TAG = "AdvertAdapter";
        private List<View> data;

        public ViewPagerAdapter(List<View> data) {
            this.data = data;

        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(data.get(position));
            if (data.size() - 1 == position) {// 判断导航页是不是最后一页
                Button submit = (Button) container
                        .findViewById(R.id.guide_start_app);
                submit.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PromptManager.SkipActivity(BaseActivity, new Intent(
                                BaseActivity, ALogin.class));
                        BaseActivity.finish();
                    }
                });

            }
            return data.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int arg0) {

        currentId = arg0;
        setPoint();
    }
}

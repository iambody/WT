package io.vtown.WeiTangApp.ui.comment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.util.List;

import io.vtown.WeiTangApp.BaseApplication;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.easy.PicImageItem;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.SdCardUtils;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.photoview.PhotoView;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.ui.ABase;

/**
 * Created by datutu on 2016/9/10.
 */
public class ALoactePhotoPager extends ABase {

    private int pic_position;

    private ViewPager mViewPager;
    private List<PicImageItem>urls;
    private TextView indicator;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Init();
        IListener();
        setAdapter();
    }

    protected void Init() {
        setContentView(R.layout.activity_photopager);
        pic_position = getIntent().getIntExtra("position", 0);
        urls =  BaseApplication.GetInstance().getPicImages();
        try {
            mViewPager = (ViewPager) findViewById(R.id.photo_view_pager);
            indicator = (TextView) findViewById(R.id.indicator);
            loading = (ProgressBar) findViewById(R.id.photo_loading);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ((urls == null) || (urls != null && urls.size() == 1)) {
            indicator.setVisibility(View.GONE);
        } else {
            indicator.setVisibility(View.VISIBLE);
        }
    }

    protected void IListener() {

        try {
            mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int arg0) {
                    CharSequence text = getString(R.string.viewpager_indicator,
                            arg0 + 1, mViewPager.getAdapter().getCount());
                    indicator.setText(text);
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                }

                @Override
                public void onPageScrollStateChanged(int arg0) {

                }
            });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void setAdapter() {
        // TODO Auto-generated method stub
        try {
            mViewPager.setAdapter(new SamplePagerAdapter());
            mViewPager.setCurrentItem(pic_position);// 图片初始化的位置

            CharSequence text = getString(R.string.viewpager_indicator,
                    pic_position + 1, mViewPager.getAdapter().getCount());
            indicator.setText(text);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    class SamplePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return urls.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, final int position) {

            final PhotoView photoView = new PhotoView(container.getContext());
            android.support.v4.view.ViewPager.LayoutParams params = new android.support.v4.view.ViewPager.LayoutParams();
            params.height = android.support.v4.view.ViewPager.LayoutParams.MATCH_PARENT;
            params.width = android.support.v4.view.ViewPager.LayoutParams.MATCH_PARENT;
            photoView.setLayoutParams(params);

            // imageLoader.displayImage(urls.get(position), photoView);
            // amelieApplication.mImageLoader.get(urls[position],
            // amelieApplication.getImageListener(photoView,
            // R.drawable.card_default_award_bg));
            if(!StrUtils.isEmpty(urls.get(position).getPathurl())){
                photoView.setImageBitmap(StrUtils.GetBitMapFromPath(urls.get(position).getPathurl()));
            }else
            ImageLoaderUtil.LoadBigListener(urls.get(position).getWeburl(), photoView,
                    new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                            if (imageUri == null)
                                return;

                            File file = ImageLoader.getInstance().getDiskCache().get(imageUri);
                            if (null == file || !file.exists()) {
                                if (urls.get(position).getWeburl().equals(imageUri))
                                    loading.setVisibility(View.VISIBLE);
                                else
                                    loading.setVisibility(View.GONE);
                            }

                            if (imageUri == null)
                                return;

                            // if (file == null || !file.exists()) {
                            // loading.setVisibility(View.VISIBLE);
                            // }
                            //
                            // if (!file.exists()) {
                            // bfb.setVisibility(View.VISIBLE);
                            // loading.setVisibility(View.VISIBLE);
                            // }

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view,
                                                    FailReason failReason) {
                            loading.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri,
                                                      View view, final Bitmap loadedImage) {
                            loading.setVisibility(View.GONE);

                            if (imageUri == null)
                                return;

                            FadeInBitmapDisplayer.animate(view, 800);
                            photoView
                                    .setOnLongClickListener(new View.OnLongClickListener() {

                                        @Override
                                        public boolean onLongClick(View v) {
                                            ShowCustomDialog("是否保存图片", "取消",
                                                    "保存", new IDialogResult() {

                                                        @Override
                                                        public void RightResult() {
                                                            SdCardUtils
                                                                    .saveImageToGallery(
                                                                            BaseContext,
                                                                            loadedImage);
                                                            PromptManager
                                                                    .ShowCustomToast(
                                                                            BaseContext,
                                                                            "已保存");
                                                        }

                                                        @Override
                                                        public void LeftResult() {

                                                        }
                                                    });

                                            return true;
                                        }
                                    });
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri,
                                                       View view) {
                            loading.setVisibility(View.GONE);
                        }
                    });

            // photoView.setImageResource(sDrawables[position]);

            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        urls = null;
    }

}
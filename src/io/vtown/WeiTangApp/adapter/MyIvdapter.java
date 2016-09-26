package io.vtown.WeiTangApp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;

/**
 * Created by datutu on 2016/9/26.
 */

public class MyIvdapter extends BaseAdapter {
    private List<String> datas;
    private LayoutInflater iLayoutInflater;
    private Context MycContext;

    public MyIvdapter(Context Acontext, List<String> datas) {
        super();
        this.datas = datas;
        this.MycContext = Acontext;
        this.iLayoutInflater = LayoutInflater.from(MycContext);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int arg0) {
        return datas.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(final int arg0, View arg1, ViewGroup arg2) {
        MyIvImageItem imageItem = null;
        if (null == arg1) {
            arg1 = iLayoutInflater.inflate(R.layout.item_show_in_imagview,
                    null);
            imageItem = new MyIvImageItem();
            imageItem.item_show_in_imagview = ViewHolder.get(arg1,
                    R.id.item_show_in_imagview);
            arg1.setTag(imageItem);
        } else {
            imageItem = (MyIvImageItem) arg1.getTag();
        }
        String tag = (String) imageItem.item_show_in_imagview.getTag();
        if (tag == null || !tag.equals(datas.get(arg0))) {
            ImageLoader.getInstance().displayImage(
                    datas.get(arg0),
                    new ImageViewAware(imageItem.item_show_in_imagview, false), ImageLoaderUtil.GetDisplayOptions(R.drawable.error_iv2),
                    new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String s, View view,
                                                    FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String s, View view,
                                                      Bitmap bitmap) {
                            view.setTag(datas.get(arg0));// 确保下载完成再打tag.
                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {

                        }

                    });
        }


        return arg1;
    }

    class MyIvImageItem {
        ImageView item_show_in_imagview;
    }


}

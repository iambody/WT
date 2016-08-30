package io.vtown.WeiTangApp.comment.util.image;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.ImagePathConfig;
import io.vtown.WeiTangApp.comment.util.SdCardUtils;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.event.interf.IPathImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ImageLoaderUtil {
    /**
     * 暴露的回掉接口
     */
    public static IPathImage myPathImage;
    public static ImageLoadingListenerImpl mImageLoadingListenerImpl = new ImageLoadingListenerImpl();

    public void GetPath(IPathImage IvPath) {
        this.myPathImage = IvPath;
    }

    /**
     * 加载普通图片（加载本地图片时候需要前面添加file:///）
     *
     * @param imgUrl
     * @param imageView
     * @param defaultImg String imageUri = "http://site.com/image.png"; // from Web
     *                   String imageUri = "file:///mnt/sdcard/image.png"; //fromSDcard
     *                   String imageUri =
     *                   "content://media/external/audio/albumart/13"; // from content
     *                   provider String imageUri = "assets://image.png"; // from
     *                   assets String imageUri = "drawable://" + R.drawable.image; //
     *                   from drawables (only images, non-9patch)
     */
    public static void Load(String imgUrl, ImageView imageView, int defaultImg) {
        if (imgUrl.contains(Constants.PicHost)
                || imgUrl.contains("storage/emulated"))
            imgUrl = "file:///" + imgUrl;
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(defaultImg)
                .showImageForEmptyUri(defaultImg).showImageOnFail(defaultImg)
                .resetViewBeforeLoading(true).cacheInMemory(false)
                .cacheOnDisk(true).considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(100))

                .bitmapConfig(Bitmap.Config.ARGB_8888).build();
        ImageLoader.getInstance().displayImage(imgUrl, imageView, options);
    }

    public static void Load2(String imgUrl, final ImageView imageView,
                             int defaultImg) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(defaultImg)
                .considerExifParams(true)
                .showImageForEmptyUri(defaultImg).showImageOnFail(defaultImg)
                .cacheOnDisc(true).cacheInMemory(true)
                .cacheOnDisk(true)
                // .considerExifParams(true)
                // .displayer(new FadeInBitmapDisplayer(100))
                .displayer(new SimpleBitmapDisplayer())
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(imgUrl, imageView, options);

    }

    public static void Load22(String imgUrl, final ImageView imageView,
                              int defaultImg) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showStubImage(defaultImg)
                .showImageForEmptyUri(defaultImg)
                .showImageOnFail(defaultImg)
                .cacheInMemory(true)
                .cacheOnDisc(true)
//				.displayer(new SimpleBitmapDisplayer())
//				.imageScaleType(ImageScaleType.NONE)
                .bitmapConfig(Bitmap.Config.RGB_565)//设置为RGB565比起默认的ARGB_8888要节省大量的内存
                .delayBeforeLoading(100)//载入图片前稍做延时可以提高整体滑动的流畅度
                .build();
        ImageAware imageAware = new ImageViewAware(imageView, false);
        ImageLoader.getInstance().displayImage(imgUrl, imageAware, options);
//		ImageLoader.getInstance().displayImage(imgUrl, imageView, options);

    }

    public static DisplayImageOptions GetDisplayOptions(int Iv) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(Iv)
                .showImageForEmptyUri(Iv)
                .showImageOnFail(Iv)
                .cacheOnDisc(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                // .considerExifParams(true)
                // .displayer(new FadeInBitmapDisplayer(100))
                // .imageScaleType(ImageScaleType.NONE)
                .displayer(new SimpleBitmapDisplayer())
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        return options;
    }

    public static void Load3(String imgUrl, final ImageView imageView,
                             int defaultImg,
                             SimpleImageLoadingListener simpleImageLoadingListener) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnFail(defaultImg).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(100))
                .displayer(new SimpleBitmapDisplayer())
                .bitmapConfig(Bitmap.Config.ARGB_8888).build();
        ImageLoader.getInstance().loadImage(imgUrl, options,
                simpleImageLoadingListener);

    }

    public static class ImageLoadingListenerImpl extends
            SimpleImageLoadingListener {

        public static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
            if (bitmap != null) {
                ImageView imageView = (ImageView) view;
                boolean isFirstDisplay = !displayedImages.contains(imageUri);
                if (isFirstDisplay) {
                    // 图片的淡入效果
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);

                }
            }
        }
    }

    /**
     * 加载大图用
     *
     * @param imgUrl
     * @param imageView
     * @param loadListener
     */
    public static void LoadBigListener(String imgUrl, ImageView imageView,
                                       ImageLoadingListener loadListener) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.error_iv2)
                .showImageForEmptyUri(R.drawable.error_iv2)
                .resetViewBeforeLoading(true).cacheOnDisk(true)
                .cacheInMemory(false).considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(imgUrl, imageView, options,
                loadListener);
    }

    public static void LoadLongPic(String imgUrl, final ImageView imageView,
                                   int defaultImg) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(defaultImg)
                .showImageForEmptyUri(defaultImg)
                .showImageOnFail(defaultImg)
                .cacheOnDisc(true)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                // .considerExifParams(true)
                // .displayer(new FadeInBitmapDisplayer(100))
                .imageScaleType(ImageScaleType.NONE)
                .displayer(new SimpleBitmapDisplayer())
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        // String tag = (String) holder.mIvContent.getTag();
        // if (tag==null||!tag.equals(imageInfo.getUrl())) {
        // imageLoader.displayImage(imageInfo.getUrl(), holder.mIvContent,
        // mOptions, new ImageLoadingListener() {
        // @Override
        // public void onLoadingStarted(String s, View view) {
        //
        // }
        //
        // @Override
        // public void onLoadingFailed(String s, View view, FailReason
        // failReason) {
        //
        // }
        //
        // @Override
        // public void onLoadingComplete(String s, View view, Bitmap bitmap) {
        // holder.mIvContent.setTag(imageInfo.getUrl());//确保下载完成再打tag.
        // }
        //
        // @Override
        // public void onLoadingCancelled(String s, View view) {
        //
        // }
        // });
        // }
        //
        ImageLoader.getInstance().displayImage(imgUrl, imageView, options);

        // DisplayImageOptions options = new DisplayImageOptions.Builder()
        // .showImageOnFail(R.drawable.error_iv2)
        // .showImageForEmptyUri(R.drawable.error_iv2)
        // .resetViewBeforeLoading(true).cacheOnDisk(true)
        // .cacheInMemory(false).considerExifParams(true)
        // .displayer(new SimpleBitmapDisplayer())
        // .imageScaleType(ImageScaleType.EXACTLY)
        // .bitmapConfig(Bitmap.Config.RGB_565).build();
        // ImageLoader.getInstance().displayImage(imgUrl, imageView, options);
    }

    /**
     * @param pContext
     * @param imgUrl
     * @param imageView
     * @param defaultImg
     * @param Type       ==========>0标识的是不需要保存；1标识的是shop的高斯图片 ；；2标识的是center的高斯图片
     */
    public static void LoadGaosi(final Context pContext, String imgUrl,
                                 final ImageView imageView, int defaultImg, final int Type) {

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.error_iv2)
                .showImageForEmptyUri(R.drawable.error_iv2)
                .resetViewBeforeLoading(true).cacheOnDisk(true)
                .cacheInMemory(false).considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoader.getInstance().loadImage(imgUrl, options,
                new ImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {
                        final Bitmap mBitmap = StrUtils
                                .drawableToBitmap(pContext.getResources()
                                        .getDrawable(R.drawable.error_iv1));
                        BitmapBlurUtil.addTask(mBitmap, new Handler() {

                            @Override
                            public void handleMessage(Message msg) {

                                super.handleMessage(msg);

                                Drawable drawable = (Drawable) msg.obj;

                                imageView.setImageDrawable(drawable);

                                mBitmap.recycle();

                            }

                        });

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  final Bitmap loadedImage) {

                        if (loadedImage != null) {
                            // 模糊处理
                            BitmapBlurUtil.addTask(loadedImage, new Handler() {

                                @Override
                                public void handleMessage(Message msg) {

                                    super.handleMessage(msg);

                                    Drawable drawable = (Drawable) msg.obj;

                                    imageView.setImageDrawable(drawable);
                                    if (1 == Type) {// 店铺的高斯保存
                                        SdCardUtils
                                                .drawableTofile(
                                                        drawable,
                                                        ImagePathConfig
                                                                .ShopCoverPath(pContext));
                                    }
                                    if (2 == Type) {// center的高斯保存
                                        SdCardUtils
                                                .drawableTofile(
                                                        drawable,
                                                        ImagePathConfig
                                                                .CenterCoverPath(pContext));
                                    }

                                    loadedImage.recycle();

                                }

                            });

                        }

                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        final Bitmap mBitmap = StrUtils
                                .drawableToBitmap(pContext.getResources()
                                        .getDrawable(R.drawable.error_iv1));
                        BitmapBlurUtil.addTask(mBitmap, new Handler() {

                            @Override
                            public void handleMessage(Message msg) {

                                super.handleMessage(msg);

                                Drawable drawable = (Drawable) msg.obj;

                                imageView.setImageDrawable(drawable);

                                mBitmap.recycle();

                            }

                        });

                    }

                });

    }

}

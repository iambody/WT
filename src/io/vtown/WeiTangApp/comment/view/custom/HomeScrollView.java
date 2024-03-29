package io.vtown.WeiTangApp.comment.view.custom;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by datutu on 2016/10/10.
 */

public class HomeScrollView extends ScrollView {
    private OnScrollListener onScrollListener;
    /**
     * 主要是用在用户手指离开MyScrollView，MyScrollView还在继续滑动，我们用来保存Y的距离，然后做比较
     */
    private int lastScrollY;

    public HomeScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public HomeScrollView(Context context) {
        super(context);
    }

    public HomeScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置滚动接口
     * @param onScrollListener
     */
    public void setScrolListener(OnScrollListener onScrollListener){
        this.onScrollListener = onScrollListener;
    }

    /**
     * 用于用户手指离开MyScrollView的时候获取MyScrollView滚动的Y距离，然后回调给onScroll方法中
     */
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int scrollY = HomeScrollView.this.getScrollY();
            if(onScrollListener != null){
                onScrollListener.onScroll(scrollY);
            }
            //此时的距离和记录下的距离不相等，在隔5毫秒给handler发送消息
            if(lastScrollY != scrollY){
                lastScrollY = scrollY;
                handler.sendMessageDelayed(handler.obtainMessage(), 5);
            }

        }
    };

    /**
     * 重写onTouchEvent， 当用户的手在MyScrollView上面的时候，
     * 直接将MyScrollView滑动的Y方向距离回调给onScroll方法中，当用户抬起手的时候，
     * MyScrollView可能还在滑动，所以当用户抬起手我们隔20毫秒给handler发送消息，在handler处理
     * MyScrollView滑动的距离
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(onScrollListener != null){
            onScrollListener.onScroll(lastScrollY = this.getScrollY());
        }
        if(ev.getAction() == MotionEvent.ACTION_UP){
            handler.sendMessageDelayed(handler.obtainMessage(), 20);
        }
        return super.onTouchEvent(ev);
    }

    public interface OnScrollListener{
        /**
         * 回调方法， 返回MyScrollView滑动的Y方向距离
         * @param scrollY
         *              、
         */
        public void onScroll(int scrollY);
    }
}

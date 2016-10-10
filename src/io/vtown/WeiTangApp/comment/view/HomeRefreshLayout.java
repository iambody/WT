package io.vtown.WeiTangApp.comment.view;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.vtown.WeiTangApp.R;

/**
 * Created by datutu on 2016/10/10.
 */

public class HomeRefreshLayout extends SwipeRefreshLayout implements AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {

    /**
     * 主要是用在用户手指离开MyScrollView，MyScrollView还在继续滑动，我们用来保存Y的距离，然后做比较
     */
    private int lastScrollY;
    /**
     * 滑动到最下面时的上拉操作
     */

    private int mTouchSlop;


    /**
     * 监听
     */
    private HomeOnLoadListener MYHomeOnLoadListener;


    /**
     * 按下时的y坐标
     */
    private int mYDown;
    /**
     * 抬起时的y坐标, 与mYDown一起用于滑动到底部时判断是上拉还是下拉
     */
    private int mLastY;
    /**
     * 是否在加载中 ( 上拉加载更多 )
     */
    private boolean isLoading = false;
    private boolean IsCanLoadMore = true;

    public boolean isCanLoadMore() {
        return IsCanLoadMore;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        IsCanLoadMore = canLoadMore;
    }

    /**
     * @param context
     */
    public HomeRefreshLayout(Context context) {
        this(context, null);
    }


    public HomeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setOnRefreshListener(this);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    /**
     * 用于用户手指离开MyScrollView的时候获取MyScrollView滚动的Y距离，然后回调给onScroll方法中
     */
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int scrollY = HomeRefreshLayout.this.getScrollY();
            if(MYHomeOnLoadListener != null){
                MYHomeOnLoadListener.onScroll(scrollY);
            }
            //此时的距离和记录下的距离不相等，在隔5毫秒给handler发送消息
            if(lastScrollY != scrollY){
                lastScrollY = scrollY;
                handler.sendMessageDelayed(handler.obtainMessage(), 5);
            }

        }
    };
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 按下
                mYDown = (int) event.getRawY();
//                handler.sendMessageDelayed(handler.obtainMessage(), 20);
                break;

            case MotionEvent.ACTION_MOVE:
                // 移动
                mLastY = (int) event.getRawY();
                lastScrollY = (int)  event.getRawY();
                handler.sendMessageDelayed(handler.obtainMessage(), 20);
                break;

            case MotionEvent.ACTION_UP:

                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }


    /**
     * 是否是上拉操作
     *
     * @return
     */
    private boolean isPullUp() {
        return (mYDown - mLastY) >= mTouchSlop;
    }


    /**
     * @param loadListener
     */
    public void setHomeOnLoadListener(HomeOnLoadListener loadListener) {
        MYHomeOnLoadListener = loadListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // 滚动时到了最底部也可以加载更多

    }

    /**
     * 设置刷新
     */
    public static void setRefreshing(SwipeRefreshLayout refreshLayout,
                                     boolean refreshing, boolean notify) {
        Class<? extends SwipeRefreshLayout> refreshLayoutClass = refreshLayout
                .getClass();
        if (refreshLayoutClass != null) {
            try {
                Method setRefreshing = refreshLayoutClass.getDeclaredMethod(
                        "setRefreshing", boolean.class, boolean.class);
                setRefreshing.setAccessible(true);
                setRefreshing.invoke(refreshLayout, refreshing, notify);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRefresh() {

        MYHomeOnLoadListener.BeginFrash();
    }

    /**
     * 加载更多的监听器
     */
    public interface HomeOnLoadListener {
        //开始下拉刷新
        public void BeginFrash();

        public void onScroll(int scrollY);
    }


}

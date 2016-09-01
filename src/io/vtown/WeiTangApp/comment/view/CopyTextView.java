package io.vtown.WeiTangApp.comment.view;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import io.vtown.WeiTangApp.R;

/**
 * Created by datutu on 2016/9/1.
 */
public class CopyTextView  extends TextView{

    private Context mContext;
    private PopupWindow mPopupWindow;

    public CopyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setBackgroundColor(getResources().getColor(R.color.app_gray1));
                getPopupWindowsInstance();
                mPopupWindow.showAsDropDown(CopyTextView.this,getWidth()/2-mPopupWindow.getWidth()/2,0);
                return true;
            }
        });
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//
//                setBackgroundColor(getResources().getColor(R.color.app_gray1));
//                getPopupWindowsInstance();
//                mPopupWindow.showAsDropDown(this,getWidth()/2-mPopupWindow.getWidth()/2,0);
//                break;
//        }
//        return super.onTouchEvent(event);
//    }


//    @Override
//    public void setOnLongClickListener(OnLongClickListener l) {
//        setBackgroundColor(getResources().getColor(R.color.app_gray1));
//        getPopupWindowsInstance();
//        mPopupWindow.showAsDropDown(this,getWidth()/2-mPopupWindow.getWidth()/2,0);
//        super.setOnLongClickListener(l);
//    }

    private void dismissPopupWindowInstance(){
        if (null != mPopupWindow) {
            mPopupWindow.dismiss();
        }
    }
    private void getPopupWindowsInstance(){
        if(mPopupWindow!=null){
            mPopupWindow.dismiss();
        }else{
            initPopuptWindow();
        }
    }
    private void initPopuptWindow() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View popupWindow = layoutInflater.inflate(R.layout.pop_copy_textview, null);
        Button btnCopy = (Button) popupWindow.findViewById(R.id.pop_btnCopy);
        btnCopy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextColor(android.graphics.Color.BLACK);
                dismissPopupWindowInstance();
                ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(getText());
            }
        });
        mPopupWindow = new PopupWindow(popupWindow, dipTopx(mContext, 50), ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(getDrawable());
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setTextColor(android.graphics.Color.BLACK);
                setBackgroundColor(getResources().getColor(R.color.transparent));
            }
        });
    }
    private Drawable getDrawable(){
        ShapeDrawable bgdrawable =new ShapeDrawable(new OvalShape());
        bgdrawable.getPaint().setColor(getResources().getColor(android.R.color.transparent));
        return   bgdrawable;
    }
    public static int dipTopx(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }

}

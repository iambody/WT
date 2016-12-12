package io.vtown.WeiTangApp.ui;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import io.vtown.WeiTangApp.BaseApplication;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.PicImageItem;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.bean.bcomment.news.BNew;
import io.vtown.WeiTangApp.comment.UpComparator;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.net.NHttpBaseStr;
import io.vtown.WeiTangApp.comment.net.delet.NHttpDeletBaseStr;
import io.vtown.WeiTangApp.comment.util.NetUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog.onConfirmClick;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog.oncancelClick;
import io.vtown.WeiTangApp.comment.view.dialog.PromptCustomDialog;
import io.vtown.WeiTangApp.comment.view.dialog.PromptCustomDialog.onDialogConfirmClick;
import io.vtown.WeiTangApp.comment.view.pop.PShare;
import io.vtown.WeiTangApp.event.ConnectivityReceiver;
import io.vtown.WeiTangApp.event.interf.IBottomDialogResult;
import io.vtown.WeiTangApp.event.interf.ICustomDialogResult;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.event.interf.IHttpResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Video.Thumbnails;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.android.volley.Request;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import org.w3c.dom.Text;

import de.greenrobot.event.EventBus;

public class ABase extends Activity {
    /**
     * 全局单利application
     */
//	protected BaseApplication baseApplication;

    protected Context BaseAppContext;
    /**
     * 上下文context
     */
    protected Context BaseContext;
    /**
     * 上下文 activity
     */
    protected Activity BaseActivity;
    /**
     * 屏幕宽度
     */
    protected int screenWidth;
    /**
     * 屏幕高度
     */
    protected int screenHeight;
    /**
     * 网络状态变化接收器
     */
    protected ConnectivityReceiver BaseReceiver;
    /**
     * 获取HTtp数后的接口 供给子类暴露接口
     */
    protected IHttpResult<BComment> mHttpDataLisenter;
    // LS请求数据的标识
    protected static final int LOAD_INITIALIZE = 0;// 初次进入时候
    protected static final int LOAD_REFRESHING = 1;// 刷新
    protected static final int LOAD_LOADMOREING = 2;// 加载更多

    protected static final int LOAD_HindINIT = 3;// 加载更多
    // 进入UI里面 展示动画的标识
    protected static final int NOVIEW_INITIALIZE = 10;// 初次进入时候
    protected static final int NOVIEW_RIGHT = 11;// 获取数据成功
    protected static final int NOVIEW_ERROR = 12;// 获取数据失败
    /**
     * 接受bundle的参数 TODO我们全部使用BComment的构造方法灵活的去改变传递数据 TODO但是必须使用Comment这个载体
     */
    protected BComment baseBcBComment;

    protected List<PicImageItem> arrayData = new ArrayList<PicImageItem>();

    /**
     * 上边传递javabean数据时候对应的key
     */
    protected String BaseKey_Bean = "abasebeankey";
    // 选择图片
    /**
     * 拍照
     */
    public static final int TAKE_PICTURE = 0x000000;
    /**
     * 获取图库
     */
    public static final int TAKE_GALLY = 0x000001;

    /**
     * 字体库
     */

    // protected Typeface BaseTypeface;

    private int PicType;// 1==>selectpice；；；2==》goodshow;3==>goodshare

    private NHttpBaseStr MyNHttpBaseStr;
    protected int PageSize3 = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 添加activities的栈里面进行管理
         */
//        AppManager.getAppManager().addActivity(this);
        /**
         * 初始化配置文件
         */
        InItBaseConfig();
        IBasebundle();
    }

    /**
     * 初始化基类的配置信息
     */
    private void InItBaseConfig() {
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        BaseContext = ABase.this;
        BaseActivity = ABase.this;
        // BaseTypeface = Typeface.createFromAsset(getAssets(),
        // "font/fzxh.ttf");
        BaseReceiver = new ConnectivityReceiver(BaseContext);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        AppManager.getAppManager().finishActivity(this);
    }

    /**
     * 获取数据前 加载图片
     */
    protected void IDataView(View ShowLay, View ErrorView, int type) {
        switch (type) {
            case NOVIEW_INITIALIZE:// 初始化进来为获取数据的Error是不显示的 需要把ShowLay也不显示状态
                ShowLay.setVisibility(View.INVISIBLE);
                ErrorView.findViewById(R.id.iv_error).setVisibility(View.GONE);
                break;
            case NOVIEW_RIGHT:// 获取到数据就正确显示ShowLay；；隐藏ErrorView
                ErrorView.setVisibility(View.GONE);
                ShowLay.setVisibility(View.VISIBLE);
                break;
            case NOVIEW_ERROR:// 获取数据失败后就显示ErrorView并且可以点击；；；隐藏ErrorView
                ShowLay.setVisibility(View.GONE);
                ErrorView.setVisibility(View.VISIBLE);
                ErrorView.findViewById(R.id.iv_error).setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    /**
     * 定义一个Http回调接口公用获取数据的返回值
     */
    public void SetTitleHttpDataLisenter(IHttpResult<BComment> Lisenter) {
        this.mHttpDataLisenter = Lisenter;
    }

    public void IBasebundle() {
        if (getIntent().getExtras() != null
                && getIntent().getExtras().containsKey(BaseKey_Bean)) {
            baseBcBComment = (BComment) getIntent().getSerializableExtra(
                    BaseKey_Bean);
        }
    }

    /**
     * 获取Http数据的开启方法
     */
    public void FBGetHttpData(HashMap<String, String> Map, String Host,
                              int Method, final int Tage, final int LoadType) {
        if (!NetUtil.isConnected(BaseContext)) {//检查网络
            PromptManager.ShowCustomToast(BaseContext, getResources().getString(R.string.network_not_connected));
            mHttpDataLisenter.onError("网络断开", LoadType);
            PromptManager.closeLoading();
            PromptManager.closetextLoading();
            return;
        }
        if (Method == com.android.volley.Request.Method.DELETE) {// Delete请求需要通过body体去操作
            NHttpDeletBaseStr mBaseStr = new NHttpDeletBaseStr(BaseContext);
            mBaseStr.setPostResult(new IHttpResult<String>() {
                @Override
                public void onError(String error, int LoadType) {
                    if (null != mHttpDataLisenter)
                        mHttpDataLisenter.onError(error, LoadType);
                }

                @Override
                public void getResult(int Code, String Msg, String Data) {
                    if (Code != 200)
                        mHttpDataLisenter.onError(Msg, LoadType);

                    else
                        mHttpDataLisenter.getResult(Code, Msg, new BComment(
                                StrUtils.isEmpty(Data) ? "" : Data, Tage,
                                LoadType));
                }
            });
            mBaseStr.getData(Host, Map, Method);
        } else {// 非Delete请求 通过请求head体去操作

            MyNHttpBaseStr = new NHttpBaseStr(BaseContext);
            MyNHttpBaseStr.setPostResult(new IHttpResult<String>() {
                @Override
                public void onError(String error, int LoadType11) {
                    mHttpDataLisenter.onError(error, LoadType);
                }

                @Override
                public void getResult(int Code, String Msg, String Data) {
                    if (Code != 200)
                        mHttpDataLisenter.onError(Msg, LoadType);

                    else
                        mHttpDataLisenter.getResult(Code, Msg, new BComment(
                                StrUtils.isEmpty(Data) ? "" : Data, Tage,
                                LoadType));
                }
            });
            MyNHttpBaseStr.getData(Host, Map, Method);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (MyNHttpBaseStr != null) {
            MyNHttpBaseStr.CancleNet();
        }
    }

    /**
     * 微信分享弹出框 BVivew 代表目前activity的view
     */
    protected void ShowP(View BVivew, BNew mBNew) {
        PShare da = new PShare(BaseContext, mBNew, false);
        da.showAtLocation(BVivew, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 拷贝资产目录下的数据库文件
     */
    protected void copyAssetsDB(final String filename) {
        File file = new File(getFilesDir(), filename);
        if (file.exists()) {
            return;
        }

        new Thread() {
            public void run() {
                try {
                    InputStream is = getAssets().open(filename);
                    File file = new File(getFilesDir(), filename);
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ;
        }.start();
    }

    /**
     * 示例 获取DB
     */
    private void GetDb() {

        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(new File(
                getFilesDir(), "city.db"), null);

    }

    // 选择图片*******************************************************

    /**
     * 创建文件夹
     *
     * @param path
     * @return
     */
    public static String getPath(String path) {
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
        return f.getAbsolutePath();
    }

    /**
     * 创建文件夹
     *
     * @param path
     * @return
     */
    public static File getFile(String path) {
        File f = new File(path);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return f;
    }

    /**
     * 判断储存卡是否可用
     *
     * @return
     */
    public boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public byte[] Bitmap2Bytes(Bitmap bm) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return baos.toByteArray();
        } catch (Exception e) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                return baos.toByteArray();
            } catch (Exception e4) {
                return null;
            }
        }

    }

    // 选择图片*******************************************************
    // public void query()
    // {
    // String sql = "select * from gps limit 5";
    //
    // DBManager dbm = new DBManager(this);
    // SQLiteDatabase db = dbm.openDatabase();
    // Cursor cur = db.rawQuery(sql, null);
    //
    // while (cur.moveToNext())
    // {
    // float latitude = cur.getFloat(1);
    // Log.i("latitude", "维度："+latitude);
    // }
    // cur.close();
    // db.close();
    // }

    /**
     * 关闭键盘
     */
    public void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 获取视频的第一帧
     *
     * @param filePath
     * @return
     */
    public Bitmap createVideoThumbnail(String filePath) {
        Bitmap bitmappp = null;
        File file = new File(filePath);// 实例化一个File对象，指定文件路径为/storage/sdcard/Movies/music1.mp4
        // 如果文件存在的话
        if (file.exists()) {
            bitmappp = ThumbnailUtils.createVideoThumbnail(
                    file.getAbsolutePath(), Thumbnails.MICRO_KIND);// 创建一个视频缩略图
            bitmappp = ThumbnailUtils.extractThumbnail(bitmappp, 200, 200,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);// 指定视频缩略图的大小
        }
        return bitmappp;
    }

    // 文件存储根目录
    public String getFileRoot(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File external = context.getExternalFilesDir(null);
            if (external != null) {
                return external.getAbsolutePath();
            }
        }

        return context.getFilesDir().getAbsolutePath() + File.separator
                + "good" + File.separator + "savePic";
    }

    protected void ShowErrorCanLoad(String ErrorTxt) {
        if (StrUtils.ISNullStr(ErrorTxt)) return;
        ((TextView) findViewById(R.id.error_kong)).setText(ErrorTxt);
    }

    protected void ShowErrorIv(int ResouceId) {
        ((ImageView) findViewById(R.id.iv_error)).setImageResource(ResouceId);
    }

    /**
     * 左右选择弹出框的封装
     */

    public void ShowCustomDialog(String title, String Left, String Right,
                                 final IDialogResult mDialogResult) {
        final CustomDialog dialog = new CustomDialog(BaseContext,
                R.style.mystyle, R.layout.dialog_purchase_cancel, 1, Left,
                Right);
        dialog.show();
        dialog.setTitleText(title);
        dialog.HindTitle2();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setcancelListener(new oncancelClick() {

            @Override
            public void oncancelClick(View v) {
                dialog.dismiss();
                mDialogResult.LeftResult();
            }
        });

        dialog.setConfirmListener(new onConfirmClick() {
            @Override
            public void onConfirmCLick(View v) {
                dialog.dismiss();
                mDialogResult.RightResult();
            }
        });

    }

    /**
     * 弹出只有确认的对话框
     *
     * @param content
     * @param iCustomDialogResult
     */
    public void ShowPromptCustomDialog(String content,
                                       final ICustomDialogResult iCustomDialogResult) {
        final PromptCustomDialog promptCustomDialog = new PromptCustomDialog(
                BaseContext, R.style.mystyle, content);
        promptCustomDialog.show();
        promptCustomDialog.setCanceledOnTouchOutside(false);
        promptCustomDialog.setConfirmListener(new onDialogConfirmClick() {

            @Override
            public void onConfirmCLick(View v) {
                promptCustomDialog.dismiss();
                iCustomDialogResult.onResult();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
//		ImageLoader.getInstance().clearMemoryCache();
//		ImageLoader.getInstance().clearDiscCache();
//		ImageLoader.getInstance().clearDiskCache();
//		ImageLoader.getInstance().pause();

    }

    @Override
    protected void onResume() {
        super.onResume();
//		ImageLoader.getInstance().resume();
    }

    /**
     * 上下三种选择
     */
    public void ShowBottomPop(Context pContext, View BaseView, String FristTxt,
                              String SecondTxt, final IBottomDialogResult mBottomDialogResult) {

        View view = View
                .inflate(pContext, R.layout.dialog_addgood_select, null);

        final PopupWindow mPopWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.startAnimation(AnimationUtils.loadAnimation(pContext,
                R.anim.fade_ins));

        final LinearLayout ll_popup = (LinearLayout) view
                .findViewById(R.id.popup_addgood_select_select);
        ll_popup.startAnimation(AnimationUtils.loadAnimation(pContext,
                R.anim.push_bottom_in_2));

        ColorDrawable dw = new ColorDrawable(0x4a000000);
        mPopWindow.setBackgroundDrawable(dw);
        mPopWindow.setFocusable(true);
        mPopWindow.setOutsideTouchable(true);
        // mPopWindow.setContentView(view);

        view.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                int Bottom = ll_popup.getTop();

                int y = (int) event.getY();

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < Bottom) {
                        mPopWindow.dismiss();
                    }
                }
                return true;
            }
        });

        Button bt1 = (Button) view
                .findViewById(R.id.item_pop_addgoods_select_pic);
        Button bt2 = (Button) view.findViewById(R.id.item_addgoods_select_vido);
        Button bt3 = (Button) view
                .findViewById(R.id.item_addgoods_select_cancel);
        bt1.setText(FristTxt);
        bt2.setText(SecondTxt);
        bt1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {//
                mPopWindow.dismiss();
                if (mBottomDialogResult != null)
                    mBottomDialogResult.FristResult();
            }
        });
        bt2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {//
                mPopWindow.dismiss();
                if (mBottomDialogResult != null)
                    mBottomDialogResult.SecondResult();
            }
        });
        bt3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mPopWindow.dismiss();
                if (mBottomDialogResult != null)
                    mBottomDialogResult.CancleResult();
            }
        });
        mPopWindow.showAtLocation(BaseView, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 优化list
     *
     * @param scrollListener
     * @return
     */
    public PauseOnScrollListener getPauseOnScrollListener(
            OnScrollListener scrollListener) {
        PauseOnScrollListener listener = new PauseOnScrollListener(
                ImageLoader.getInstance(), false, true, scrollListener);
        return listener;
    }

    /**
     * 部分点击前检查网络
     */
    public boolean CheckNet(Context mContext) {
        if (!NetUtil.isConnected(mContext)) {
            PromptManager.ShowCustomToast(mContext,
                    mContext.getString(R.string.check_net));
            return true;
        } else
            return false;

    }

    public String Sign(HashMap<String, String> map) {
        HashMap<String, String> da = new HashMap<String, String>();

        da = map;
        List<BComment> mBlComments = new ArrayList<BComment>();
        for (Iterator iter = da.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry element = (Map.Entry) iter.next();
            String strKey = String.valueOf(element.getKey());
            String strObj = String.valueOf(element.getValue());
            mBlComments.add(new BComment(strKey, strObj));
        }
        UpComparator m = new UpComparator();
        Collections.sort(mBlComments, m);
        String K = "";
        for (BComment h : mBlComments) {
            K = K + h.getId() + h.getTitle();
        }

        K = K + Constants.SignKey;

        return Constants.SHA(K);

    }

    /**
     * 联系客服---拨号
     */
    public void ContactService() {
        final CustomDialog dialog = new CustomDialog(BaseContext,
                R.style.mystyle, R.layout.dialog_purchase_cancel, 1, "取消", "拨号");
        dialog.show();
        dialog.setTitleText("联系微糖客服400-809-0565");
        dialog.Settitles("周一至周五9:00--18:00");
        dialog.setCanceledOnTouchOutside(true);
        dialog.setcancelListener(new oncancelClick() {
            @Override
            public void oncancelClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.setConfirmListener(new onConfirmClick() {
            @Override
            public void onConfirmCLick(View v) {
                dialog.dismiss();
                Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri
                        .parse("tel:" + "4008090565"));

                startActivity(intentPhone);
            }
        });

    }

    /**
     * 购物车的提示
     */
    public void Send(int Number) {
        BMessage bMessage = new BMessage(BMessage.Tage_Tab_ShopBus);
        // bMessage.setTabShopBusNumber(mBComment.getCart_num());
        bMessage.setTabShopBusNumber(Number);
        EventBus.getDefault().post(bMessage);
    }

    /**
     * 从一条message里面获取
     *
     * @param message
     * @return
     */
    public String getMessage(EMMessage message) {

        String str = "";

        switch (message.getType()) {

            // 图片消息
            case IMAGE: {
                ImageMessageBody imageBody = (ImageMessageBody) message.getBody();
                str = "图片";
                break;
            }

            case TXT: {
                TextMessageBody txtBody = (TextMessageBody) message.getBody();
                str = txtBody.getMessage();

                break;
            }

        }

        return str;
    }

    public int getPicType() {
        return PicType;
    }

    public void setPicType(int picType) {
        PicType = picType;
    }

    /**
     * 初始化极光推送
     */
    public void InitJPush() {
        boolean isLogin_Get = Spuit.IsLogin_Get(getApplicationContext());
        if (isLogin_Get) {
            BUser user_Get = Spuit.User_Get(getApplicationContext());
            String member_id = user_Get.getMember_id();
            JPushInterface.setDebugMode(false);

            JPushInterface.init(getApplicationContext());
            JPushInterface.setAliasAndTags(getApplicationContext(), member_id,
                    null, new TagAliasCallback() {

                        @Override
                        public void gotResult(int arg0, String arg1,
                                              Set<String> arg2) {
                        }
                    });
        }
    }

    public void setArrayData(List<PicImageItem> arrayData) {
        this.arrayData = arrayData;
    }

    public List<PicImageItem> getArrayData() {
        return this.arrayData;
    }

    //eventbus通知操作
    public void SenMessage(int SentType) {
        EventBus.getDefault().post(new BMessage(SentType));
    }

    /**
     * 发show的奖励 交互 （发完show 需要和后台确认便于后台增加show积分）
     */
    protected void Show_Award() {
        HashMap<String, String> map = new HashMap<>();
        map.put("member_id", Spuit.User_Get(BaseContext).getMember_id());
        NHttpBaseStr NHttpBase = new NHttpBaseStr(BaseContext);
        NHttpBase.setPostResult(new IHttpResult<String>() {
            @Override
            public void getResult(int Code, String Msg, String Data) {

            }

            @Override
            public void onError(String error, int LoadType) {

            }
        });
        NHttpBase.getData(Constants.UpShow_AWard, map, Request.Method.POST);
    }

    //判断是否是图片
    protected boolean checkIsImageFile(String fName) {
        boolean isImageFile = false;
        // 获取扩展名
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                fName.length()).toLowerCase();
        if (FileEnd.equals("jpg") || FileEnd.equals("png") || FileEnd.equals("gif")
                || FileEnd.equals("jpeg") || FileEnd.equals("bmp")) {
            isImageFile = true;
        } else {
            isImageFile = false;
        }
        return isImageFile;
    }
}

package io.vtown.WeiTangApp.comment.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.BaseApplication;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.BShowShare;
import io.vtown.WeiTangApp.bean.bcomment.easy.PicImageItem;
import io.vtown.WeiTangApp.bean.bcomment.easy.show.BLShow;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.net.qiniu.NUPLoadUtil;
import io.vtown.WeiTangApp.comment.net.qiniu.NUpLoadUtils;
import io.vtown.WeiTangApp.comment.util.DimensionPixelUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.comment.view.select_pic.PicSelActivity;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.ALoactePhotoPager;
import io.vtown.WeiTangApp.ui.comment.AphotoPager;

/**
 * Created by Yihuihua on 2016/9/5.
 */
public class ShowSelectPic extends ATitleBase {

    private EditText good_show_select_share_ed;
    private CompleteGridView good_show_select_gridview;
    private TextView good_show_select_share_bt;

    // show；列表传递进来的数据****************
    public final static String Key_Data = "showdata";
    // =====>从show列表进入的分享界面
    private BShowShare ShowDatas = new BShowShare();//

    // 上传图片时候的九宫格的Ap
    private MyGridAdapter myGridAdapter;
    // 父类的布局
    private View parentView;

    int width = 0;

    private List<PicImageItem> showpics = new ArrayList<PicImageItem>();


    private int showpics_size = 0;

    // 上传文件时候记录是否已经上传完
    private int AllNumber = 0;
    // 需要上传的本地图片的个数
    private int NeedUpNumber = 0;
    private BUser MyUser;

    private boolean IsUpLoading;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_show_select_pic);
        EventBus.getDefault().register(this, "getEventMsg", BMessage.class);
        MyUser = Spuit.User_Get(BaseContext);

        IBund();
        IView();
    }

    private void IView() {
        good_show_select_share_ed = (EditText) findViewById(R.id.good_show_select_share_ed);
        good_show_select_share_ed.setText(ShowDatas.getIntro());

        good_show_select_gridview = (CompleteGridView) findViewById(R.id.good_show_select_gridview);
        good_show_select_share_bt = (TextView) findViewById(R.id.good_show_select_share_bt);
        good_show_select_share_bt.setOnClickListener(this);
        IGrid();
    }

    // 获取数据
    private void IBund() {
        ShowDatas = (BShowShare) getIntent().getSerializableExtra(Key_Data);
        // if (IsPic) {// 是图片的分享
        showpics = GetPicChange(ShowDatas.getImgarr());
        showpics_size = GetPicChange(ShowDatas.getImgarr()).size();


    }

    private void IGrid() {
        screenWidth = screenWidth - DimensionPixelUtil.dip2px(BaseContext, 16);
        width = screenWidth / 3;
        myGridAdapter = new MyGridAdapter(BaseContext);

        good_show_select_gridview.setAdapter(myGridAdapter);
        good_show_select_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

//                if (getArrayData() != null && getArrayData().size() > 0) {
//                    for (PicImageItem item : getArrayData()) {
//                        getArrayData().remove(item);
//                    }
//                }

//                setArrayData(showpics);

                Intent mIntent = new Intent(BaseContext,
                        ALoactePhotoPager.class);
                mIntent.putExtra("position", arg2);
                BaseApplication.GetInstance().setPicImages(showpics);
                PromptManager.SkipActivity(BaseActivity, mIntent);
            }
        });
    }


    /**
     * 如果是图片将图片换成bitmap数组&&&&&&&&&&&&&&如果是视频就不需要操作
     */
    private List<PicImageItem> GetPicChange(List<String> pics) {
        List<PicImageItem> items = new ArrayList<PicImageItem>();
        // 需要图片转化内置的列表数据======》并且展示
        for (int i = 0; i < pics.size(); i++) {
            items.add(new PicImageItem(pics.get(i), ""));

        }
        return items;
    }

    @Override
    protected void InitTile() {
        SetTitleTxt("分享");
        SetRightText("添加图片");
        right_txt.setOnClickListener(this);
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        IsUpLoading = false;
        Show_Award();//show分享成功就获取积分
        PromptManager.closeTextLoading3();
        PromptManager.ShowCustomToast(BaseContext, "Show分享成功");
        BaseActivity.finish();
        //偷偷刷新show列表
        EventBus.getDefault().post(new BMessage(BMessage.Tage_Show_Hind_Load));
    }

    @Override
    protected void DataError(String error, int LoadType) {
        PromptManager.closeTextLoading3();
        PromptManager.ShowCustomToast(BaseContext, error);


    }

    @Override
    protected void NetConnect() {
        NetError.setVisibility(View.GONE);

    }

    @Override
    protected void NetDisConnect() {
        NetError.setVisibility(View.VISIBLE);
        PromptManager.closeTextLoading3();
    }

    @Override
    protected void SetNetView() {
        SetNetStatuse(NetError);

    }

    @Override
    protected void MyClick(View V) {
        switch (V.getId()) {
            case R.id.good_show_select_share_bt:
                hintKbTwo();
                if (CheckNet(BaseContext)) return;
                ShowZhuanNet();
                break;

            case R.id.right_txt:
                // SelectPicPop();
                if (showpics.size() < 9) {
                    Intent intent = new Intent(BaseContext, PicSelActivity.class);
                    intent.putExtra(PicSelActivity.Select_Img_Size_str, 9 - showpics.size());
                    intent.putExtra(PicSelActivity.Select_Img_Type, PicSelActivity.Tage_Show_Share);
                    startActivity(intent);
                } else {
                    PromptManager.ShowCustomToast(BaseContext, "亲，你已经有9张图片了");
                    return;
                }

                break;
            default:
                break;
        }

    }

    @Override
    protected void InItBundle(Bundle bundle) {

    }

    @Override
    protected void SaveBundle(Bundle bundle) {

    }


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


    // 提交Show分享的直接接口
    private void ShowZhuanNet() {
        if (showpics.size() == 0) {
            PromptManager.ShowCustomToast(BaseContext, "请添加图片");
            return;
        }

        try {
            ImageShareShow();
        } catch (Exception e) {
        }


    }

    /**
     * 图片分享时候 需要先上传图片完毕在根据上传后的七牛返回的URL分享Show
     */
    private void ImageShareShow() {
        // 计算下需要上传的图片信息和 总的图片的信息****************
        NeedUpNumber = 0;

        AllNumber = 0;
        for (int i = 0; i < showpics.size(); i++) {
            if (!StrUtils.isEmpty(showpics.get(i).getPathurl())) {
                NeedUpNumber = NeedUpNumber + 1;
            }
        }

        // 如果没有需要上传的图片 直接进行转发
        if (0 == NeedUpNumber) {
            UpOverToShare();
            return;
        }
        IsUpLoading = true;
        // 如果有需要上传的图片===》开始对上边处理过需要上传图片的信息进行上传处理****************
        PromptManager.showtextLoading3(BaseContext, getResources()
                .getString(R.string.uploading));
        for (int i = 0; i < showpics.size(); i++) {
            final int Postion = i;

            if (StrUtils.isEmpty(showpics.get(i).getWeburl())) {
                // 开始上传本地新增加的图片
                NUPLoadUtil dLoadUtils = new NUPLoadUtil(BaseContext, new File(
                        showpics.get(Postion).getPathurl()), StrUtils.UploadQNName("photo"));
                dLoadUtils.SetUpResult1(new NUPLoadUtil.UpResult1() {
                    @Override
                    public void Progress(String arg0, double arg1) {

                    }

                    @Override
                    public void Onerror() {
                        showpics.get(Postion).setWeburl("");
                        AllNumber = AllNumber + 1;
                        if (AllNumber == NeedUpNumber) {
                            UpOverToShare();
                        }
                    }

                    @Override
                    public void Complete(String HostUrl, String Url) {
                        AllNumber = AllNumber + 1;

                        showpics.get(Postion).setWeburl(
                                HostUrl);
                        if (AllNumber == NeedUpNumber) {
                            UpOverToShare();
                        }
                    }
                });
                dLoadUtils.UpLoad();

//                NUpLoadUtils dLoadUtils = new NUpLoadUtils(BaseContext,
//                        StrUtils.Bitmap2Bytes(StrUtils.GetBitMapFromPath(showpics.get(Postion).getPathurl())),
//                        StrUtils.UploadQNName("photo"));
//
//                dLoadUtils.SetUpResult(new NUpLoadUtils.UpResult() {
//
//                    @Override
//                    public void Progress(String arg0, double arg1) {
//
//                    }
//
//                    @Override
//                    public void Onerror() {
//                        showpics.get(Postion).setWeburl("");
//                        AllNumber = AllNumber + 1;
//                        if (AllNumber == NeedUpNumber) {
//                            UpOverToShare();
//                        }
//                    }
//
//                    @Override
//                    public void Complete(String HostUrl, String Url) {
//                        AllNumber = AllNumber + 1;
//
//                        showpics.get(Postion).setWeburl(
//                                HostUrl);
//                        if (AllNumber == NeedUpNumber) {
//                            UpOverToShare();
//                        }
//                    }
//                });
//                dLoadUtils.UpLoad();

            }

        }
    }


    /**
     * 上传完毕图片后需要进行相应的分享
     */

    private void UpOverToShare() {
        List<String> Urlss = new ArrayList<String>();
        for (int i = 0; i < showpics.size(); i++) {
            if (!StrUtils.isEmpty(showpics.get(i).getWeburl())) {
                Urlss.add(showpics.get(i).getWeburl());
            }
        }
        SetTitleHttpDataLisenter(this);

        HashMap<String, String> mHashMap = ShowZhuanNetParm(Urlss,
                ShowDatas.getGoods_id(), good_show_select_share_ed.getText()
                        .toString().trim(), ShowDatas.getImgarr().get(0));

        FBGetHttpData(mHashMap, Constants.GoodsShow_ZhuanFa, Request.Method.POST, 5,
                LOAD_LOADMOREING);

    }

    // 生成show分享的参数

    /**
     * @param Urls 视频的集合
     *             //     * @param GoodId商品的ID
     *             //     * @param vid小视频的地址
     *             //     * @param intro填写内容
     *             //     * @param is_type发图片还是视频 0图片1视频
     *             //     * @param pre_url缩略图地址
     * @return
     */
    private HashMap<String, String> ShowZhuanNetParm(List<String> Urls,
                                                     String GoodId, String intro,
                                                     String pre_url) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("goods_id", GoodId);
        hashMap.put("seller_id", MyUser.getSeller_id());
        hashMap.put("is_type", "0");
        hashMap.put("intro", intro);
        hashMap.put("ratio", "1");
        SetHasmp(hashMap, Urls);
        hashMap.put("pre_url", Urls.get(0));


        return hashMap;

    }

    // 图片列表传递存放到hasmap里面
    private void SetHasmp(HashMap<String, String> ha, List<String> Urls) {
        for (int i = 0; i < Urls.size(); i++) {
            ha.put("cid" + (i + 1), Urls.get(i));
        }

    }

    public class MyGridAdapter extends BaseAdapter {
        private LayoutInflater inflater;


        public MyGridAdapter(Context context) {

            inflater = LayoutInflater.from(context);


        }

        public void update() {

            this.notifyDataSetChanged();
        }

        public int getCount() {

            return showpics.size();


        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }


        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {

                convertView = inflater.inflate(R.layout.item_published_grida,
                        parent, false);
                GridView.LayoutParams params = new GridView.LayoutParams(width, width);
                convertView.setLayoutParams(params);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                holder.image_delete = (ImageView) convertView.findViewById(R.id.item_gride_delete);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (!StrUtils.isEmpty(showpics.get(position).getPathurl())) {
                holder.image.setImageBitmap(StrUtils.GetBitMapFromPath(showpics.get(position).getPathurl()));
            } else {
                String path = showpics.get(position).getWeburl();
                ImageLoaderUtil.Load2(path, holder.image, R.drawable.error_iv2);
            }
            final int MyPostion = position;
            holder.image_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showpics.remove(MyPostion);
                    notifyDataSetChanged();
                }
            });


            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
            public ImageView image_delete;
        }

    }


    public void getEventMsg(BMessage event) {
        int msg_type = event.getMessageType();
        if (BMessage.Tage_Select_Pic_Show == msg_type) {
            List<String> imgs = event.getTmpArrayList();
            if (imgs != null && imgs.size() > 0) {

                for (String path : imgs) {
                    PicImageItem item = new PicImageItem("", path);
                    showpics.add(item);
                }

                myGridAdapter.update();
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            return;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && IsUpLoading) {
            ShowCustomDialog(getResources().getString(R.string.uploding), getResources().getString(R.string.fangqi), getResources().getString(R.string.tuichu), new IDialogResult() {
                @Override
                public void LeftResult() {

                }

                @Override
                public void RightResult() {
                    PromptManager.closeTextLoading3();
                }
            });

        }

        return super.onKeyDown(keyCode, event);
    }
}

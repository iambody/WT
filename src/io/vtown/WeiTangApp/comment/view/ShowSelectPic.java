package io.vtown.WeiTangApp.comment.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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

import java.util.ArrayList;
import java.util.List;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.show.BLShow;
import io.vtown.WeiTangApp.comment.selectpic.ui.AShareGaller;
import io.vtown.WeiTangApp.comment.selectpic.ui.AlbumActivity;
import io.vtown.WeiTangApp.comment.selectpic.util.Bimp;
import io.vtown.WeiTangApp.comment.selectpic.util.FileUtils;
import io.vtown.WeiTangApp.comment.selectpic.util.ImageItem;
import io.vtown.WeiTangApp.comment.selectpic.util.PublicWay;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.event.interf.IBottomDialogResult;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * Created by Yihuihua on 2016/9/5.
 */
public class ShowSelectPic extends ATitleBase {

    private EditText good_show_select_share_ed;
    private CompleteGridView good_show_select_gridview;
    private TextView good_show_select_share_bt;
    // 默认是show列表进来的 需要获取blcomment&&&如果是商品详情进入的需要获取其他类型的数据
    public final static String Key_FromShow = "fromshow";
    private boolean IsShow = true;
    // show；列表传递进来的数据****************
    public final static String Key_Data = "showdata";
    // =====>从show列表进入的分享界面
    private BLShow ShowDatas = new BLShow();// ;new BLComment();
    // 判断是照片还是视频=====》标识
    private boolean IsPic;
    // 上传图片时候的九宫格的Ap
    private MyGridAdapter myGridAdapter;
    // 父类的布局
    private View parentView;


    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_show_select_pic);
        parentView = LayoutInflater.from(BaseContext).inflate(
                R.layout.activity_good_zhuanfa, null);
        PublicWay.activityList.add(this);
        IBund();
        IView();

    }

    private void IView(){
        good_show_select_share_ed = (EditText) findViewById(R.id.good_show_select_share_ed);
        good_show_select_gridview = (CompleteGridView)findViewById(R.id.good_show_select_gridview);
        good_show_select_share_bt = (TextView)findViewById(R.id.good_show_select_share_bt);
        good_show_select_share_bt.setOnClickListener(this);
        IGrid();
    }

    // 获取数据
    private void IBund() {
        IsShow = getIntent().getBooleanExtra(Key_FromShow, false);
        if (IsShow) {// 从show进入的
            ShowDatas = (BLShow) getIntent().getSerializableExtra(Key_Data);
            IsPic = ShowDatas.getIs_type().equals("0");

            IsPic = true;
            // if (IsPic) {// 是图片的分享
            Bimp.tempSelectBitmap = (ArrayList<ImageItem>) GetPicChange(ShowDatas
                    .getImgarr());
            Bimp.max = GetPicChange(ShowDatas.getImgarr()).size();

        } else {

        }

    }

    private void IGrid(){
        myGridAdapter = new MyGridAdapter(BaseContext);
        myGridAdapter.update();
        good_show_select_gridview.setAdapter(myGridAdapter);
        good_show_select_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == Bimp.tempSelectBitmap.size()) {
                    // new PicPop(BaseContext, parentView);
                    SelectPicPop();
                } else {
                    Intent intent = new Intent(BaseActivity, AShareGaller.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", arg2);
                    BaseActivity.startActivity(intent);
                }
            }
        });
    }

    /**
     * 点击添加图片时候的pop操作
     */
    private void SelectPicPop() {
        ShowBottomPop(BaseContext, parentView, "拍照", "照片",
                new IBottomDialogResult() {

                    @Override
                    public void SecondResult() {
                        Intent intent = new Intent(BaseActivity,
                                AlbumActivity.class).putExtra("isshare", true);
                        startActivity(intent);
                        overridePendingTransition(R.anim.activity_translate_in,
                                R.anim.activity_translate_out);
                    }

                    @Override
                    public void FristResult() {
                        photo();
                    }

                    @Override
                    public void CancleResult() {
                    }
                });
    }

    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {

                    String fileName = String.valueOf(System.currentTimeMillis());
                    Bitmap bm = (Bitmap) data.getExtras().get("data");
                    FileUtils.saveBitmap(bm, fileName);
                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setBitmap(bm);
                    Bimp.tempSelectBitmap.add(takePhoto);
                }
                break;
        }
    }

    /**
     * 如果是图片将图片换成bitmap数组&&&&&&&&&&&&&&如果是视频就不需要操作
     */

    private List<ImageItem> GetPicChange(List<String> pics) {
        List<ImageItem> items = new ArrayList<ImageItem>();
        // 需要图片转化内置的列表数据======》并且展示
        for (int i = 0; i < pics.size(); i++) {
            items.add(new ImageItem(pics.get(i), ""));
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

    }

    @Override
    protected void DataError(String error, int LoadType) {

    }

    @Override
    protected void NetConnect() {

    }

    @Override
    protected void NetDisConnect() {

    }

    @Override
    protected void SetNetView() {

    }

    @Override
    protected void MyClick(View V) {
        switch (V.getId()){
            case R.id.good_show_select_share_bt:
                hintKbTwo();
                SharePop();
                break;

            case R.id.right_txt:
                    SelectPicPop();
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


    /**
     * 点击分享时候弹出的框
     */
    private void SharePop() {

    }





    public class MyGridAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;
        boolean IsZroo = false;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public MyGridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {

            loading();
        }

        public int getCount() {

            return Bimp.tempSelectBitmap.size();

            // if (Bimp.tempSelectBitmap.size() == 9) {
            // return 9;
            // }
            // return (Bimp.tempSelectBitmap.size() + 1);
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int arg0) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            int width = screenWidth / 3;
            ViewHolder holder = null;

            if (convertView == null) {

                convertView = inflater.inflate(R.layout.item_published_grida,
                        parent, false);
                GridView.LayoutParams params = new GridView.LayoutParams(width,width);
                //params.span = 1;
                convertView.setLayoutParams(params);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                holder.image_delete = (ImageView)convertView.findViewById(R.id.item_gride_delete);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (IsZroo && position == 0)
                return convertView;
            if (!IsZroo && position == 0) {
                IsZroo = true;
            }
            if (!StrUtils.isEmpty(Bimp.tempSelectBitmap.get(position)
                    .getImagePath())
                    || Bimp.tempSelectBitmap.get(position).getBitmap() != null) {
                if (Bimp.tempSelectBitmap.get(position).getBitmap() == null)
                    Bimp.tempSelectBitmap.get(position).setBitmap(
                            StrUtils.GetBitMapFromPath(Bimp.tempSelectBitmap
                                    .get(position).getImagePath()));
                holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position)
                        .getBitmap());
            } else {
                String path = Bimp.tempSelectBitmap.get(position)
                        .getThumbnailPath();
                if (null == path)
                    path = "";

                ImageLoaderUtil.Load2(path, holder.image, R.drawable.error_iv2);
            }

            holder.image_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bimp.tempSelectBitmap.remove(position);
                    notifyDataSetChanged();
                }
            });

            // }

            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
            public ImageView image_delete;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        myGridAdapter.notifyDataSetChanged();

                        // PromptManager.ShowCustomToast(BaseContext, "条数："
                        // + Bimp.tempSelectBitmap.size());
                        if (Bimp.tempSelectBitmap.size() > 0) {
                            // PromptManager.ShowCustomToast(BaseContext, "path："
                            // + Bimp.tempSelectBitmap.get(0).getImagePath());
                        }
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading() {
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.tempSelectBitmap.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        } else {
                            Bimp.max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }
    }

}

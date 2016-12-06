package io.vtown.WeiTangApp.ui.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.BaseApplication;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.PicImageItem;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.bean.bcomment.three_one.search.BLSearchShopAndGood;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.DimensionPixelUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.comment.view.custom.switchButtonView.EaseSwitchButton;
import io.vtown.WeiTangApp.comment.view.select_pic.PicSelActivity;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.ALoactePhotoPager;
import io.vtown.WeiTangApp.ui.comment.recordervido.ARecoderVido;

/**
 * Created by Yihuihua on 2016/12/5.
 */

public class AAddNewShow extends ATitleBase implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.tv_add_new_show_pic)
    TextView tvAddNewShowPic;
    @BindView(R.id.tv_add_new_show_vedio)
    TextView tvAddNewShowVedio;
    @BindView(R.id.iv_add_new_show_vedio_bg)
    ImageView ivAddNewShowVedioBg;
    @BindView(R.id.iv_add_new_show_vedio_control_icon)
    ImageView ivAddNewShowVedioControlIcon;
    @BindView(R.id.rl_add_new_show_vedio_layout)
    RelativeLayout rlAddNewShowVedioLayout;
    @BindView(R.id.gv_add_new_show_pics)
    CompleteGridView gvAddNewShowPics;
    @BindView(R.id.et_add_new_show_txt_content)
    EditText etAddNewShowTxtContent;
    @BindView(R.id.sb_add_new_show_select_good)
    EaseSwitchButton sbAddNewShowSelectGood;
    @BindView(R.id.rl_add_new_show_add_good)
    RelativeLayout rlAddNewShowAddGood;
    @BindView(R.id.iv_add_new_show_good_icon)
    ImageView ivAddNewShowGoodIcon;
    @BindView(R.id.iv_add_new_show_good_name)
    TextView ivAddNewShowGoodName;
    @BindView(R.id.tv_add_new_show_good_sales)
    TextView tvAddNewShowGoodSales;
    @BindView(R.id.tv_add_new_show_good_score)
    TextView tvAddNewShowGoodScore;
    @BindView(R.id.tv_add_new_show_good_price)
    TextView tvAddNewShowGoodPrice;
    @BindView(R.id.tv_add_new_show_good_origprice)
    TextView tvAddNewShowGoodOrigprice;
    @BindView(R.id.ll_add_new_show_good)
    LinearLayout llAddNewShowGood;
    @BindView(R.id.fl_add_new_show_good)
    FrameLayout flAddNewShowGood;
    @BindView(R.id.tv_add_new_show_good_share)
    TextView tvAddNewShowGoodShare;
    private Unbinder mBinder;
    private static final int TYPE_PIC = 123;
    private static final int TYPE_VEDIO = 124;
    private int current_type = TYPE_PIC;
    List<PicImageItem> datas = new ArrayList<PicImageItem>();
    private int width = 0;
    private MyGridAdapter myGridAdapter;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_add_new_show);
        EventBus.getDefault().register(this, "getEventMsg", BMessage.class);
        mBinder = ButterKnife.bind(this);
        IView();
        IGrid();
    }

    private void IView() {
        sbAddNewShowSelectGood.setChecked(true);
        sbAddNewShowSelectGood.setOnCheckedChangeListener(this);
    }

    @Override
    protected void InitTile() {
        SetTitleTxt("发布Show");

        SetRightText("添加");

    }


    private void IGrid() {
        screenWidth = screenWidth - DimensionPixelUtil.dip2px(BaseContext, 16);
        width = screenWidth / 3;
        myGridAdapter = new MyGridAdapter(BaseContext);

        gvAddNewShowPics.setAdapter(myGridAdapter);
        gvAddNewShowPics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                BaseApplication.GetInstance().setPicImages(datas);
                PromptManager.SkipActivity(BaseActivity, mIntent);
            }
        });
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
    protected void InItBundle(Bundle bundle) {

    }

    @Override
    protected void SaveBundle(Bundle bundle) {

    }

    private void ControlClick(int ClickId) {
        tvAddNewShowPic
                .setBackground(R.id.tv_add_new_show_pic == ClickId ? getResources()
                        .getDrawable(R.drawable.shape_left_pre)
                        : getResources().getDrawable(R.drawable.shape_left_nor));
        tvAddNewShowPic
                .setTextColor(R.id.tv_add_new_show_pic == ClickId ? getResources()
                        .getColor(R.color.TextColorWhite) : getResources()
                        .getColor(R.color.app_fen));

        tvAddNewShowVedio
                .setBackground(R.id.tv_add_new_show_vedio == ClickId ? getResources()
                        .getDrawable(R.drawable.shape_right_pre)
                        : getResources()
                        .getDrawable(R.drawable.shape_right_nor));
        tvAddNewShowVedio
                .setTextColor(R.id.tv_add_new_show_vedio == ClickId ? getResources()
                        .getColor(R.color.TextColorWhite) : getResources()
                        .getColor(R.color.app_fen));

    }

    @OnClick({R.id.right_txt, R.id.tv_add_new_show_pic, R.id.tv_add_new_show_vedio, R.id.iv_add_new_show_vedio_control_icon, R.id.rl_add_new_show_add_good, R.id.ll_add_new_show_good, R.id.tv_add_new_show_good_share})
    protected void MyClick(View V) {
        switch (V.getId()) {
            case R.id.tv_add_new_show_pic:
                current_type = TYPE_PIC;
                ControlClick(R.id.tv_add_new_show_pic);
                rlAddNewShowVedioLayout.setVisibility(View.GONE);
                gvAddNewShowPics.setVisibility(View.VISIBLE);

                break;
            case R.id.tv_add_new_show_vedio:
                current_type = TYPE_VEDIO;
                ControlClick(R.id.tv_add_new_show_vedio);
                gvAddNewShowPics.setVisibility(View.GONE);
                rlAddNewShowVedioLayout.setVisibility(View.VISIBLE);

                break;
            case R.id.iv_add_new_show_vedio_control_icon:

                break;
            case R.id.rl_add_new_show_add_good:
            case R.id.ll_add_new_show_good:
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext, ASouSouGood.class).putExtra(ASouSouGood.From_Add_Show, true));
                break;
            case R.id.tv_add_new_show_good_share:
                break;
            case R.id.right_txt:
                if (TYPE_PIC == current_type) {
                    toPicSelect(PicSelActivity.Tage_Add_New_Show);
                } else {
                    PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                            ARecoderVido.class));
                }
                break;
        }
    }

    private void toPicSelect(int type) {
        if (9 - datas.size() > 0) {
            Intent intent = new Intent(BaseContext, PicSelActivity.class);
            intent.putExtra(PicSelActivity.Select_Img_Size_str, 9 - datas.size());
            intent.putExtra(PicSelActivity.Select_Img_Type, type);
            startActivity(intent);
        } else {
            PromptManager.ShowCustomToast(BaseContext, "亲，最多9张图片哦");
            return;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            flAddNewShowGood.setVisibility(View.VISIBLE);
        } else {
            flAddNewShowGood.setVisibility(View.GONE);
        }
    }

    public void getEventMsg(BMessage event) {
        int messageType = event.getMessageType();
        switch (messageType) {
            case BMessage.From_Search_Lv_Finish:
                BLSearchShopAndGood goodInfo = event.getmSearchGood();
                setGoodInfo(goodInfo);
                break;

            case BMessage.Tage_Select_Pic_Add_Show://选择图片返回的数据
                List<String> imgs = event.getTmpArrayList();
                if (imgs != null && imgs.size() > 0) {

                    for (String path : imgs) {
                        PicImageItem item = new PicImageItem("", path);
                        datas.add(item);
                    }

                    myGridAdapter.update();
                }
                break;
        }
    }

    private void setGoodInfo(BLSearchShopAndGood goodInfo) {
        if (goodInfo != null) {
            rlAddNewShowAddGood.setVisibility(View.GONE);
            llAddNewShowGood.setVisibility(View.VISIBLE);
            ImageLoaderUtil.Load2(goodInfo.getAvatar(), ivAddNewShowGoodIcon, R.drawable.error_iv2);
            StrUtils.SetTxt(ivAddNewShowGoodName, goodInfo.getTitle());
            StrUtils.SetMoneyFormat(BaseContext, tvAddNewShowGoodPrice, goodInfo.getSell_price(), 15);
            if ("0".equals(goodInfo.getOrig_price()) || StrUtils.isEmpty(goodInfo.getOrig_price())) {
                tvAddNewShowGoodOrigprice.setVisibility(View.INVISIBLE);
            } else {
                tvAddNewShowGoodOrigprice.setVisibility(View.VISIBLE);
                StrUtils.SetTxt(tvAddNewShowGoodOrigprice, StrUtils.SetTextForMony(goodInfo.getOrig_price()));
                tvAddNewShowGoodOrigprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }

            if (goodInfo.getScore() > 0) {
                tvAddNewShowGoodScore.setVisibility(View.VISIBLE);
                StrUtils.SetTxt(tvAddNewShowGoodScore, "积分：" + goodInfo.getScore());
            } else {
                tvAddNewShowGoodScore.setVisibility(View.GONE);
            }

            if (goodInfo.getSales() > 0) {
                tvAddNewShowGoodSales.setVisibility(View.VISIBLE);
                StrUtils.SetTxt(tvAddNewShowGoodSales, "销量：" + goodInfo.getSales() + "件");
            } else {
                tvAddNewShowGoodSales.setVisibility(View.GONE);
            }
        } else {
            llAddNewShowGood.setVisibility(View.GONE);
            rlAddNewShowAddGood.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 点击左侧按钮的监听事件
     */
    public void title_left_bt(View v) {
        exitEdit();
    }

    @Override
    public void onBackPressed() {
        exitEdit();
    }

    private void exitEdit() {
        ShowCustomDialog("退出此次编辑？", "取消", "退出", new IDialogResult() {
            @Override
            public void LeftResult() {

            }

            @Override
            public void RightResult() {
                finish();
                overridePendingTransition(R.anim.push_rigth_in, R.anim.push_rigth_out);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinder.unbind();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {

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

            return datas.size();


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

                convertView = inflater.inflate(R.layout.item_add_new_show,
                        parent, false);
                GridView.LayoutParams params = new GridView.LayoutParams(width, width);
                convertView.setLayoutParams(params);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (!StrUtils.isEmpty(datas.get(position).getPathurl())) {
                holder.itemAddNewShowImage.setImageBitmap(StrUtils.GetBitMapFromPath(datas.get(position).getPathurl()));
            } else {
                String path = datas.get(position).getWeburl();
                ImageLoaderUtil.Load2(path, holder.itemAddNewShowImage, R.drawable.error_iv2);
            }
            final int MyPostion = position;
            holder.itemAddNewShowDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datas.remove(MyPostion);
                    notifyDataSetChanged();
                }
            });


            return convertView;
        }

    }

    class ViewHolder {
        @BindView(R.id.item_add_new_show_image)
        ImageView itemAddNewShowImage;
        @BindView(R.id.item_add_new_show_delete)
        ImageView itemAddNewShowDelete;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

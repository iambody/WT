package io.vtown.WeiTangApp.comment.view.pop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.event.interf.OnPasswordInputFinish;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-5-23 上午10:39:00
 */
public class PPassWord extends PopupWindow implements OnClickListener {
    /**
     * 上下文
     */
    private Context pContext;

    /**
     * 基view
     */
    private View BaseView;

    private String strPassword; // 输入的密码

    private TextView[] tvList; // 用数组保存6个TextView，为什么用数组？
    // 因为就6个输入框不会变了，用数组内存申请固定空间，比List省空间（自己认为）

    private GridView gridView; // 用GrideView布局键盘，其实并不是真正的键盘，只是模拟键盘的功能

    private ArrayList<Map<String, String>> valueList; // 用数组不能往adapter中填充

    private ImageView imgCancel;
    private TextView tvForget;
    private int currentIndex = -1; // 用于记录当前输入密码格位置
    /**
     * 回调接口
     */
    private OnPasswordInputFinish mPasswordInputFinish;

    public PPassWord(Context pContext, int width, String Title) {
        super();
        this.pContext = pContext;

        BaseView = LayoutInflater.from(pContext).inflate(
                R.layout.layout_popup_bottom, null);

        IPop(width);
        IView(Title);
        ITouch();
    }

    private void ITouch() {
    }

    private void IView(String TITLE) {
        valueList = new ArrayList<Map<String, String>>();
        tvList = new TextView[6];
        StrUtils.SetTxt(
                (TextView) BaseView.findViewById(R.id.popup_bottom_title),
                TITLE);
        imgCancel = (ImageView) BaseView.findViewById(R.id.img_cancel);
        imgCancel.setOnClickListener(this);

        tvForget = (TextView) BaseView.findViewById(R.id.tv_forgetPwd);
        tvForget.setOnClickListener(this);

        tvList[0] = (TextView) BaseView.findViewById(R.id.tv_pass1);
        tvList[1] = (TextView) BaseView.findViewById(R.id.tv_pass2);
        tvList[2] = (TextView) BaseView.findViewById(R.id.tv_pass3);
        tvList[3] = (TextView) BaseView.findViewById(R.id.tv_pass4);
        tvList[4] = (TextView) BaseView.findViewById(R.id.tv_pass5);
        tvList[5] = (TextView) BaseView.findViewById(R.id.tv_pass6);

        gridView = (GridView) BaseView.findViewById(R.id.gv_keybord);
        SetView();
    }

    private void SetView() {

		/* 初始化按钮上应该显示的数字 */
        for (int i = 1; i < 13; i++) {
            Map<String, String> map = new HashMap<String, String>();
            if (i < 10) {
                map.put("name", String.valueOf(i));
            } else if (i == 10) {
                map.put("name", "取消");
            } else if (i == 11) {
                map.put("name", String.valueOf(0));
            } else if (i == 12) {
                map.put("name", "删除");
            }
            valueList.add(map);
        }

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position < 11 && position != 9) { // 点击0~9按钮
                    if (currentIndex >= -1 && currentIndex < 5) { // 判断输入位置————要小心数组越界
                        tvList[++currentIndex].setText(valueList.get(position)
                                .get("name"));
                    }
                } else if (position == 9) {
                    PPassWord.this.dismiss();
                } else {
                    if (position == 11) { // 点击退格键
                        if (currentIndex - 1 >= -1) { // 判断是否删除完毕————要小心数组越界
                            tvList[currentIndex--].setText("");
                        }
                    }
                }
            }
        });

    }

    private void IPop(int width) {
        setContentView(BaseView);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.MATCH_PARENT);

        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        setBackgroundDrawable(dw);
        this.setOutsideTouchable(true);

    }

    // GrideView的适配器
    BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return valueList.size();
        }

        @Override
        public Object getItem(int position) {
            return valueList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(pContext,
                        R.layout.item_view_pay_gride, null);
                viewHolder = new ViewHolder();
                viewHolder.btnKey = (TextView) convertView
                        .findViewById(R.id.btn_keys);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.btnKey.setText(valueList.get(position).get("name"));
            if (position == 10) {
//                viewHolder.btnKey.setTextSize(13);
            }
            if (position == 9) {
//                viewHolder.btnKey.setTextSize(13);
////                viewHolder.btnKey
////                        .setBackgroundResource(R.drawable.lightpink);
//                viewHolder.btnKey.setTextSize(13);
                viewHolder.btnKey.setBackgroundColor(pContext.getResources().getColor(R.color.app_fen2));
//                viewHolder.btnKey.setEnabled(false);
                viewHolder.btnKey.setTextColor(pContext.getResources().getColor(R.color.white));
            }
            if (position == 11) {
//                viewHolder.btnKey.setTextSize(13);
                viewHolder.btnKey.setBackgroundColor(pContext.getResources().getColor(R.color.app_fen2));
//                viewHolder.btnKey
//                        .setBackgroundResource(R.drawable.selector_key_del);
                viewHolder.btnKey.setTextColor(pContext.getResources().getColor(R.color.white));
            }

            return convertView;
        }

        class ViewHolder {
            public TextView btnKey;
        }
    };

    // 设置监听方法，在第6位输入完成后触发
    public void setOnPassWordListener(final OnPasswordInputFinish Passinterface) {
        this.mPasswordInputFinish = Passinterface;
        tvList[5].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 1) {
                    strPassword = ""; // 每次触发都要先将strPassword置空，再重新获取，避免由于输入删除再输入造成混乱
                    for (int i = 0; i < 6; i++) {
                        strPassword += tvList[i].getText().toString().trim();
                    }
                    mPasswordInputFinish.inputFinish(getStrPassword()); // 接口中要实现的方法，完成密码输入完成后的响应逻辑
                }
            }
        });
    }

    /* 获取输入的密码 */
    public String getStrPassword() {
        return strPassword;
    }

    /* 暴露取消支付的按钮，可以灵活改变响应 */
    public ImageView getCancelImageView() {
        return imgCancel;
    }

    /* 暴露忘记密码的按钮，可以灵活改变响应 */
    public TextView getForgetTextView() {
        return tvForget;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_cancel:
                mPasswordInputFinish.Cancle();
                break;
            case R.id.tv_forgetPwd:
                mPasswordInputFinish.LostPassWord();
                break;
        }
    }
}

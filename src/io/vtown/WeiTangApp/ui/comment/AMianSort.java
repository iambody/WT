package io.vtown.WeiTangApp.ui.comment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.easy.mainsort.BSortCategory;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.net.NHttpBaseStr;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.comment.view.custom.CompleteListView;
import io.vtown.WeiTangApp.event.interf.IHttpResult;
import io.vtown.WeiTangApp.ui.ABase;

/**
 * Created by datutu on 2016/11/1.
 */

public class AMianSort extends ABase {
    @BindView(R.id.pop_maitab_sort_type)
    TextView popMaitabSortType;
    @BindView(R.id.pop_maitab_sort_price)
    TextView popMaitabSortPrice;
    @BindView(R.id.pop_maitab_sort_jifen)
    TextView popMaitabSortJifen;
    @BindView(R.id.pop_maitab_sort_branc)
    TextView popMaitabSortBranc;
    @BindView(R.id.pop_maitab_queding)
    TextView popMaitabQueding;
    @BindView(R.id.pop_maitab_reset)
    TextView popMaitabReset;
    @BindView(R.id.pop_maitab_cancle)
    TextView popMaitabCancle;
    @BindView(R.id.pop_maitab_sort_ls)
    CompleteListView popMaitabSortLs;
    @BindView(R.id.pop_maitab_sort_brand_gridview)
    CompleteGridView popMaitabSortBrandGridview;


    //我左侧的文本控制列表
    private List<TextView> MyLeft;
    //我的二级ap
    private MySortAp mySortAp;
    private MyBrandAp myBrnadAp;

    //我左侧上边的位置标示
    private int LeftPostion = 0;
    //记录下二级类别被选中后的

    //排序Fragment页面带过来的数据
    private String catoryid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_maintab_sort);
        ButterKnife.bind(this);
        IBund();
        IBase();
        IData();
    }

    /**
     * 根据情况获取数据 默认刚进来获取的数据时
     */
    private void IData() {
        //获取分类列表

    }

    private void IBund() {
        catoryid = getIntent().getStringExtra("catoryid");
    }

    private void IBase() {
        MyLeft = new ArrayList<>();
        MyLeft.add(popMaitabSortType);
        MyLeft.add(popMaitabSortPrice);
        MyLeft.add(popMaitabSortJifen);
        MyLeft.add(popMaitabSortBranc);
        if (catoryid.equals("0")) {
            popMaitabSortType.setVisibility(View.GONE);
            LeftPostion = 1;
            CheckLeftPostion(LeftPostion);
        } else {
            CheckLeftPostion(LeftPostion);
            NetSort(catoryid);
        }
        //开始二级分类的Ap的初始化
        mySortAp = new MySortAp();
        popMaitabSortLs.setAdapter(mySortAp);
//开始进行品牌列表的Ap的初始化
        myBrnadAp = new MyBrandAp();
        popMaitabSortBrandGridview.setAdapter(myBrnadAp);

        popMaitabSortLs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (LeftPostion) {
                    case 0:
                        mySortAp.SetSelectPostion(position);

                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }
        });
        popMaitabSortBrandGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (LeftPostion) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        myBrnadAp.SetSelectPostion(position);
                        break;
                }
            }
        });

    }

    private void CheckLeftPostion(int postion) {
        for (int i = 0; i < 4; i++) {
            if (i == postion) {
                MyLeft.get(i).setBackgroundColor(getResources().getColor(R.color.white));
            } else {
                MyLeft.get(i).setBackgroundColor(getResources().getColor(R.color.transparent));
            }
        }
    }

    @OnClick({R.id.pop_maitab_sort_type, R.id.pop_maitab_sort_price, R.id.pop_maitab_sort_jifen, R.id.pop_maitab_sort_branc, R.id.pop_maitab_queding, R.id.pop_maitab_reset, R.id.pop_maitab_cancle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pop_maitab_sort_type:
                LeftPostion = 0;
                CheckLeftPostion(LeftPostion);

                popMaitabSortBrandGridview.setVisibility(View.GONE);
                popMaitabSortLs.setVisibility(View.VISIBLE);
                break;
            case R.id.pop_maitab_sort_price:
                LeftPostion = 1;
                CheckLeftPostion(LeftPostion);
                break;
            case R.id.pop_maitab_sort_jifen:
                LeftPostion = 2;
                CheckLeftPostion(LeftPostion);
                break;
            case R.id.pop_maitab_sort_branc://
                LeftPostion = 3;
                CheckLeftPostion(LeftPostion);
                //开始请求数据

                popMaitabSortLs.setVisibility(View.GONE);
                popMaitabSortBrandGridview.setVisibility(View.VISIBLE);
                if (popMaitabSortBrandGridview.getCount() == 0)
                    NetBrandLs();

                break;
            case R.id.pop_maitab_queding:
                BaseActivity.finish();
                break;
            case R.id.pop_maitab_reset:
                BaseActivity.finish();
                break;
            case R.id.pop_maitab_cancle:
                BaseActivity.finish();
                break;
        }
    }


    /**
     * 获取二级分类的列表
     */
    private void NetSort(String Sorttype) {
        PromptManager.showtextLoading(BaseContext, "筛选中");
        NHttpBaseStr mNHttpBaseStr = new NHttpBaseStr(BaseContext);
        mNHttpBaseStr.setPostResult(new IHttpResult<String>() {
            @Override
            public void getResult(int Code, String Msg, String Data) {
                List<BSortCategory> dataresult = JSON.parseArray(Data, BSortCategory.class);
                mySortAp.FrashSortAp(dataresult);
            }

            @Override
            public void onError(String error, int LoadType) {

            }
        });
        HashMap<String, String> map = new HashMap<>();
        map.put("pid", Sorttype);
        mNHttpBaseStr.getData(Constants.Add_Good_Categoty, map, Request.Method.GET);
    }

    /**
     * 获取品牌列表
     * 点击品牌后
     */
    private void NetBrandLs() {
        //先判断是否存在品牌的缓存数据
        String BrnadCahcStr = CacheUtil.HomeSort_Brand_Get(BaseContext);
        if (!StrUtils.isEmpty(BrnadCahcStr)) {//存在缓存
            List<String> dataresult = JSON.parseArray(BrnadCahcStr, String.class);
            myBrnadAp.FrashBrandAp(dataresult);
            return;
        }

        PromptManager.showtextLoading(BaseContext, "筛选中");
        NHttpBaseStr mbrandNHttpBaseStr = new NHttpBaseStr(BaseContext);
        mbrandNHttpBaseStr.setPostResult(new IHttpResult<String>() {
            @Override
            public void getResult(int Code, String Msg, String Data) {
                List<String> dataresult = JSON.parseArray(Data, String.class);
                myBrnadAp.FrashBrandAp(dataresult);
                CacheUtil.HomeSort_Brand_Save(BaseContext,Data);
            }

            @Override
            public void onError(String error, int LoadType) {

            }
        });
        HashMap<String, String> map = new HashMap<>();
        mbrandNHttpBaseStr.getData(Constants.BrandsLs, map, Request.Method.GET);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //通知分类Fragment进行重置分类文本的颜色和图片的颜色
        EventBus.getDefault().post(new BMessage(2111));
    }

    /**
     * 二级分类的ap
     */
    private class MySortAp extends BaseAdapter {
        //二级分类
        private List<BSortCategory> datas = new ArrayList<>();
        //品牌列表

        private int selectItem = -1;

        public void SetSelectPostion(int postion) {
            this.selectItem = postion;
            this.notifyDataSetChanged();
        }

        public void FrashSortAp(List<BSortCategory> ddd) {
            this.datas = ddd;
            this.notifyDataSetChanged();
        }

        public void FrashBrnadAp(List<String> dass) {

        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            myitem mmiten = null;
            if (null == convertView) {
                convertView = LayoutInflater.from(BaseContext).inflate(R.layout.pop_mainsort_sort_item, null);
                mmiten = new myitem();
                mmiten.pop_mainsort_sort_item_txt = (TextView) convertView.findViewById(R.id.pop_mainsort_sort_item_txt);
                convertView.setTag(mmiten);
            } else {
                mmiten = (myitem) convertView.getTag();
            }
            BSortCategory da = datas.get(position);
            StrUtils.SetTxt(mmiten.pop_mainsort_sort_item_txt, da.getCate_name());
            if (selectItem == position) {
                mmiten.pop_mainsort_sort_item_txt.setBackgroundColor(getResources().getColor(R.color.app_fen2));
                mmiten.pop_mainsort_sort_item_txt.setTextColor(getResources().getColor(R.color.white));
            } else {
                mmiten.pop_mainsort_sort_item_txt.setBackgroundColor(getResources().getColor(R.color.transparent));
                mmiten.pop_mainsort_sort_item_txt.setTextColor(getResources().getColor(R.color.black));
            }
            return convertView;
        }

        class myitem {
            TextView pop_mainsort_sort_item_txt;
        }
    }

    /**
     * 品牌的分类的Ap
     */
    private class MyBrandAp extends BaseAdapter {
        //二级分类
        private List<String> datas = new ArrayList<>();
        //品牌列表

        private int selectItem = -1;

        public void SetSelectPostion(int postion) {
            this.selectItem = postion;
            this.notifyDataSetChanged();
        }

        public void FrashBrandAp(List<String> ddd) {
            this.datas = ddd;
            this.notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyBrnaditem mmiten = null;
            if (null == convertView) {
                convertView = LayoutInflater.from(BaseContext).inflate(R.layout.item_pop_mainsort_sort_brand, null);
                mmiten = new MyBrnaditem();
                mmiten.pop_mainsort_sort_brand_item_txt = (TextView) convertView.findViewById(R.id.pop_mainsort_sort_brand_item_txt);
                convertView.setTag(mmiten);
            } else {
                mmiten = (MyBrnaditem) convertView.getTag();
            }
            String da = datas.get(position);
            StrUtils.SetTxt(mmiten.pop_mainsort_sort_brand_item_txt, da);
            if (selectItem == position) {
                mmiten.pop_mainsort_sort_brand_item_txt.setBackgroundColor(getResources().getColor(R.color.app_fen2));
                mmiten.pop_mainsort_sort_brand_item_txt.setTextColor(getResources().getColor(R.color.white));
            } else {
                mmiten.pop_mainsort_sort_brand_item_txt.setBackgroundColor(getResources().getColor(R.color.transparent));
                mmiten.pop_mainsort_sort_brand_item_txt.setTextColor(getResources().getColor(R.color.black));
            }
            return convertView;
        }

        class MyBrnaditem {
            TextView pop_mainsort_sort_brand_item_txt;
        }
    }
}

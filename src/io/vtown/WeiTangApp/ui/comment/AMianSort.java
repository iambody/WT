package io.vtown.WeiTangApp.ui.comment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.vtown.WeiTangApp.R;
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

    private List<TextView> MyLeft;
    private int LeftPostion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_maintab_sort);
        ButterKnife.bind(this);
        IBase();
        CheckLeftPostion(0);
    }

    private void IBase() {
        MyLeft = new ArrayList<>();
        MyLeft.add(popMaitabSortType);
        MyLeft.add(popMaitabSortPrice);
        MyLeft.add(popMaitabSortJifen);
        MyLeft.add(popMaitabSortBranc);
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

    @OnClick({R.id.pop_maitab_sort_type, R.id.pop_maitab_sort_price, R.id.pop_maitab_sort_jifen, R.id.pop_maitab_sort_branc,R.id.pop_maitab_queding, R.id.pop_maitab_reset, R.id.pop_maitab_cancle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pop_maitab_sort_type:
                LeftPostion = 0;
                CheckLeftPostion(LeftPostion);
                break;
            case R.id.pop_maitab_sort_price:
                LeftPostion = 1;
                CheckLeftPostion(LeftPostion);
                break;
            case R.id.pop_maitab_sort_jifen:
                LeftPostion = 2;
                CheckLeftPostion(LeftPostion);
                break;
            case R.id.pop_maitab_sort_branc:
                LeftPostion = 3;
                CheckLeftPostion(LeftPostion);
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


}

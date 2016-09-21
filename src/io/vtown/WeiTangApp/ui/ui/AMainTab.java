package io.vtown.WeiTangApp.ui.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jauker.widget.BadgeView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.view.radiogroup.GradualRadioButton;
import io.vtown.WeiTangApp.comment.view.radiogroup.GradualRadioGroup;
import io.vtown.WeiTangApp.fragment.FBase;
import io.vtown.WeiTangApp.fragment.main.FMainCenter;
import io.vtown.WeiTangApp.fragment.main.FMainHome;
import io.vtown.WeiTangApp.fragment.main.FMainShop;
import io.vtown.WeiTangApp.fragment.main.FMainShopBus;
import io.vtown.WeiTangApp.fragment.main.FMainShow;
import io.vtown.WeiTangApp.ui.ABaseFragment;

/**
 * Created by datutu on 2016/9/18.
 */
public class AMainTab extends ABaseFragment implements GradualRadioGroup.MainTabPostion, View.OnClickListener {
    //    private ViewPager maintab_viewpager;
    private GradualRadioGroup maintab_radiobar;
    private GradualRadioButton maintab_home, maintab_shop, maintab_shopbus, maintab_center;
    private List<GradualRadioButton> RadioButtons;
    private ImageView maintab_show_iv;
    private FBase FMainHome, FMainShop, FMainShow, FMainShopBus, FMainCenter;
    private List<FBase> Fragments;
    private int CurrentPostion = 0;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_maintab);
        EventBus.getDefault().register(this, "ReciverChangMainTab", BMessage.class);
        FBaseInit();
        IBase();

    }

    private void FBaseInit() {


        Fragments = new ArrayList<FBase>();
        FMainHome = new FMainHome();
        FMainShop = new FMainShop();
        FMainShow = new FMainShow();
        FMainShopBus = new FMainShopBus();
        FMainCenter = new FMainCenter();

        FMainHome.setArguments(GetBund());
        FMainShop.setArguments(GetBund());
        FMainShow.setArguments(GetBund());
        FMainShopBus.setArguments(GetBund());
        FMainCenter.setArguments(GetBund());

        Fragments.add(FMainHome);
        Fragments.add(FMainShop);
//        Fragments.add(FMainShow);
        Fragments.add(FMainShopBus);
        Fragments.add(FMainCenter);


    }

    private Bundle GetBund() {
        Bundle mybundle = new Bundle();
        mybundle.putInt("screenwidth", screenWidth);
        return mybundle;
    }

    private void IBase() {
        maintab_radiobar = (GradualRadioGroup) findViewById(R.id.maintab_radiobar);
        maintab_home = (GradualRadioButton) findViewById(R.id.maintab_home);
        maintab_shop = (GradualRadioButton) findViewById(R.id.maintab_shop);
        maintab_shopbus = (GradualRadioButton) findViewById(R.id.maintab_shopbus);
        maintab_center = (GradualRadioButton) findViewById(R.id.maintab_center);
        RadioButtons = new ArrayList<GradualRadioButton>();

        RadioButtons.add(maintab_home);
        RadioButtons.add(maintab_shop);
        RadioButtons.add(maintab_shopbus);
        RadioButtons.add(maintab_center);
        maintab_show_iv = (ImageView) findViewById(R.id.maintab_show_iv);
        maintab_radiobar.SetListenerPostion(this);
        maintab_show_iv.setOnClickListener(this);
        switchContent1(FMainHome, R.id.maintab_fragmentlay);
        CurrentFragment = FMainHome;

        if (Spuit.ShopBusNumber_Get(this) > 0) {
            maintab_shopbus.SetIsShowTage(true);
        }

    }

    @Override
    protected void InitTile() {

    }

    @Override
    protected void NetConnect() {

        NetError.setVisibility(View.GONE);
    }

    @Override
    protected void NetDisConnect() {
        NetError.setVisibility(View.VISIBLE);
    }

    @Override
    protected void SetNetView() {
        SetNetStatuse(NetError);
    }

    @Override
    public void ListenerTabPostion(int postion) {
        if (CurrentPostion == postion) {
            PromptManager.ShowCustomToast(BaseContext, "无用");
            return;
        }
        if (postion == 2) {
            maintab_shopbus.SetIsShowTage(false);
        }
        if (CurrentPostion == 4) {//当前处于show的fragment
            maintab_show_iv.setImageResource(R.drawable.tab3_f_nor);
            switchContent(CurrentFragment, Fragments.get(postion), R.id.maintab_fragmentlay);
//            CurrentFragment = Fragments.get(postion);
            CurrentPostion = postion;
            return;
        }
        switchContent(CurrentFragment, Fragments.get(postion), R.id.maintab_fragmentlay);

        CurrentPostion = postion;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.maintab_show_iv:
                CleranGroup();
                break;
        }
    }

    /**
     * 点击show时候需要进行的处理
     */
    private void CleranGroup() {
        for (int i = 0; i < RadioButtons.size(); i++) {
            RadioButtons.get(i).setRadioButtonChecked(false);
        }
        maintab_show_iv.setImageResource(R.drawable.tab3_f_pre);
        switchContent(CurrentFragment, FMainShow, R.id.maintab_fragmentlay);
//        CurrentFragment = FMainShow;
        CurrentPostion = 4;
    }

    public void ReciverChangMainTab(BMessage bMessage) {
        switch (bMessage.getMessageType()) {
            case BMessage.Tage_Tab_one:// 首页

                break;
            case BMessage.Tage_Tab_two:// 商铺

                break;
            case BMessage.Tage_Tab_three:// show


                break;
            case BMessage.Tage_Tab_four:// shopbus

                switchContent3(CurrentFragment, Fragments.get(2), R.id.maintab_fragmentlay);
                RadioButtons.get(0).setRadioButtonChecked(false);
                RadioButtons.get(1).setRadioButtonChecked(false);
                RadioButtons.get(2).setRadioButtonChecked(true);
                RadioButtons.get(3).setRadioButtonChecked(false);
//                getSupportFragmentManager().beginTransaction().hide(CurrentFragment);
//                switchContent1( Fragments.get(2), R.id.maintab_fragmentlay);
//                CurrentFragment = FMainShopBus;
                CurrentPostion = 2;

                break;
            case BMessage.Tage_Tab_five:// center

                break;
            case BMessage.Tage_Tab_Im_Regist:// 当不处于聊天页面时候就开始注册

                // unregisterReceiver(msgReceiver);
                break;
            case BMessage.Tage_Tab_Im_UnRegist:// 当处于聊天页面时候就取消注册

                // unregisterReceiver(msgReceiver);
                // PromptManager.ShowCustomToast(BaseCotext, "注销广播");
                break;
            case BMessage.Tage_Tab_ShopBus:
//                ShopBadgeView.setBadgeCount(bMessage.getTabShopBusNumber());

                break;
            case BMessage.Tage_MainTab_ShopBus:
                if (bMessage.getTabShopBusNumber() == 0) {
                    maintab_shopbus.SetIsShowTage(false);
                }
                break;
            case BMessage.Shop_Frash://购物车获取数据刷新后需要进行刷新
//                Spuit.ShopBusNumber_Save(BaseCotext, Spuit.ShopBusNumber_Get(BaseCotext) + bMessage.getGood_numb());
//                ShopBadgeView.setBadgeCount(Spuit.ShopBusNumber_Get(BaseCotext));
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
        }
    }
}

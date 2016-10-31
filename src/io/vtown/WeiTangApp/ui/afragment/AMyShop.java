package io.vtown.WeiTangApp.ui.afragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.fragment.FBase;
import io.vtown.WeiTangApp.fragment.main.FMainShop;
import io.vtown.WeiTangApp.ui.ABaseFragment;

/**
 *
 */

public class AMyShop extends ABaseFragment {
    Fragment mainShop;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_myshop);
        mainShop = new FMainShop();
        mainShop.setArguments(GetBund());
        getSupportFragmentManager().beginTransaction().add(R.id.myshop_fragment, mainShop).commit();
    }

    private Bundle GetBund() {
        Bundle mybundle = new Bundle();
        mybundle.putInt("screenwidth", screenWidth);
        return mybundle;
    }

    @Override
    protected void InitTile() {

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
}

package io.vtown.WeiTangApp.bean.bcomment.easy.oderbeing;

import java.util.ArrayList;
import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BLDComment;

/**
 * Created by datutu on 2016/9/12.
 */
public class BOdeBeing extends BBase {
    private String order_total_price;// ":"3768",

    public String getIs_used_coupons() {
        return is_used_coupons;
    }

    public void setIs_used_coupons(String is_used_coupons) {
        this.is_used_coupons = is_used_coupons;
    }

    private String money_paid;// ":"768",
    private String is_used_coupons;// Y=>可选//N不可选
    private BLDComment coupons;
    private BLDComment address = new BLDComment();
    private List<BLComment> list = new ArrayList<BLComment>();
}

package io.vtown.WeiTangApp.bean.bcomment.easy;

import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * Created by Yihuihua on 2016/10/8.
 */

public class BCBankCardAndAlipayInfo extends BBase {
    private BLBankCardAndAlipayList alipay_list;
    private String tixinarule;//tixinarule=提现金额不能低于100元,提现收取1%手续费。
    private List<BLBankCardAndAlipayList> bank_list;

    public BLBankCardAndAlipayList getAlipay_list() {
        return alipay_list;
    }

    public void setAlipay_list(BLBankCardAndAlipayList alipay_list) {
        this.alipay_list = alipay_list;
    }

    public String getTixinarule() {
        return tixinarule;
    }

    public void setTixinarule(String tixinarule) {
        this.tixinarule = tixinarule;
    }

    public List<BLBankCardAndAlipayList> getBank_list() {
        return bank_list;
    }

    public void setBank_list(List<BLBankCardAndAlipayList> bank_list) {
        this.bank_list = bank_list;
    }
}

package io.vtown.WeiTangApp.bean.bcomment.easy;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * Created by Yihuihua on 2016/10/8.
 */

public class BLBankCardAndAlipayList extends BBase {

   private String id;// "id": 148,
    private String bank_id;// "bank_id": 12,
    private String icon;// "icon": "http://fs.v-town.cc/NYYH.png",
    private String bank_name;// "bank_name": "农业银行",
    private String bank_code;// "bank_code": "NYYH",
    private String card_number;// "card_number": "45558899558877",
    private String name;// "name": "mk"

    private String alipay;//alipay=dkdkdkdk@165.com
   // private String name;//name=mk

    public String getAlipay() {
        return alipay;
    }

    public void setAlipay(String alipay) {
        this.alipay = alipay;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBank_id() {
        return bank_id;
    }

    public void setBank_id(String bank_id) {
        this.bank_id = bank_id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package io.vtown.WeiTangApp.bean.bcomment.easy;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * Created by Yihuihua on 2016/9/19.
 */
public class BLSelectBank extends BBase {

    private String bank_code;//"bank_code": "GSYH",
    private String bank_id;// "bank_id": 10,
    private String icon;//"icon": "http://fs.v-town.cc/GSYH.png",
    private String bank_name;//"bank_name": "中国工商银行"


    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
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
}

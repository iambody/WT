package io.vtown.WeiTangApp.bean.bcomment.easy.shop;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * Created by datutu on 2016/9/10.
 */
public class BShopCatory extends BBase {
    private String id;//": 26,
    private String is_delete;//": 0,
    private String cate_name;//": "其他服装"
    private boolean IsBrandDetaiLsSelect = false;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }

    public boolean isBrandDetaiLsSelect() {
        return IsBrandDetaiLsSelect;
    }

    public void setBrandDetaiLsSelect(boolean brandDetaiLsSelect) {
        IsBrandDetaiLsSelect = brandDetaiLsSelect;
    }
}

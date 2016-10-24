package io.vtown.WeiTangApp.bean.bcomment.easy;

import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BLDComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.gooddetail.BDataGood;
import io.vtown.WeiTangApp.bean.bcomment.easy.gooddetail.BDataPrice;
import io.vtown.WeiTangApp.bean.bcomment.easy.gooddetail.BSellInf;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @author 商品详情
 * @version 创建时间：2016-7-27 上午11:51:57
 */
public class BGoodDetail extends BBase {
    // 图片详情
    private String avatar;// =http://fs.v-town.cc/avatar_522381468916718038.jpg
    private String category_id;// =4
    private String cover;// =http://fs.v-town.cc/photo_14679666240000000000000724010375.jpg
    private String create_time;// =1467969761
    private List<BDataGood> goods_attr;
    private String goods_code;// =
    private BSellInf goods_info;
    private String goods_info_id;//=339
    private String goods_pid;// =0
    private String goods_sid;// =0
    private String goods_url;// =http://dev.vt.m.v-town.cn/goods/index/index?goods_id=687&type=goods
    private String id;// =687
    private String is_agent;// =0
    private String is_collect;// =0
    private String is_delete;// =0
    private String is_desell;// =0
    private String is_edit;// =0
    private BDataPrice ladder_price = new BDataPrice();//
    private String max_price;// =3
    private String postage;// =0
    private String sale_status;// =100
    private String sell_price;// =1
    private String seller_id;// =490
    private String seller_name;// =蜜趣杂货铺
    private String slevel;// =0
    private String title;// =盘子
    private String orig_price;
//    private String is_fee;
    private int is_fee;//=1

    private String is_dasell;//品牌商是否可以代理！！！！！！！！！！！！！！！！！！！！！=0

    public String getAvatar() {
        return avatar;
    }

    public String getIs_dasell() {
        return is_dasell;
    }

    public void setIs_dasell(String is_dasell) {
        this.is_dasell = is_dasell;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public List<BDataGood> getGoods_attr() {
        return goods_attr;
    }

    public void setGoods_attr(List<BDataGood> goods_attr) {
        this.goods_attr = goods_attr;
    }

    public String getGoods_code() {
        return goods_code;
    }

    public void setGoods_code(String goods_code) {
        this.goods_code = goods_code;
    }

    public BSellInf getGoods_info() {
        return goods_info;
    }

    public void setGoods_info(BSellInf goods_info) {
        this.goods_info = goods_info;
    }

    public String getGoods_info_id() {
        return goods_info_id;
    }

    public void setGoods_info_id(String goods_info_id) {
        this.goods_info_id = goods_info_id;
    }

    public String getGoods_pid() {
        return goods_pid;
    }

    public void setGoods_pid(String goods_pid) {
        this.goods_pid = goods_pid;
    }

    public String getGoods_sid() {
        return goods_sid;
    }

    public void setGoods_sid(String goods_sid) {
        this.goods_sid = goods_sid;
    }

    public String getGoods_url() {
        return goods_url;
    }

    public void setGoods_url(String goods_url) {
        this.goods_url = goods_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIs_agent() {
        return is_agent;
    }

    public void setIs_agent(String is_agent) {
        this.is_agent = is_agent;
    }

    public String getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(String is_collect) {
        this.is_collect = is_collect;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    public String getIs_desell() {
        return is_desell;
    }

    public void setIs_desell(String is_desell) {
        this.is_desell = is_desell;
    }

    public String getIs_edit() {
        return is_edit;
    }

    public void setIs_edit(String is_edit) {
        this.is_edit = is_edit;
    }

    public BDataPrice getLadder_price() {
        return ladder_price;
    }

    public void setLadder_price(BDataPrice ladder_price) {
        this.ladder_price = ladder_price;
    }

    public String getMax_price() {
        return max_price;
    }

    public void setMax_price(String max_price) {
        this.max_price = max_price;
    }

    public String getPostage() {
        return postage;
    }

    public void setPostage(String postage) {
        this.postage = postage;
    }

    public String getSale_status() {
        return sale_status;
    }

    public void setSale_status(String sale_status) {
        this.sale_status = sale_status;
    }

    public String getSell_price() {
        return sell_price;
    }

    public void setSell_price(String sell_price) {
        this.sell_price = sell_price;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getSlevel() {
        return slevel;
    }

    public void setSlevel(String slevel) {
        this.slevel = slevel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIs_fee() {
        return is_fee;
    }

    public void setIs_fee(int is_fee) {
        this.is_fee = is_fee;
    }

    public String getOrig_price() {
        return orig_price;
    }

    public void setOrig_price(String orig_price) {
        this.orig_price = orig_price;
    }
}

package io.vtown.WeiTangApp.bean.bcomment.easy;

import java.util.ArrayList;
import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * Created by datutu on 2016/9/8.
 * 用于图片Show分享的操作时候的 bean传递
 */
public class BShowShare extends BBase {
    //所有图片分享进入的 都需要传进来一个图片数组和一个商品Id
    private List<String> imgarr = new ArrayList<String>();
    private String goods_id;
    private String intro;//带入到页面的描述

    //下面是视频show分享时候  都需要传进的数据
//注意！！！！所有进入视频show分享的带入数据都需要全部set这些数据
    private String Vido_Vid;//视频的url
    private String Vido_pre_url;//视频的封面
    //    private String goods_id;
    public List<String> getImgarr() {
        return imgarr;
    }

    public void setImgarr(List<String> imgarr) {
        this.imgarr = imgarr;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getVido_Vid() {
        return Vido_Vid;
    }

    public void setVido_Vid(String vido_Vid) {
        Vido_Vid = vido_Vid;
    }

    public String getVido_pre_url() {
        return Vido_pre_url;
    }

    public void setVido_pre_url(String vido_pre_url) {
        Vido_pre_url = vido_pre_url;
    }


}

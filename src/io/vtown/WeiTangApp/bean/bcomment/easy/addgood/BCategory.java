package io.vtown.WeiTangApp.bean.bcomment.easy.addgood;

import java.util.ArrayList;
import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;
import io.vtown.WeiTangApp.bean.bcomment.BLDComment;

/**
 * Created by datutu on 2016/9/9.
 */
public class BCategory extends BBase {
    // *****************start*********添加商品之规格*********start******************
    private String id;
    private String cate_name;
    private String add_good_id;// 分类Id
    private String add_good_cate_name;// 分类名称
    private String add_good_attrs_id_1;// 分类参数Id
    private String add_good_attrs_id_2;// 分类参数Id
    private String add_good_attrs_name_1;// 分类参数名称
    private String add_good_attrs_name_2;// 分类参数名称

    private String add_good_attrs_value_1;// 输入的参数值
    private String add_good_attrs_value_2;// 输入的参数值
    private String add_good_attrs_value_3;// 输入的参数值
    private String add_good_attrs_value_4;// 输入的参数值
    private List<BLCatehory> attrs = new ArrayList<BLCatehory>();
    // *****************end*********添加商品之规格*********end******************


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }

    public String getAdd_good_id() {
        return add_good_id;
    }

    public void setAdd_good_id(String add_good_id) {
        this.add_good_id = add_good_id;
    }

    public String getAdd_good_cate_name() {
        return add_good_cate_name;
    }

    public void setAdd_good_cate_name(String add_good_cate_name) {
        this.add_good_cate_name = add_good_cate_name;
    }

    public String getAdd_good_attrs_id_1() {
        return add_good_attrs_id_1;
    }

    public void setAdd_good_attrs_id_1(String add_good_attrs_id_1) {
        this.add_good_attrs_id_1 = add_good_attrs_id_1;
    }

    public String getAdd_good_attrs_id_2() {
        return add_good_attrs_id_2;
    }

    public void setAdd_good_attrs_id_2(String add_good_attrs_id_2) {
        this.add_good_attrs_id_2 = add_good_attrs_id_2;
    }

    public String getAdd_good_attrs_name_1() {
        return add_good_attrs_name_1;
    }

    public void setAdd_good_attrs_name_1(String add_good_attrs_name_1) {
        this.add_good_attrs_name_1 = add_good_attrs_name_1;
    }

    public String getAdd_good_attrs_name_2() {
        return add_good_attrs_name_2;
    }

    public void setAdd_good_attrs_name_2(String add_good_attrs_name_2) {
        this.add_good_attrs_name_2 = add_good_attrs_name_2;
    }

    public String getAdd_good_attrs_value_1() {
        return add_good_attrs_value_1;
    }

    public void setAdd_good_attrs_value_1(String add_good_attrs_value_1) {
        this.add_good_attrs_value_1 = add_good_attrs_value_1;
    }

    public String getAdd_good_attrs_value_2() {
        return add_good_attrs_value_2;
    }

    public void setAdd_good_attrs_value_2(String add_good_attrs_value_2) {
        this.add_good_attrs_value_2 = add_good_attrs_value_2;
    }

    public String getAdd_good_attrs_value_3() {
        return add_good_attrs_value_3;
    }

    public void setAdd_good_attrs_value_3(String add_good_attrs_value_3) {
        this.add_good_attrs_value_3 = add_good_attrs_value_3;
    }

    public String getAdd_good_attrs_value_4() {
        return add_good_attrs_value_4;
    }

    public void setAdd_good_attrs_value_4(String add_good_attrs_value_4) {
        this.add_good_attrs_value_4 = add_good_attrs_value_4;
    }

    public List<BLCatehory> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<BLCatehory> attrs) {
        this.attrs = attrs;
    }
}

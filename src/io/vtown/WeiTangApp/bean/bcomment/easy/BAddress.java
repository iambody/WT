package io.vtown.WeiTangApp.bean.bcomment.easy;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * Created by Yihuihua on 2016/9/21.
 */

public class BAddress extends BBase {

    private String usrname;
    private String mobile;
    private String province;
    private String city;
    private String area;
    private String address;

    public BAddress(String usrname,String mobile,String province,String city,String area,String address){
        super();
        this.usrname = usrname;
        this.mobile = mobile;
        this.province = province;
        this.city = city;
        this.area = area;
        this.address = address;
    }

    public String getUsrname() {
        return usrname;
    }

    public void setUsrname(String usrname) {
        this.usrname = usrname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}

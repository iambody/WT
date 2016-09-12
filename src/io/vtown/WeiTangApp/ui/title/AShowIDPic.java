package io.vtown.WeiTangApp.ui.title;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.net.qiniu.NUPLoadUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.view.select_pic.PicSelActivity;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * Created by Yihuihua on 2016/9/12.
 */
public class AShowIDPic extends ATitleBase {

    private ImageView show_id_pic_face;
    private ImageView show_id_pic_back;
    private TextView show_id_pic_submit;
    private List<String> pic_list = new ArrayList<String>();
    // 上传文件时候记录是否已经上传完
    private int AllNumber = 0;
    // 需要上传的本地图片的个数
    private int NeedUpNumber = 0;
    private FrameLayout show_id_pic_face_fl;
    private FrameLayout show_id_pic_back_fl;
    private TextView tv_show_id_pic_face;
    private TextView tv_show_id_pic_back;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_show_id_pic);
        EventBus.getDefault().register(this, "getEventMsg", BMessage.class);
        IView();
    }

    private void IView() {
        show_id_pic_face = (ImageView) findViewById(R.id.show_id_pic_face);
        show_id_pic_back = (ImageView) findViewById(R.id.show_id_pic_back);
        show_id_pic_submit = (TextView) findViewById(R.id.show_id_pic_submit);
        show_id_pic_face_fl = (FrameLayout) findViewById(R.id.show_id_pic_face_fl);
        show_id_pic_back_fl = (FrameLayout) findViewById(R.id.show_id_pic_back_fl);
        tv_show_id_pic_face = (TextView) findViewById(R.id.tv_show_id_pic_face);
        tv_show_id_pic_back = (TextView) findViewById(R.id.tv_show_id_pic_back);
        show_id_pic_face_fl.setOnClickListener(this);
        show_id_pic_back_fl.setOnClickListener(this);
        show_id_pic_submit.setOnClickListener(this);
    }

    @Override
    protected void InitTile() {
        SetTitleTxt("身份证信息");
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {

    }

    @Override
    protected void DataError(String error, int LoadType) {

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

    @Override
    protected void MyClick(View V) {
        switch (V.getId()) {
            case R.id.show_id_pic_face_fl:
                Intent intent = new Intent(BaseContext, PicSelActivity.class);
                intent.putExtra(PicSelActivity.Select_Img_Type, PicSelActivity.Tage_Usr_ID_Face);
                intent.putExtra(PicSelActivity.Select_Img_Size_str, 1);
                startActivity(intent);
                break;

            case R.id.show_id_pic_back_fl:
                Intent intent1 = new Intent(BaseContext, PicSelActivity.class);
                intent1.putExtra(PicSelActivity.Select_Img_Type, PicSelActivity.Tage_Usr_ID_Back);
                intent1.putExtra(PicSelActivity.Select_Img_Size_str, 1);
                startActivity(intent1);
                break;

            case R.id.show_id_pic_submit:
                if (CheckNet(BaseContext)) return;
                PromptManager.showLoading(BaseContext);
                ShowZhuanNet();
                break;
        }

    }

    // 提交Show分享的直接接口
    private void ShowZhuanNet() {
        if (pic_list.size() != 2) {
            PromptManager.ShowCustomToast(BaseContext, "请添加正反面身份证照片");
            return;
        }


        ImageShareShow();


    }

    /**
     * 图片分享时候 需要先上传图片完毕在根据上传后的七牛返回的URL分享Show
     */
    private void ImageShareShow() {


        // 计算下需要上传的图片信息和 总的图片的信息****************
        NeedUpNumber = 0;

        AllNumber = 0;
        for (int i = 0; i < pic_list.size(); i++) {
            if (!StrUtils.isEmpty(pic_list.get(i))) {
                NeedUpNumber = NeedUpNumber + 1;
            }
        }


        // 如果有需要上传的图片===》开始对上边处理过需要上传图片的信息进行上传处理****************
        PromptManager.showtextLoading3(BaseContext, getResources()
                .getString(R.string.uploading));
        for (int i = 0; i < pic_list.size(); i++) {
            final int Postion = i;



                NUPLoadUtil dLoadUtils = new NUPLoadUtil(BaseContext, new File(
                        pic_list.get(Postion)), StrUtils.UploadQNName("photo_id"));
                dLoadUtils.SetUpResult1(new NUPLoadUtil.UpResult1() {
                    @Override
                    public void Progress(String arg0, double arg1) {

                    }

                    @Override
                    public void Onerror() {

                        AllNumber = AllNumber + 1;

                    }

                    @Override
                    public void Complete(String HostUrl, String Url) {
                        AllNumber = AllNumber + 1;

                       PromptManager.ShowCustomToast(BaseContext,"身证照片上传成功");
                        BaseActivity.finish();
                    }
                });
                dLoadUtils.UpLoad();




        }
    }

    @Override
    protected void InItBundle(Bundle bundle) {

    }

    @Override
    protected void SaveBundle(Bundle bundle) {

    }

    public void getEventMsg(BMessage event){
        int msg_type = event.getMessageType();
        switch (msg_type){
            case BMessage.Tage_Select_Pic_ID_Face:
                List<String> data =  event.getTmpArrayList();
                getPic(data,show_id_pic_face,tv_show_id_pic_face);

                break;

            case BMessage.Tage_Select_Pic_ID_Back:
                List<String> data2 =  event.getTmpArrayList();
                getPic(data2,show_id_pic_back,tv_show_id_pic_back);
                break;
        }
    }

    private void getPic(List<String> data,ImageView view,TextView tv_view) {
        String s = data.get(0);
        if(!StrUtils.isEmpty(s)){
            tv_view.setVisibility(View.INVISIBLE);
            view.setImageBitmap(StrUtils.GetBitMapFromPath(s));
            pic_list.add(s);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            return;
        }
    }

    
}

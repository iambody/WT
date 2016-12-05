package io.vtown.WeiTangApp.comment.util;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

/**
 * Created by datutu on 2016/12/1.
 */

public class DownFileUtils {
    //这里Demo展示的是下载一个apk文件路径，下载后安装//其他文件可以删除文件的安装
    public void xUtilsHttpUtilDonLoadFile(String downLoadUrl, String savePath) {
        //保存位置
        //关于下边的File.separator原文The system-dependent string used to separate components in filenames ('/').
        //其实就是代表了一个斜杠
        final File filepath = new File(savePath + File.separator + "MyDownLoadText" + File.separator + downLoadUrl);//仅创建路径的File对象
        if (!filepath.exists()) {
            filepath.mkdir();//如果路径不存在就先创建路径
        }
        // 准备进度条Progress弹窗
//        final ProgressDialog dialog = new ProgressDialog(this);
//        dialog.setCancelable(true);// 设置是否可以通过点击Back键取消
//        dialog.setTitle("下载中");
//        //Progress弹窗设置为水平进度条
//        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
        //httputils网络请求工具
        HttpUtils http = new HttpUtils();
        //下载请求
        /*
             第一参数：downLoadUrl是网络下载位置
             第二参数：filepath.getPath()是下载保存位置
            第三个参数：如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
            第四个参数：如果从请求返回信息中获取到文件名，下载完成后自动重命名。
            第五个参数：下载的监听
         */
        HttpHandler handler = http.download(downLoadUrl, filepath.getPath(), true, true, new RequestCallBack<File>() {
            @Override
            public void onStart() {
//                dialog.show();//展示关闭弹窗
//                Log.i("开", "onStart: 开始下载");
            }
            @Override
            public void onLoading(long total, long current, boolean isUploading) {
//                Log.i("下载中", "onLoading:总共： " + total + "已下" + current);
//                dialog.setMax((int) total);
//                dialog.setProgress((int) current);
            }
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
//                dialog.dismiss();//关闭弹窗
//                //安装下载的文件（如果不是apk这里可以不用安装，下载完成后自己看着办吧）
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(Uri.fromFile(new File(filepath.getPath().toString())), "application/vnd.android.package-archive");
//                startActivity(intent);
//                //卸载
//                  /*    Uri packageURI = Uri.parse("package:com.demo.CanavaCancel");//package:com.demo.CanavaCancel应用的包名
//                        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
//                         startActivity(uninstallIntent);*/
//                Log.i("完成", "onStart: 下载完成");
            }
            @Override
            public void onFailure(HttpException error, String msg) {
//                dialog.dismiss();//关闭弹窗
//                Log.i("取消", "onStart: 下载失败" + error.toString());
            }
        });
    }
}

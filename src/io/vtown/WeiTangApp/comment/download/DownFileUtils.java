package io.vtown.WeiTangApp.comment.download;

import android.util.Log;

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
    private DownLoadListener downLoadListener;

    public void SetResult(DownLoadListener ResultLister) {
        this.downLoadListener = ResultLister;
    }

    //这里Demo展示的是下载一个apk文件路径，下载后安装//其他文件可以删除文件的安装
    public void xUtilsHttpUtilDonLoadFile(String downLoadUrl, String savePath, File filepath, final String name) {
        //保存位置
        //关于下边的File.separator原文The system-dependent string used to separate components in filenames ('/').
        //其实就是代表了一个斜杠
//        final File filepath = new File(savePath);
//        final File filepath = new File(savePath + File.separator + "MyDownLoadText" + File.separator + downLoadUrl);//仅创建路径的File对象
//        if (!filepath.exists()) {
//            filepath.mkdir();//如果路径不存在就先创建路径
//        }
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
        HttpHandler handler = http.download(downLoadUrl, savePath, true, true, new RequestCallBack<File>() {
            @Override
            public void onStart() {
                Log.i("filetest", " 开始下载。。。。。。。。。。。。");
//                dialog.show();//展示关闭弹窗
//
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
//                Log.i("下载中", "onLoading:总共： " + total + "已下" + current);
//                dialog.setMax((int) total);
//                dialog.setProgress((int) current);
            }

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                Log.i("filetest", " 下载成功。。。。。。。。。。。。" + responseInfo.result.getPath());
                responseInfo.result.renameTo(new File(responseInfo.result.getPath().replace(responseInfo.result.getName(), name + ".jpg")));
                Log.i("filetest", " 下载成功修改。。。。。。。。。。。。" + responseInfo.result.getName());
                downLoadListener.DownLoadOk();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                downLoadListener.DownLoadError();
                Log.i("filetest", " 下载失败。。。。。。。。。。。。" + error.toString());
            }
        });
    }

    public static String getPathFromFilepath(final String filepath) {
        int pos = filepath.lastIndexOf('/');
        if (pos != -1) {
            return filepath.substring(0, pos);
        }
        return "";
    }

    public interface DownLoadListener {
        void DownLoadOk();

        void DownLoadError();
    }

}

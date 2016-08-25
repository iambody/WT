package io.vtown.WeiTangApp.comment.ndk;

/**
 * Created by datutu on 2016/8/25.
 */
public class NdkUtils {
    static {
        System.loadLibrary("ndk_test");
    }

    public static native String getTestStringFormC();
}

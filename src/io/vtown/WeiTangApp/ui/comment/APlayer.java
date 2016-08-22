package io.vtown.WeiTangApp.ui.comment;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.comment.view.VidoPlayer;
import io.vtown.WeiTangApp.ui.ABase;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.ProgressBar;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-7-15 下午6:51:31
 * 
 */
public class APlayer extends ABase {
	private SurfaceView activity_play_video;
	private ProgressBar activity_play_progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aplayer);

		SurfaceView view_video = (SurfaceView) findViewById(R.id.activity_play_video);
		ProgressBar view_progress = (ProgressBar) findViewById(R.id.activity_play_progress);
		VidoPlayer player = new VidoPlayer(view_video, view_progress,
				new VidoPlayer.PlayerListener() {
					@Override
					public void onPlayProgressChangeListener(int current,
							int max) {
					}

					@Override
					public void onLoadProgressChangeListener(int percent) {
					}

					@Override
					public void onCompleteListener() {
					}

					@Override
					public void onStartListener(int start) {
					}

					@Override
					public void onPauseListener(int position) {
					}

					@Override
					public void onStopListener() {
					}

					@Override
					public void onSeekListener(int seekto) {
					}

					@Override
					public void onInited() {
						// player.playUrl("http://url"); //从头播放
						// player.playUrl("http://url",123); //从123秒处开始播放
					}
				});
		player.playUrl("http://fs.v-town.cc/video1.mp4"); // 从头播放
	}

}

package io.vtown.WeiTangApp.comment.view;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ProgressBar;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-7-15 下午6:49:16
 * 
 */
public class VidoPlayer implements MediaPlayer.OnBufferingUpdateListener,
		MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener,
		MediaPlayer.OnSeekCompleteListener, SurfaceHolder.Callback {
	private int videoWidth;
	private int videoHeight;
	public MediaPlayer mediaPlayer;
	private SurfaceHolder surfaceHolder;
	private ProgressBar skbProgress;
	private Timer mTimer = new Timer();
	public long pos = 0;
	public int seek = 0;
	private PlayerListener listener;

	public VidoPlayer(SurfaceView surfaceView, ProgressBar skbProgress,
			PlayerListener listener) {
		this.listener = listener;
		this.skbProgress = skbProgress;
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mTimer.schedule(mTimerTask, 0, 1000);
	}

	TimerTask mTimerTask = new TimerTask() {
		@Override
		public void run() {
			if (mediaPlayer == null) {
				return;
			}
			if (mediaPlayer.isPlaying() && skbProgress.isPressed() == false) {

				handleProgress.sendEmptyMessage(0);
			}
		}
	};
	Handler handleProgress = new Handler() {
		public void handleMessage(Message msg) {

			int position = mediaPlayer.getCurrentPosition() / 1000;
			int duration = mediaPlayer.getDuration() / 1000;

			if (duration > 0) {
				pos = skbProgress.getMax() * position / duration;
				skbProgress.setProgress((int) pos);
			}
			if (listener != null) {
				listener.onPlayProgressChangeListener(position, duration);
			}
		}

		;
	};

	public void play() {
		if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
			mediaPlayer.start();
			if (listener != null)
				listener.onStartListener(mediaPlayer.getCurrentPosition());
		}
	}

	public void playUrl(String videoUrl) {
		if (mediaPlayer != null) {
			try {
				mediaPlayer.reset();
				mediaPlayer.setDataSource(videoUrl);
				mediaPlayer.prepareAsync();// prepare之后自动播放
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void playUrl(String videoUrl, int seek) {
		if (mediaPlayer != null) {
			try {
				mediaPlayer.reset();
				mediaPlayer.setDataSource(videoUrl);
				mediaPlayer.prepareAsync();
				this.seek = seek * 1000;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void pause() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.pause();
			if (listener != null) {
				listener.onPauseListener(mediaPlayer.getCurrentPosition() / 1000);
			}
		}
	}

	public void stop() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
			mediaPlayer.reset();
			mediaPlayer.release();
			mediaPlayer = null;
			if (listener != null) {
				listener.onStopListener();
			}
		}
	}

	public void seek(int seekTo) {
		if (mediaPlayer != null) {
			if (!mediaPlayer.isPlaying())
				mediaPlayer.start();
			mediaPlayer.seekTo(seekTo * 1000);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		if (mediaPlayer != null) {
			mediaPlayer.reset();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setDisplay(surfaceHolder);
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setLooping(false);
		mediaPlayer.setOnBufferingUpdateListener(this);
		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.setOnSeekCompleteListener(this);
		if (listener != null) {
			listener.onInited();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		if (mediaPlayer != null) {
			mediaPlayer.reset();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	@Override
	public void onPrepared(MediaPlayer arg0) {
		videoWidth = mediaPlayer.getVideoWidth();
		videoHeight = mediaPlayer.getVideoHeight();
		if (videoHeight != 0 && videoWidth != 0) {
			arg0.start();
			if (seek > 0) {
				arg0.seekTo(seek);
			}
			if (listener != null) {
				listener.onStartListener(arg0.getCurrentPosition());
			}
		}
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		mTimerTask.cancel();
		mTimer.cancel();
		if (mediaPlayer != null) {
			mediaPlayer.reset();
			mediaPlayer.release();
			mediaPlayer = null;
			listener.onCompleteListener();
		}
	}

	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
		skbProgress.setSecondaryProgress(bufferingProgress);
		// int currentProgress = skbProgress.getMax() *
		// mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();
		// Log.e(currentProgress + "% play", bufferingProgress + "% buffer");
		if (listener != null) {
			listener.onLoadProgressChangeListener(bufferingProgress);
		}
	}

	@Override
	public void onSeekComplete(MediaPlayer mediaPlayer) {
		if (mediaPlayer != null) {
			if (listener != null) {
				listener.onSeekListener(mediaPlayer.getCurrentPosition());
			}
		}
	}

	public interface PlayerListener {
		abstract public void onPlayProgressChangeListener(int current, int max);

		abstract public void onLoadProgressChangeListener(int percent);

		abstract public void onCompleteListener();

		abstract public void onStartListener(int start);

		abstract public void onPauseListener(int position);

		abstract public void onStopListener();

		abstract public void onSeekListener(int seekto);

		abstract public void onInited();
	}

}

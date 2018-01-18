package com.spriteapp.videoexample;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class MainActivity extends AppCompatActivity {

    private static final String TEST_VIDEO_URL = "http://wvideo.spriteapp.cn/video/2018/0109/f22ea9a8f4a911e784f8842b2b4c75ab_o4n.mp4";
    private static final String TEST_IMAGE_URL = "http://wimg.spriteapp.cn/picture/2018/0109/f22ea9a8f4a911e784f8842b2b4c75ab_wpd.jpg";
    private static final long HIDE_IMAGE_DELAY = 100;
    private VideoView mVideoView;
    private ImageView mPreviewImageView;
    private int mCurrentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVideoView = findViewById(R.id.video_view);
        mPreviewImageView = findViewById(R.id.preview_image_view);
        //使用glide加载预览图片
        Glide.with(this)
                .load(TEST_IMAGE_URL)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mPreviewImageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //在onStop()方法中显示预览图片
        mPreviewImageView.setVisibility(View.VISIBLE);
        //停止视频播放
        if (mVideoView != null) {
            mCurrentPosition = mVideoView.getCurrentPosition();
            mVideoView.stopPlayback();
        }
    }

    private void startPlay() {
        //设置屏幕常亮
        mVideoView.setKeepScreenOn(true);
        //设置url
        mVideoView.setVideoPath(TEST_VIDEO_URL);
        //播放视频
        mVideoView.start();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                /*
                定位到之前播放的位置,但其实定位位置并恰好是mCurrentPosition，
                因为视频有一个关键帧的概念，只能seek到关键帧的位置，
                所以会定位到离mCurrentPosition之前最近的一个关键帧。
                 */
                if (mCurrentPosition != 0) {
                    mVideoView.seekTo(mCurrentPosition);
                }
                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        /* The player just pushed the very first video frame for rendering.
                         * 视频第一帧开始渲染,视频真正开始播放.
                         * @see android.media.MediaPlayer.OnInfoListener
                         */
                        if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //隐藏预览图片,这里延时100ms消失是防止页面过渡时闪屏
                                    mPreviewImageView.setVisibility(View.INVISIBLE);
                                }
                            }, HIDE_IMAGE_DELAY);
                            return true;
                        }
                        return false;
                    }
                });
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //处理视频播放完成操作
            }
        });
    }
}

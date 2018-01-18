# VideoExample
Tell you how to avoid black screen when VideoView play.
# How to realize?
Showing a preview image before VideoView playing and hide it when MediaPlayer called onInfo listener.
```
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
```
# Want to see more?
[http://blog.csdn.net/kuangxiaoguo0123/article/details/79099984](http://blog.csdn.net/kuangxiaoguo0123/article/details/79099984)

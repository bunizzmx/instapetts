package com.bunizz.instapetts.utils.trimVideo.trim;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;


import com.bunizz.instapetts.utils.trimVideo.interfaces.VideoTrimListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import iknow.android.utils.DeviceUtil;
import iknow.android.utils.UnitConverter;
import iknow.android.utils.callback.SingleCallback;
import iknow.android.utils.thread.BackgroundExecutor;

/**
 * Author：J.Chou
 * Date：  2016.08.01 2:23 PM
 * Email： who_know_me@163.com
 * Describe:
 */
public class VideoTrimmerUtil {
  private static final String TAG = VideoTrimmerUtil.class.getSimpleName();
  public static final long MIN_SHOOT_DURATION = 500L;
  public static final int VIDEO_MAX_TIME = 30;
  public static final long MAX_SHOOT_DURATION = VIDEO_MAX_TIME * 1000L;

  public static final int MAX_COUNT_RANGE = 10;
  private static final int SCREEN_WIDTH_FULL = DeviceUtil.getDeviceWidth();
  public static final int RECYCLER_VIEW_PADDING = UnitConverter.dpToPx(35);
  public static final int VIDEO_FRAMES_WIDTH = SCREEN_WIDTH_FULL - RECYCLER_VIEW_PADDING * 2;
  public static  int THUMB_WIDTH = UnitConverter.dpToPx(90);
  private static  int THUMB_HEIGHT = UnitConverter.dpToPx(90);


  public static void shootVideoThumbInBackground(final Context context, final Uri videoUri, final int totalThumbsCount, final long startPosition,
                                                 final long endPosition, final SingleCallback<Bitmap, Integer> callback) {
    BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0L, "") {
      @Override
      public void execute() {
        try {
          MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
          mediaMetadataRetriever.setDataSource(context, videoUri);
          // Retrieve media data use microsecond
          long interval = (endPosition - startPosition) / (totalThumbsCount - 1);
          for (long i = 0; i < totalThumbsCount; ++i) {
            long frameTime = startPosition + interval * i;
            Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(frameTime * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            if(bitmap == null) continue;
            try {
              if(totalThumbsCount <= 5){
                THUMB_WIDTH = UnitConverter.dpToPx(100);
                THUMB_HEIGHT = UnitConverter.dpToPx(100);
              }
              bitmap = Bitmap.createScaledBitmap(bitmap, THUMB_WIDTH, THUMB_HEIGHT, false);
            } catch (final Throwable t) {
              t.printStackTrace();
            }
            callback.onSingleCallback(bitmap, (int) interval);
          }
          mediaMetadataRetriever.release();
        } catch (final Throwable e) {
          Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
        }
      }
    });
  }


}

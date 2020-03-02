package com.bunizz.instapetts.activitys.searchqr;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.fragments.camera.CameraPreviewStoryFragment;
import com.bunizz.instapetts.fragments.camera.CameraStoryt;
import com.bunizz.instapetts.fragments.qr.FragmentDetectQR;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.utils.ViewExtensionsKt;
import com.bunizz.instapetts.utils.dilogs.DialogQrResult;
import com.bunizz.instapetts.utils.qr.camera.CameraManager;
import com.bunizz.instapetts.utils.qr.decode.CaptureActivityHandler;
import com.bunizz.instapetts.utils.qr.decode.DecodeImageCallback;
import com.bunizz.instapetts.utils.qr.decode.DecodeImageThread;
import com.bunizz.instapetts.utils.qr.decode.DecodeManager;
import com.bunizz.instapetts.utils.qr.decode.InactivityTimer;
import com.google.zxing.Result;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Stack;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;



public class QrSearchActivity extends AppCompatActivity implements changue_fragment_parameters_listener {

    private Stack<FragmentElement> stack_qr;
    private Stack<FragmentElement> stack_qr_my_pet;

    private FragmentElement mCurrentFragment;
    private FragmentElement mOldFragment;

    @BindView(R.id.root_inmersive)
    RelativeLayout root_inmersive;

    private static final float BEEP_VOLUME = 0.10f;
    private static final long VIBRATE_DURATION = 200L;
    private MediaPlayer mMediaPlayer;
    private boolean mPlayBeep;
    private boolean mVibrate;
    private boolean mNeedFlashLightOpen = true;
    private Handler mHandler;
    private InactivityTimer mInactivityTimer;
    private final DecodeManager mDecodeManager = new DecodeManager();
    private static final int REQUEST_SYSTEM_PICTURE = 0;
    private static final int REQUEST_PICTURE = 1;
    public static final int MSG_DECODE_SUCCEED = 1;
    public static final int MSG_DECODE_FAIL = 2;

    private Executor mQrCodeExecutor;
    private final String GOT_RESULT = "com.blikoon.qrcodescanner.got_qr_scan_relult";
    private final String ERROR_DECODING_IMAGE = "com.blikoon.qrcodescanner.error_decoding_image";
    private final String LOGTAG = "QRScannerQRCodeActivity";
    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener mBeepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };



    private CaptureActivityHandler mCaptureActivityHandler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
        changeStatusBarColor(R.color.white);
        stack_qr = new Stack<>();
        stack_qr_my_pet = new Stack<>();
        setupFirstFragment();
        initData();
        if (mCaptureActivityHandler == null) {
            mCaptureActivityHandler = new CaptureActivityHandler(this);
        }
        mHandler = new WeakHandler(this);
    }

    private void initData() {
        CameraManager.init(this);
        mInactivityTimer = new InactivityTimer(this);
        mQrCodeExecutor = Executors.newSingleThreadExecutor();
    }


    public void changeStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), color));
            window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        }
    }
    private void setupFirstFragment() {
        mCurrentFragment = new FragmentElement<>(null, FragmentDetectQR.newInstance(), FragmentElement.INSTANCE_HISTORY_CAMERA, true);
        change_my_qr(mCurrentFragment);
    }

    private void change_my_qr(FragmentElement fragment) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            if(stack_qr_my_pet.size()<=0){stack_qr_my_pet.push(mCurrentFragment);}
        }
        inflateFragment();
    }
    private void change_detect_qr(FragmentElement fragment,Bundle data) {
        if (fragment != null) {
            mCurrentFragment = fragment;
            mCurrentFragment.getFragment().setArguments(data);
            if(stack_qr.size()<=0){stack_qr.push(mCurrentFragment);}
        }
        inflateFragment();
    }


    private void saveFragment() {
        mOldFragment = mCurrentFragment;
    }

    private synchronized void changeOfInstance(int intanceType,Bundle data) {
        saveFragment();
        if (intanceType == FragmentElement.INSTANCE_VIEW_MY_QR) {
            if (stack_qr_my_pet.size() == 0) {
                change_my_qr(new FragmentElement<>("", CameraStoryt.newInstace(), FragmentElement.INSTANCE_VIEW_MY_QR));
            } else {
                change_my_qr(stack_qr_my_pet.pop());
            }
        }else if(intanceType == FragmentElement.INSTANCE_DETECT_QR) {
            if (stack_qr.size() == 0) {
                change_detect_qr(new FragmentElement<>("", FragmentDetectQR.newInstance(), FragmentElement.INSTANCE_DETECT_QR),data);
            } else {
                change_detect_qr(stack_qr.pop(),data);
            }

        }

    }

    @SuppressLint("RestrictedApi")
    private synchronized void inflateFragment() {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if(mOldFragment!=null) {
                if (mCurrentFragment.getFragment().isAdded()) {
                    fragmentManager
                            .beginTransaction()
                            .addToBackStack(null)
                            .hide(mOldFragment.getFragment())
                            .show(mCurrentFragment.getFragment()).commit();
                } else {
                    fragmentManager
                            .beginTransaction()
                            .addToBackStack(null)
                            .hide(mOldFragment.getFragment())
                            .add(R.id.root_login, mCurrentFragment.getFragment()).commit();
                }

            }else{
                fragmentManager
                        .beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.root_login, mCurrentFragment.getFragment()).commit();
            }
        } catch (IllegalStateException ignored) {}
    }

    @Override
    public void onBackPressed() {
        if(mCurrentFragment.getInstanceType() ==  FragmentElement.INSTANCE_HISTORY_CAMERA){
            finish();
        }else{
            changeOfInstance(FragmentElement.INSTANCE_HISTORY_CAMERA,null);
        }

    }

    @Override
    public void change_fragment_parameter(int type_fragment, Bundle data) {
        changeOfInstance(type_fragment,data);
    }


    @Override
    protected void onResume() {
        super.onResume();
        root_inmersive.postDelayed(() -> root_inmersive.setSystemUiVisibility(ViewExtensionsKt.FLAGS_FULLSCREEN ),500);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public Handler getCaptureActivityHandler() {
        return mCaptureActivityHandler;
    }


    public void handleDecode(Result result) {
        mInactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        if (null == result) {
            mDecodeManager.showCouldNotReadQrCodeFromScanner(this, new DecodeManager.OnRefreshCameraListener() {
                @Override
                public void refresh() {
                    restartPreview();
                }
            });
        } else {
            String resultString = result.getText();
            handleResult(resultString);
        }
    }

    private void handleResult(String resultString) {
        if (TextUtils.isEmpty(resultString)) {
            mDecodeManager.showCouldNotReadQrCodeFromScanner(this, new DecodeManager.OnRefreshCameraListener() {
                @Override
                public void refresh() {
                    restartPreview();
                }
            });
        } else {
            DialogQrResult dialogQrResult = new DialogQrResult(this);
            dialogQrResult.show();
            Toast.makeText(this,"CODE" + resultString,Toast.LENGTH_LONG).show();
        }
    }

    private void restartPreview() {
        if (null != mCaptureActivityHandler) {
            mCaptureActivityHandler.restartPreviewAndDecode();
        }
    }

    private void initBeepSound() {
        if (mPlayBeep && mMediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnCompletionListener(mBeepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mMediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mMediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mMediaPlayer.prepare();
            } catch (IOException e) {
                mMediaPlayer = null;
            }
        }
    }

    private void playBeepSoundAndVibrate() {
        if (mPlayBeep && mMediaPlayer != null) {
            mMediaPlayer.start();
        }
        if (mVibrate) {
            Vibrator vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }



    private static class WeakHandler extends Handler {
        private WeakReference<QrSearchActivity> mWeakQrCodeActivity;
        private DecodeManager mDecodeManager = new DecodeManager();

        public WeakHandler(QrSearchActivity imagePickerActivity) {
            super();
            this.mWeakQrCodeActivity = new WeakReference<>(imagePickerActivity);
        }


        @Override
        public void handleMessage(Message msg) {
            QrSearchActivity qrCodeActivity = mWeakQrCodeActivity.get();
            switch (msg.what) {
                case MSG_DECODE_SUCCEED:
                    Result result = (Result) msg.obj;
                    if (null == result) {
                        mDecodeManager.showCouldNotReadQrCodeFromPicture(qrCodeActivity);
                    } else {
                        String resultString = result.getText();
                        handleResult(resultString);
                    }
                    break;
                case MSG_DECODE_FAIL:
                    mDecodeManager.showCouldNotReadQrCodeFromPicture(qrCodeActivity);
                    break;
            }
            super.handleMessage(msg);
        }

        private void handleResult(String resultString) {
            QrSearchActivity imagePickerActivity = mWeakQrCodeActivity.get();

            mDecodeManager.showResultDialog(imagePickerActivity, resultString, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_PICTURE:
                finish();
                break;
            case REQUEST_SYSTEM_PICTURE:
                Uri uri = data.getData();
                String imgPath = getPathFromUri(uri);
                if (imgPath != null && !TextUtils.isEmpty(imgPath) && null != mQrCodeExecutor) {
                    mQrCodeExecutor.execute(new DecodeImageThread(imgPath, mDecodeImageCallback));
                }
                break;
        }
    }


    public String getPathFromUri(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }


    private DecodeImageCallback mDecodeImageCallback = new DecodeImageCallback() {
        @Override
        public void decodeSucceed(Result result) {
            //Got scan result from scaning an image loaded by the user
            Log.d(LOGTAG,"Decoded the image successfully :"+ result.getText());
            Intent data = new Intent();
            data.putExtra(GOT_RESULT,result.getText());
           setResult(RESULT_OK,data);
            finish();
        }

        @Override
        public void decodeFail(int type, String reason) {
            Log.d(LOGTAG,"Something went wrong decoding the image :"+ reason);
            Intent data = new Intent();
            data.putExtra(ERROR_DECODING_IMAGE,reason);
            setResult(Activity.RESULT_CANCELED,data);
            finish();
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if (mCaptureActivityHandler != null) {
            mCaptureActivityHandler.quitSynchronously();
            mCaptureActivityHandler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        if (null != mInactivityTimer) {
            mInactivityTimer.shutdown();
        }
        super.onDestroy();
    }
}

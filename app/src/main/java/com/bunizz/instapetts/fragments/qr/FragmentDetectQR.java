package com.bunizz.instapetts.fragments.qr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.fragments.login.login.FragmentLogin;
import com.bunizz.instapetts.fragments.search.SearchPetAdapter;
import com.bunizz.instapetts.utils.dilogs.DialogQrResult;
import com.bunizz.instapetts.utils.qr.camera.CameraManager;
import com.bunizz.instapetts.utils.qr.decode.CaptureActivityHandler;
import com.bunizz.instapetts.utils.qr.decode.DecodeImageCallback;
import com.bunizz.instapetts.utils.qr.decode.DecodeImageThread;
import com.bunizz.instapetts.utils.qr.decode.DecodeManager;
import com.bunizz.instapetts.utils.qr.decode.InactivityTimer;
import com.bunizz.instapetts.utils.qr.view.QrCodeFinderView;
import com.bunizz.instapetts.utils.tabs.TabType;
import com.google.zxing.Result;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.VIBRATOR_SERVICE;

public class FragmentDetectQR extends Fragment implements Callback {
    private boolean mHasSurface;
    private boolean mPermissionOk;
    private QrCodeFinderView mQrCodeFinderView;
    private SurfaceView mSurfaceView;
    private Context mApplicationContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplicationContext = getActivity();

    }

    public static FragmentDetectQR newInstance() {
        return new FragmentDetectQR();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_qr_code, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mQrCodeFinderView = (QrCodeFinderView) view.findViewById(R.id.qr_code_view_finder);
        mSurfaceView = (SurfaceView) view.findViewById(R.id.qr_code_preview_view);
        mHasSurface = false;

    }


    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException e) {
            Toast.makeText(getContext(), getString(R.string.qr_code_camera_not_found), Toast.LENGTH_SHORT).show();
            getActivity().finish();
            return;
        } catch (RuntimeException re) {
            re.printStackTrace();
            Toast.makeText(getContext(), getString(R.string.qr_code_camera_not_found), Toast.LENGTH_SHORT).show();
            return;
        }
        mQrCodeFinderView.setVisibility(View.VISIBLE);
        mSurfaceView.setVisibility(View.VISIBLE);
        //findViewById(R.id.qr_code_view_background).setVisibility(View.GONE);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    private boolean checkCameraHardWare(Context context) {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!mHasSurface) {
            mHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mHasSurface = false;
    }


    @Override
    public void onResume() {
        super.onResume();
        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        if (mHasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
    }
}

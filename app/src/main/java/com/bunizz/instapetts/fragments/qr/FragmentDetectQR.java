package com.bunizz.instapetts.fragments.qr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import com.bunizz.instapetts.R;
import com.bunizz.instapetts.fragments.FragmentElement;
import com.bunizz.instapetts.listeners.changue_fragment_parameters_listener;
import com.bunizz.instapetts.utils.dilogs.DialogQrResult;
import com.bunizz.instapetts.utils.qr.demo.QRDataListener;
import com.bunizz.instapetts.utils.qr.demo.QREader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.VIBRATOR_SERVICE;
import static com.bunizz.instapetts.fragments.FragmentElement.INSTANCE_PREVIEW_PROFILE;

public class FragmentDetectQR extends Fragment implements Callback {
    private boolean mHasSurface;

    @BindView(R.id.camera_view)
    SurfaceView camera_view;

    changue_fragment_parameters_listener listener;

    @OnClick(R.id.back_to_qr)
    void back_to_qr()
    {
        listener.change_fragment_parameter(FragmentElement.INSTANCE_VIEW_MY_QR,null);
    }


    private Context mApplicationContext;
    private QREader qrEader;
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
        setupQREader();

    }

    void setupQREader() {
        // Init QREader
        // ------------
        qrEader = new QREader.Builder(getContext(), camera_view, data -> {
            Log.e("QREader", "Value : " + data);
            listener.change_fragment_parameter(INSTANCE_PREVIEW_PROFILE,null);
            qrEader.stop();

        }).facing(QREader.BACK_CAM)
                .enableAutofocus(true)
                .height(camera_view.getHeight())
                .width(camera_view.getWidth())
                .build();
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
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mHasSurface = false;
    }


    @Override
    public void onResume() {
        super.onResume();
        qrEader.initAndStart(camera_view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener= (changue_fragment_parameters_listener) context;
    }
}

package com.bunizz.instapetts.utils.dilogs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bunizz.instapetts.R;
import com.bunizz.instapetts.listeners.delete;

import androidx.appcompat.app.AlertDialog;

public class DialogUpdateAvailable extends BaseAlertDialog{
    private boolean allowAnimation = true;
    private boolean cancelable = true;
    private boolean isLocked = false;
    Context context;
    delete listener;
    Button update_app_button;
    TextView next_app_button;
    TextView label_version;
    public delete getListener() {
        return listener;
    }
    public void setListener(delete listener) {
        this.listener = listener;
    }
    public DialogUpdateAvailable(Context context,String version){
        this.context = context;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.context);
        LayoutInflater inflater = LayoutInflater.from(this.context);
        dialogView = inflater.inflate(R.layout.dialog_update_available, null);
        update_app_button = dialogView.findViewById(R.id.update_app_button);
        next_app_button = dialogView.findViewById(R.id.next_app_button);
        label_version = dialogView.findViewById(R.id.label_version);
        label_version.setText(getContext().getString(R.string.update_app_description)  + version );
        update_app_button.setOnClickListener(view -> {
            final String appPackageName = getContext().getPackageName(); // getPackageName() from Context or Activity object
            try {
                getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        });
        next_app_button.setOnClickListener(view -> dismiss());

        dialogBuilder.setView(dialogView);
        dialog = dialogBuilder.create();
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (allowAnimation) dialog.getWindow().getAttributes().windowAnimations = R.style.customDialogAnimation;
    }


    @Override
    public void show(){
        dialog.setCancelable(true);
        dialog.show();
    }

    /**
     * Dismiss the dialog.
     */
    @Override
    public void dismiss(){
        if(!isLocked){
            dialog.dismiss();
        }
    }

    public void setTitleVisable(boolean show){
        try{

        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public Context getContext() {
        return context;
    }


    @Override
    public View getDialogView() {
        return dialogView;
    }

    public void setDialogView(View dialogView) {
        this.dialogView = dialogView;
    }
    @Override
    public AlertDialog getDialog() {
        return dialog;
    }





}

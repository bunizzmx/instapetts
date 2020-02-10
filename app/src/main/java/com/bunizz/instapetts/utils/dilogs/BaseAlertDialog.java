package com.bunizz.instapetts.utils.dilogs;

import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

/**
 * Created by Tony Zaitoun on 7/3/2016.
 */
public abstract class BaseAlertDialog {
     public final static String TAG = "CrofisAlertDialog";

    /**Application Context**/
    Context context;

    /**The View of the dialog**/
    View dialogView;

    /**The Root AlertDialog**/
    AlertDialog dialog;

    public abstract void show();

    public abstract void dismiss();

    public abstract AlertDialog getDialog();

    public abstract View getDialogView();

    public abstract Context getContext();

    public interface OnClickListener {

        void onClick(View v, BaseAlertDialog dialog);
    }
}

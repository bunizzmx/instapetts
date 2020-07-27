package com.bunizz.instapetts.activitys.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Looper;
import android.os.StatFs;
import android.util.Log;


import com.bunizz.instapetts.R;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;
import java.util.Locale;

public class UnCaughtException implements UncaughtExceptionHandler {

    private static Context context1;
    private Context context;

    public UnCaughtException(Context ctx) {
        context = ctx;
        context1 = ctx;
    }

    private StatFs getStatFs() {
        File path = Environment.getDataDirectory();
        return new StatFs(path.getPath());
    }

    private long getAvailableInternalMemorySize(StatFs stat) {
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    private long getTotalInternalMemorySize(StatFs stat) {
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    private void addInformation(StringBuilder message) {
        message.append("");
    }

    public void uncaughtException(Thread t, Throwable e) {
        try {
            Log.e("uncaughtException", "-->" + e.getMessage());

        } catch (Throwable ignore) {
            Log.e(UnCaughtException.class.getName(), context.getString(R.string.app_name), ignore);
        }
    }

    /**
     * Este método llama al diálogo de alerta cuando la aplicación falla
     */
}
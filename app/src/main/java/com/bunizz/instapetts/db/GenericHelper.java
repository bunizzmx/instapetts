package com.bunizz.instapetts.db;

import android.content.Context;
import android.content.Intent;
import android.util.AndroidRuntimeException;
import android.util.Log;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.activitys.Splash;
import com.bunizz.instapetts.utils.AndroidIdentifier;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;

import java.io.OutputStreamWriter;

public class GenericHelper {

    @SuppressWarnings("WeakerAccess")
    protected DataSQLite mHelper;
    private String secret;
    protected Context context;
    OutputStreamWriter archivo;


    public GenericHelper(Context context) {
        this.context = context;
        mHelper = DataSQLite.getInstance(this.context);
        secret = Utilities.Md5Hash(new AndroidIdentifier(   this.context).generateCombinationID());
    }






    public SQLiteDatabase getWritableDatabase() {
        return mHelper.getWritableDatabase(secret);
    }

    public SQLiteDatabase getReadableDatabase() {

            return mHelper.getReadableDatabase(secret);

    }

}

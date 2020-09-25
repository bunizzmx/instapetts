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
        try {
            return mHelper.getReadableDatabase(secret);
        } catch (Exception readableDatabase) {
            Log.e("EXCEPTIOCN","-->" + readableDatabase.getMessage()) ;
            context.deleteDatabase(DataSQLite.DATABASE_NAME);
            Intent homeIntent = new Intent(context, Splash.class);
            try {
                context.startActivity(homeIntent);
            } catch (AndroidRuntimeException ignore) {}
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(homeIntent);
            return null;
        }
    }

}

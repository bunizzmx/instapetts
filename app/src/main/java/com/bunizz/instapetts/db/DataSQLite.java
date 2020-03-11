package com.bunizz.instapetts.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.bunizz.instapetts.R;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;


/**
 * This class create and update FC database for Final Consumer
 */
public class DataSQLite extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "DB_INSTAPETTS";
    public static final int ACTIVE = 0;


    public static final String DATE_SQLITE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @SuppressLint("StaticFieldLeak")
    private static DataSQLite helper;
    private Context context;

    public static synchronized DataSQLite getInstance(Context context) {
        if (helper == null) {
            helper = new DataSQLite(context);
        }

        return helper;
    }

    private DataSQLite(Context context) {
        super(context, DATABASE_NAME, null, 2);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("CREATE_TABLES","--");
        db.execSQL(context.getString(R.string.table_historias_vistas));
        db.execSQL(context.getString(R.string.histories));
        db.execSQL(context.getString(R.string.pets));
        db.execSQL(context.getString(R.string.propietary));
        db.execSQL(context.getString(R.string.table_razas));

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);


    }
}
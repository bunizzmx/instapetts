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

    public static final String DATABASE_NAME = "DB_INSTAPETTS.db";
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
        super(context, DATABASE_NAME, null, 4);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("CREATE_TABLES","--");
        db.execSQL(context.getString(R.string.table_historias_vistas));
        db.execSQL(context.getString(R.string.histories));
        db.execSQL(context.getString(R.string.pets_table));
        db.execSQL(context.getString(R.string.propietary));
        db.execSQL(context.getString(R.string.table_razas));
        db.execSQL(context.getString(R.string.table_saved_posts));
        db.execSQL(context.getString(R.string.table_liked_post));
        db.execSQL(context.getString(R.string.table_notifications));
        db.execSQL(context.getString(R.string.table_relacion_follows));
        db.execSQL(context.getString(R.string.table_search_recents));
        db.execSQL(context.getString(R.string.table_likes_comments));
        db.execSQL(context.getString(R.string.table_likes_tips));
        db.execSQL(context.getString(R.string.table_identificadores_histories));
        db.execSQL(context.getString(R.string.table_ids_users_follows));
        db.execSQL(context.getString(R.string.table_liked_stories));
        db.execSQL(context.getString(R.string.table_temp_video_post));
        db.execSQL(context.getString(R.string.table_play_ideos));

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
        if(oldVersion < 2)
        db.execSQL(context.getString(R.string.update_pets_v1));
        if(oldVersion < 3)
        db.execSQL(context.getString(R.string.update_notificaciones_v1));
        if(oldVersion <4)
        db.execSQL(context.getString(R.string.table_play_ideos));

    }
}
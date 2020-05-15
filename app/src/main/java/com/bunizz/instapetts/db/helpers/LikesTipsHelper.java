package com.bunizz.instapetts.db.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.bunizz.instapetts.db.GenericHelper;

import net.sqlcipher.database.SQLiteConstraintException;

public class LikesTipsHelper  extends GenericHelper {

    private static final String TABLE_NAME = "likes_tips";
    public static final String ID = "id_tip";


    public static LikePostHelper getInstance(Context context) {
        return new LikePostHelper(context);
    }


    public LikesTipsHelper(Context context) {
        super(context);
    }


    public boolean saveLikeTip(int id_post) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, id_post);
        try {
            final Cursor cursor = getReadableDatabase().query(
                    TABLE_NAME,
                    new String[] { ID},
                    ID + "=" + id_post,
                    null, null, null, null, null);
            try {
                if (cursor.moveToFirst()) {
                    Log.e("YA ESTA_REGISTYRADO","---");
                    return true;
                }else{
                    getWritableDatabase().insertOrThrow(TABLE_NAME, null,contentValues);
                    return false;
                }
            } catch (SQLiteConstraintException | IllegalStateException e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

        } catch (SQLiteConstraintException | IllegalStateException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean searchTipsById(int id_post) {
        final Cursor cursor = getReadableDatabase().query(
                TABLE_NAME,
                new String[] { ID},
                ID + "=" + id_post,
                null, null, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                return  true;
            }else{
                return  false;
            }
        } catch (SQLiteConstraintException | IllegalStateException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return true;
    }
}


package com.bunizz.instapetts.db.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.bunizz.instapetts.db.GenericHelper;

import net.sqlcipher.database.SQLiteConstraintException;

public class LikeStoryHelper extends GenericHelper {

    private static final String TABLE_NAME = "liked_stories";
    public static final String ID = "identificador";


    public static LikeStoryHelper getInstance(Context context) {
        return new LikeStoryHelper(context);
    }


    public LikeStoryHelper(Context context) {
        super(context);
    }


    public boolean saveLikeStory(String  identificador) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, identificador);
        try {
            final Cursor cursor = getReadableDatabase().query(
                    TABLE_NAME,
                    new String[] { ID},
                    ID + "='" + identificador+"'",
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

    public boolean isLikedHistory(String identificador) {
        final Cursor cursor = getReadableDatabase().query(
                TABLE_NAME,
                new String[] { ID},
                ID + "='" + identificador+"'",
                null, null, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                return true;
            }else{
                return false;
            }
        } catch (SQLiteConstraintException | IllegalStateException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return false;
    }
}

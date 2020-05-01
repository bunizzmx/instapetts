package com.bunizz.instapetts.db.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.db.GenericHelper;

import net.sqlcipher.database.SQLiteConstraintException;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;

public class ComentariosHelper extends GenericHelper {

    private static final String TABLE_NAME = "likes_comentarios";
    public static final String ID = "id";
    public static final String ID_DOCUMENT = "id_document";

    public static FollowsHelper getInstance(Context context) {
        return new FollowsHelper(context);
    }


    public ComentariosHelper(Context context) {
        super(context);
    }

    public void saveNewLikeComment(String id_comentario) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_DOCUMENT, id_comentario);;
        final Cursor cursor = getReadableDatabase().query(
                TABLE_NAME,
                null,
                "id_document ='" +id_comentario+"'",
                null, null, null, null, null);
        try {
            if(cursor.moveToFirst()){}
            else{
                try {
                    Log.e("SAVE_NEW","save new like comentario");
                    getWritableDatabase().insertOrThrow(TABLE_NAME, null,contentValues);
                } catch (SQLiteConstraintException | IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLiteConstraintException | IllegalStateException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }

    public boolean isLikedComment(String id_comentario) {
        final Cursor cursor = getReadableDatabase().query(
                TABLE_NAME,
                null,
                "id_document='"+id_comentario+"'",
                null, null, null, null, null);
        try {
            if(cursor.moveToFirst()){
                return true;
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
        return false;
    }


    public void cleanTable() {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        try {
            writableDatabase.delete(TABLE_NAME, null, null);
        } catch (SQLiteConstraintException ex) {
            ex.printStackTrace();
        }
    }
}
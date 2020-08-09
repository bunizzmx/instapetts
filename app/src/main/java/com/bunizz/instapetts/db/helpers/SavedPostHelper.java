package com.bunizz.instapetts.db.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.RazaBean;
import com.bunizz.instapetts.db.GenericHelper;

import net.sqlcipher.database.SQLiteConstraintException;

import java.util.ArrayList;

public class SavedPostHelper extends GenericHelper {

    public static SavedPostHelper getInstance(Context context) {
        return new SavedPostHelper(context);
    }


    public SavedPostHelper(Context context) {
        super(context);
    }


    private static final String TABLE_NAME = "saved_posts";
    public static final String ID = "id_from_web";
    public static final String NAME_URLS_POSTS = "urls_posts";
    public static final String NAME_PET = "name_pet";
    public static final String NAME_USER = "name_user";
    public static final String URL_PHOTO_USER = "url_photo_user";
    public static final String DESCRIPTION = "description";
    public static final String DATE_POST = "date_post";


    public void savePost(PostBean postBean) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME_URLS_POSTS, postBean.getUrls_posts());
        contentValues.put(NAME_PET, postBean.getName_pet());
        contentValues.put(NAME_USER, postBean.getName_user());
        contentValues.put(URL_PHOTO_USER, postBean.getUrl_photo_user());
        contentValues.put(DESCRIPTION, postBean.getDescription());
        contentValues.put(DATE_POST, postBean.getDate_post());
        contentValues.put(ID, postBean.getId_post_from_web());
        try {
            getWritableDatabase().insertOrThrow(TABLE_NAME, null,contentValues);
        } catch (SQLiteConstraintException | IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void deleteSavedPost(int id_post){
        getWritableDatabase().delete(TABLE_NAME, ID + "=" +id_post, null) ;
    }


    public boolean searchPostById(int id_post) {
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

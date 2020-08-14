package com.bunizz.instapetts.db.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.bunizz.instapetts.beans.PostBean;
import com.bunizz.instapetts.beans.SearcRecentBean;
import com.bunizz.instapetts.db.GenericHelper;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import net.sqlcipher.database.SQLiteConstraintException;

import java.util.ArrayList;

public class TempPostVideoHelper  extends GenericHelper {

    public static TempPostVideoHelper getInstance(Context context) {
        return new TempPostVideoHelper(context);
    }
    public TempPostVideoHelper(Context context) {
        super(context);
    }

    @SerializedName("url_posts")
    @Expose
    String urls_posts;




    private static final String TABLE_NAME = "temp_video_post";
    public static final String CAN_COMMENT = "can_comment";
    public static final String DURACION = "duracion";
    public static final String ASPECT = "aspect";
    public static final String TYPE_PET = "type_pet";
    public static final String TYPE_POST = "type_post";
    public static final String ID_PET = "id_pet";
    public static final String DATE_POST = "date_post";
    public static final String DESCRIPTION = "description";
    public static final String URL_PHOTO_PET = "url_photo_pet";
    public static final String NAME_PET = "name_pet";
    public static final String THUMBH_VIDEO = "thumb_video";
    public static final String URL_POSTS = "urls_posts";


    public void savePostVideo(PostBean postBean) {
        Cursor cursor=null;
        ContentValues contentValues = new ContentValues();
        contentValues.put(CAN_COMMENT, postBean.getCan_comment());
        contentValues.put(DURACION, postBean.getDuracion());
        contentValues.put(ASPECT, postBean.getAspect());
        contentValues.put(TYPE_PET, postBean.getType_pet());
        contentValues.put(TYPE_POST, postBean.getType_post());
        contentValues.put(ID_PET, postBean.getId_pet());
        contentValues.put(DATE_POST, postBean.getDate_post());
        contentValues.put(DESCRIPTION, postBean.getDescription());
        contentValues.put(URL_PHOTO_PET,postBean.getUrl_photo_pet());
        contentValues.put(NAME_PET,postBean.getName_pet());
        contentValues.put(THUMBH_VIDEO,postBean.getThumb_video());
        contentValues.put(URL_POSTS,postBean.getUrls_posts());
        try {
              cleanTable();
              Log.e("YA_ESTA_LA_BUSQUEDA","NO");
              getWritableDatabase().insertOrThrow(TABLE_NAME, null,contentValues);
        } catch (SQLiteConstraintException | IllegalStateException e) {
            e.printStackTrace();
        }finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public PostBean getPostVideo() {

        PostBean post  = new PostBean();
        final Cursor cursor = getReadableDatabase().query(
                TABLE_NAME,
                null,
                null,
                null, null, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                if (cursor.getColumnIndex(CAN_COMMENT) != -1)
                    post.setCan_comment(cursor.getInt(cursor.getColumnIndex(CAN_COMMENT)));

                if (cursor.getColumnIndex(DURACION) != -1)
                    post.setDuracion(cursor.getInt(cursor.getColumnIndex(DURACION)));

                if (cursor.getColumnIndex(ASPECT) != -1)
                    post.setAspect(cursor.getString(cursor.getColumnIndex(ASPECT)));

                if (cursor.getColumnIndex(TYPE_PET) != -1)
                    post.setType_pet(cursor.getInt(cursor.getColumnIndex(TYPE_PET)));

                if (cursor.getColumnIndex(TYPE_POST) != -1)
                    post.setType_post(cursor.getInt(cursor.getColumnIndex(TYPE_POST)));

                if (cursor.getColumnIndex(ID_PET) != -1)
                    post.setId_pet(cursor.getInt(cursor.getColumnIndex(ID_PET)));

                if (cursor.getColumnIndex(DATE_POST) != -1)
                    post.setDate_post(cursor.getString(cursor.getColumnIndex(DATE_POST)));

                if (cursor.getColumnIndex(DESCRIPTION) != -1)
                    post.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));

                if (cursor.getColumnIndex(URL_PHOTO_PET) != -1)
                    post.setUrl_photo_pet(cursor.getString(cursor.getColumnIndex(URL_PHOTO_PET)));

                if (cursor.getColumnIndex(NAME_PET) != -1)
                    post.setName_pet(cursor.getString(cursor.getColumnIndex(NAME_PET)));

                if (cursor.getColumnIndex(THUMBH_VIDEO) != -1)
                    post.setThumb_video(cursor.getString(cursor.getColumnIndex(THUMBH_VIDEO)));

                if (cursor.getColumnIndex(URL_POSTS) != -1)
                    post.setUrls_posts(cursor.getString(cursor.getColumnIndex(URL_POSTS)));
            }else{
                post = null;
            }
        } catch (SQLiteConstraintException | IllegalStateException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return post;
    }

    public void cleanTable() {
        try {
            getWritableDatabase().delete(TABLE_NAME, null, null);
        } catch (SQLiteConstraintException ex) {
            ex.printStackTrace();
        }
    }

}

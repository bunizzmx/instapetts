package com.bunizz.instapetts.db.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.RazaBean;
import com.bunizz.instapetts.db.GenericHelper;

import net.sqlcipher.database.SQLiteConstraintException;

import java.util.ArrayList;

public class MyStoryHelper extends GenericHelper {

    private static final String TABLE_NAME = "histories";
    public static final String ID = "id";
    public static final String FECHA = "fecha";
    public static final String NOMBRE_PET = "nombre_pet";
    public static final String ID_PET = "id_pet";
    public static final String ID_USER = "id_propietary";
    public static final String URIS_FOTOS = "uris_photos";


    public static MyStoryHelper getInstance(Context context) {
        return new MyStoryHelper(context);
    }


    public MyStoryHelper(Context context) {
        super(context);
    }

    public void saveMyStory(HistoriesBean historiesBean) {
        Log.e("SAVE_MY_STORY",":)" + historiesBean.getUris_stories());
        ContentValues contentValues = new ContentValues();
        contentValues.put(FECHA, historiesBean.getDate_story());
        contentValues.put(NOMBRE_PET, historiesBean.getName_pet());
        contentValues.put(ID_PET, historiesBean.getId_pet());
        contentValues.put(ID_USER, historiesBean.getId_user());
        contentValues.put(URIS_FOTOS, historiesBean.getUris_stories());
        try {
            getWritableDatabase().insertOrThrow(TABLE_NAME, null,contentValues);
        } catch (SQLiteConstraintException | IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<HistoriesBean> getMyStories() {
        ArrayList<HistoriesBean> histories = new ArrayList<>();
        final Cursor cursor = getReadableDatabase().query(
                TABLE_NAME,
                null,
                null,
                null, null, null, null, null);
        try {
            while (cursor.moveToNext()) {
                  HistoriesBean h = new HistoriesBean();
                  h.setDate_story(cursor.getString(cursor.getColumnIndex(FECHA)));
                  h.setName_pet(cursor.getString(cursor.getColumnIndex(NOMBRE_PET)));
                  h.setId_pet(cursor.getInt(cursor.getColumnIndex(ID_PET)));
                  h.setId_user(cursor.getInt(cursor.getColumnIndex(ID_USER)));
                  h.setUris_stories(cursor.getString(cursor.getColumnIndex(URIS_FOTOS)));
                  histories.add(h);
            }
        } catch (SQLiteConstraintException | IllegalStateException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.e("RESULTADOS_BUSQUEDA","HISTORIES-->" + histories.size());
        return histories;
    }
}

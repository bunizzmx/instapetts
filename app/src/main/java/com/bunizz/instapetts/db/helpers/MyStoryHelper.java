package com.bunizz.instapetts.db.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.IndividualDataPetHistoryBean;
import com.bunizz.instapetts.beans.RazaBean;
import com.bunizz.instapetts.constantes.PREFERENCES;
import com.bunizz.instapetts.db.GenericHelper;

import net.sqlcipher.database.SQLiteConstraintException;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;

public class MyStoryHelper extends GenericHelper {

    private static final String TABLE_NAME = "histories";
    public static final String ID = "id";
    public static final String FECHA = "fecha";
    public static final String NOMBRE_PET = "nombre_pet";
    public static final String ID_PET = "id_pet";
    public static final String URIS_FOTO_HISTORY = "uri_photo";
    public static final String URIS_FOTO_PET = "uri_perfil";
    public static final String IDENTIFICADOR = "identificador";

    public static MyStoryHelper getInstance(Context context) {
        return new MyStoryHelper(context);
    }


    public MyStoryHelper(Context context) {
        super(context);
    }

    public void saveMyStory(IndividualDataPetHistoryBean historiesBean) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FECHA, historiesBean.getDate_story());
        contentValues.put(NOMBRE_PET, historiesBean.getName_pet());
        contentValues.put(ID_PET, historiesBean.getId_pet());
        contentValues.put(URIS_FOTO_HISTORY, historiesBean.getUrl_photo());
        contentValues.put(URIS_FOTO_PET, historiesBean.getPhoto_pet());
        contentValues.put(IDENTIFICADOR, historiesBean.getIdentificador());

        try {
            getWritableDatabase().insertOrThrow(TABLE_NAME, null,contentValues);
        } catch (SQLiteConstraintException | IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<IndividualDataPetHistoryBean> getMyStories() {
        ArrayList<IndividualDataPetHistoryBean> histories = new ArrayList<>();
        final Cursor cursor = getReadableDatabase().query(
                TABLE_NAME,
                null,
                null,
                null, null, null, null, null);
        try {
            while (cursor.moveToNext()) {
                IndividualDataPetHistoryBean h = new IndividualDataPetHistoryBean();
                  h.setDate_story(cursor.getString(cursor.getColumnIndex(FECHA)));
                  h.setName_pet(cursor.getString(cursor.getColumnIndex(NOMBRE_PET)));
                  h.setId_pet(cursor.getInt(cursor.getColumnIndex(ID_PET)));
                  h.setUrl_photo(cursor.getString(cursor.getColumnIndex(URIS_FOTO_HISTORY)));
                  h.setPhoto_pet(cursor.getString(cursor.getColumnIndex(URIS_FOTO_PET)));
                  h.setIdentificador(cursor.getString(cursor.getColumnIndex(IDENTIFICADOR)));
                  histories.add(h);
            }
        } catch (SQLiteConstraintException | IllegalStateException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return histories;
    }

    public void cleanTable() {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        try {
            writableDatabase.delete(TABLE_NAME, null, null);
        } catch (SQLiteConstraintException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteHistory(String id){
       Log.e("LOG_DBSSS","->" + getWritableDatabase().delete(TABLE_NAME, IDENTIFICADOR + "=?",  new String[]{id}));
    }
}

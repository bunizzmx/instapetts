package com.bunizz.instapetts.db.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.beans.RazaBean;
import com.bunizz.instapetts.beans.SearcRecentBean;
import com.bunizz.instapetts.db.GenericHelper;

import net.sqlcipher.database.SQLiteConstraintException;

import java.net.IDN;
import java.util.ArrayList;

public class SearchResentHelper extends GenericHelper {

    public static SearchResentHelper getInstance(Context context) {
        return new SearchResentHelper(context);
    }


    public SearchResentHelper(Context context) {
        super(context);
    }


    private static final String TABLE_NAME = "search_recents";
    public static final String ID = "id";
    public static final String URL = "url_photo";
    public static final String NAME = "name";
    public static final String NAME_TAG = "name_tag";
    public static final String TYPE_PET = "type_mascota";
    public static final String RAZA_MASCOTA = "name_raza";
    public static final String TYPE_SAVE = "type_save";
    public static final String UUID_USUARIO = "uuid_usuario";
    public static final String ID_USUARIO = "id_usuario";

    public static final String NAME_PET = "name_pet";
    public static final String ID_MASCOTA = "id_mascota";


    public void saveSearch(SearcRecentBean search) {
        Cursor cursor=null;
        Log.e("SAVE_SEARCH",":)" + search.getName_tag() + "/id_usuario:" + search.getId_usuario() + "/id_mascota:" + search.getId_mascota()
               + "/name_mascota:" + search.getName_pet()
    +"/name_user:" + search.getName()
        +"/raza:" + search.getName_raza() );
        ContentValues contentValues = new ContentValues();
        contentValues.put(URL, search.getUrl_photo());
        contentValues.put(TYPE_PET, search.getType_mascota());
        contentValues.put(NAME, search.getName());
        contentValues.put(RAZA_MASCOTA, search.getName_raza());
        contentValues.put(TYPE_SAVE, search.getType_save());
        contentValues.put(ID_USUARIO, search.getId_usuario());
        contentValues.put(UUID_USUARIO, search.getUuid_usuario());
        contentValues.put(NAME_PET, search.getName_pet());
        contentValues.put(ID_MASCOTA, search.getId_mascota());
        contentValues.put(NAME_TAG,search.getName_tag());
        try {
            if(search.getType_save() == 2) {
                cursor = getReadableDatabase().query(
                        TABLE_NAME,
                        null,
                        "name_pet ='" + search.getName_pet()+"'",
                        null, null, null, null, null);
            }else{
                cursor = getReadableDatabase().query(
                        TABLE_NAME,
                        null,
                        "id_usuario =" + search.getId_usuario(),
                        null, null, null, null, null);
            }
            if(cursor.moveToFirst()){
               Log.e("YA_ESTA_LA_BUSQUEDA","SI");
            }else{
                Log.e("YA_ESTA_LA_BUSQUEDA","NO");
                getWritableDatabase().insertOrThrow(TABLE_NAME, null,contentValues);
            }
        } catch (SQLiteConstraintException | IllegalStateException e) {
            e.printStackTrace();
        }finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


   public  void deleteSearch(int id_item,boolean is_user){
        if(is_user){
            getWritableDatabase().delete(TABLE_NAME, ID_USUARIO + "=" +id_item, null) ;
        }else{
            getWritableDatabase().delete(TABLE_NAME, ID_MASCOTA + "=" +id_item, null) ;
        }
    }


    public ArrayList<SearcRecentBean> getMySearchRecent(int type_Search) {

        ArrayList<SearcRecentBean> seacrhs = new ArrayList<>();
        final Cursor cursor = getReadableDatabase().query(
                TABLE_NAME,
                null,
                "type_save =" + type_Search,
                null, null, null, null, null);
        try {
            while (cursor.moveToNext()) {
                SearcRecentBean h ;
                h = new SearcRecentBean(cursor);
                seacrhs.add(h);
            }
        } catch (SQLiteConstraintException | IllegalStateException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.e("GETSEARCH_RECENTS",":)" +seacrhs.size());
        return seacrhs;
    }


    public void cleanTable() {
        try {
            getWritableDatabase().delete(TABLE_NAME, null, null);
        } catch (SQLiteConstraintException ex) {
            ex.printStackTrace();
        }
    }

}


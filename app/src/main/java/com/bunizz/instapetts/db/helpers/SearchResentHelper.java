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
    public static final String TYPE_PET = "type_mascota";
    public static final String RAZA_MASCOTA = "name_raza";
    public static final String TYPE_SAVE = "type_save";
    public static final String UUID_USUARIO = "uuid_usuario";
    public static final String ID_USUARIO = "id_usuario";

    public static final String NAME_PET = "name_pet";
    public static final String ID_MASCOTA = "id_mascota";


    public void saveSearch(SearcRecentBean search) {
        Log.e("SAVE_SEARCH",":)");
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
        try {
            getWritableDatabase().insertOrThrow(TABLE_NAME, null,contentValues);
        } catch (SQLiteConstraintException | IllegalStateException e) {
            e.printStackTrace();
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


package com.bunizz.instapetts.db.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.bunizz.instapetts.beans.RazaBean;
import com.bunizz.instapetts.db.GenericHelper;

import net.sqlcipher.database.SQLiteConstraintException;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;

public class SearchRazaHelper extends GenericHelper {

    public static SearchRazaHelper getInstance(Context context) {
        return new SearchRazaHelper(context);
    }


    public SearchRazaHelper(Context context) {
        super(context);
    }


    private static final String TABLE_NAME = "razas";
    public static final String ID = "id";
    public static final String NAME_ENG = "name_eng";
    public static final String NAME_ESP = "nam_esp";
    public static final String TYPE_PET = "id_type_pet";
    public static final String PHOTO = "photo";


    public void saveRaza(RazaBean raza) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME_ENG, raza.getName_raza_eng());
        contentValues.put(NAME_ESP, raza.getName_raza_esp());
        contentValues.put(TYPE_PET, raza.getId_type_pet());
        contentValues.put(PHOTO, raza.getUrl_photo());
        try {
            getWritableDatabase().insertOrThrow(TABLE_NAME, null,contentValues);
        } catch (SQLiteConstraintException | IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<RazaBean> getRazas(String querytext) {
        Log.e("CATEGORIA_FILTRADa","-->" + querytext);
        ArrayList<RazaBean> razas = new ArrayList<>();
        razas.clear();
        final Cursor cursor = getReadableDatabase().query(
                TABLE_NAME,
                new String[] { NAME_ESP, NAME_ENG,TYPE_PET,PHOTO},
                NAME_ESP + " LIKE '%" + querytext + "%'",
                null, null, null, null, null);
        try {
            while (cursor.moveToNext()) {
                razas.add(new RazaBean(
                        cursor.getString(cursor.getColumnIndex(NAME_ESP)),
                        cursor.getString(cursor.getColumnIndex(NAME_ENG)),
                        cursor.getInt(cursor.getColumnIndex(TYPE_PET)),
                        cursor.getString(cursor.getColumnIndex(PHOTO))
                ));
            }
        } catch (SQLiteConstraintException | IllegalStateException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return razas;
    }

    public void cleanTable() {
        try {
            getWritableDatabase().delete(TABLE_NAME, null, null);
        } catch (SQLiteConstraintException ex) {
            ex.printStackTrace();
        }
    }

    }


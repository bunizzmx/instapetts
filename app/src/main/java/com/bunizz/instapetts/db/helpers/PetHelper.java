package com.bunizz.instapetts.db.helpers;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.db.GenericHelper;

import net.sqlcipher.database.SQLiteConstraintException;
import net.sqlcipher.database.SQLiteDatabase;

public class PetHelper extends GenericHelper {

    public static PetHelper getInstance(Context context){
        return new PetHelper(context);
    }


    public PetHelper(Context context) {
        super(context);
    }

    private static final String TABLE_NAME = "pets";
    public static final String ID = "id";
    public static final String ID_PET = "id_pet";
    public static final String NAME_PET = "name_pet";
    public static final String ID_PROPIETARY = "id_propietary";
    public static final String NAME_PROPIETARY = "name_propietary";
    public static final String PESO_PET = "peso";
    public static final String RAZA_PET = "raza";
    public static final String TYPE_PET = "type_pet";
    public static final String COLOR_PET = "color";
    public static final String GENERO_PET = "genero";
    public static final String DESCRIPCION_PET = "descripcion";
    public static final String EDAD_PET = "edad";
    public static final String RATING_PET = "rating";
    public static final String URL_PHOTO = "url_hoto";


    public void savePet(PetBean pet) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        try {
            writableDatabase.insertOrThrow(TABLE_NAME, null, pet.toContentValues());
        } catch (SQLiteConstraintException | IllegalStateException e) {
            e.printStackTrace();
        }
    }




   /* public int getVotoby_id(String id) {
        Log.e("CATEGORIA_FILTRADa","-->" + id);
        String[] columns = {ID_DOCUMENT,TIPO_VOTO};
        String selection = "id_document =?";
        String[] selectionArgs = {id};
        final SQLiteDatabase readableDatabase = getReadableDatabase();
        final Cursor cursor = readableDatabase.query(TABLE_NAME, columns,  selection, selectionArgs, null,
                null, null);
        try {
            while (cursor.moveToFirst()) {
              return cursor.getInt(cursor.getColumnIndex(TIPO_VOTO));
            }
        } catch (SQLiteConstraintException | IllegalStateException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }*/

    public void cleanTable() {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        try {
            writableDatabase.delete(TABLE_NAME, null, null);
        } catch (SQLiteConstraintException ex) {
            ex.printStackTrace();
        }
    }
}

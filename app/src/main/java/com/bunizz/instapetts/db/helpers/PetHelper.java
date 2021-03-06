package com.bunizz.instapetts.db.helpers;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.PetBean;
import com.bunizz.instapetts.db.GenericHelper;

import net.sqlcipher.database.SQLiteConstraintException;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;

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
    public static final String GENERO_PET = "genero";
    public static final String DESCRIPCION_PET = "descripcion";
    public static final String EDAD_PET = "edad";
    public static final String RATING_PET = "rating";
    public static final String URL_PHOTO = "url_foto";
    public static final String URL_PHOTO_THUMBH = "url_photo_tumbh";



    public void savePet(PetBean pet) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        try {
            writableDatabase.insertOrThrow(TABLE_NAME, null, pet.toContentValues());
        } catch (SQLiteConstraintException | IllegalStateException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<PetBean> getMyPets() {
        ArrayList<PetBean> pets = new ArrayList<>();
        final Cursor cursor = getReadableDatabase().query(
                TABLE_NAME,
                null,
                null,
                null, null, null, null, null);
        try {
            while (cursor.moveToNext()) {
                PetBean h ;
                h = new PetBean(cursor);
                pets.add(h);
            }
        } catch (SQLiteConstraintException | IllegalStateException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return pets;
    }

    public PetBean getPetByid(String id) {
        Log.e("PET_NAME","-->" + id);
       PetBean pet = null;
        String selection = "name_pet =?";
        String[] selectionArgs = {String.valueOf(id)};
        final Cursor cursor = getReadableDatabase().query(
                TABLE_NAME,
                null,
                selection,
                selectionArgs , null, null, null, null);
        Log.e("SI_HALLE_PET","xxxsi" +cursor.getCount());
        try {
            if (cursor.moveToFirst()) {
                Log.e("SI_HALLE_PET","si");
                pet = new PetBean(cursor);
                Log.e("SI_HALLE_PET","aaaaaaaaaa" +pet.getId_pet());
            }else{
                Log.e("SI_HALLE_PET","no");
                pet = null;
            }
        } catch (SQLiteConstraintException | IllegalStateException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return pet;
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

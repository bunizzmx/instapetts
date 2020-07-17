package com.bunizz.instapetts.db.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.bunizz.instapetts.beans.IdentificadoresHistoriesBean;
import com.bunizz.instapetts.beans.IndividualDataPetHistoryBean;
import com.bunizz.instapetts.db.GenericHelper;

import net.sqlcipher.database.SQLiteConstraintException;

import java.util.ArrayList;

public class IdentificadoresHistoriesHelper extends GenericHelper {

    private static final String TABLE_NAME = "identificadores_histories";
    public static final String IDENTIFICADOR = "identificador";
    public static final String NUM_LIKES = "num_likes";
    public static final String NUM_VIEWS = "num_views";


    public static IdentificadoresHistoriesHelper getInstance(Context context) {
        return new IdentificadoresHistoriesHelper(context);
    }


    public IdentificadoresHistoriesHelper(Context context) {
        super(context);
    }


    public boolean saveIdentificador(IdentificadoresHistoriesBean indentificador) {
        try {
            final Cursor cursor = getReadableDatabase().query(
                    TABLE_NAME,
                    new String[] {IDENTIFICADOR},
                    IDENTIFICADOR + "='" + indentificador.getIdentificador()+"'",
                    null, null, null, null, null);
            try {
                if (cursor.moveToFirst()) {
                    ContentValues valores = new ContentValues();
                    valores.put(NUM_LIKES, indentificador.getNum_likes());
                    valores.put(NUM_VIEWS, indentificador.getNum_views());
                    getWritableDatabase().update(TABLE_NAME,valores,"identificador='"+indentificador.getIdentificador()+"'",null);
                    return true;
                }else{
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(IDENTIFICADOR, indentificador.getIdentificador());
                    contentValues.put(NUM_LIKES, indentificador.getNum_likes());
                    contentValues.put(NUM_VIEWS, indentificador.getNum_views());
                    getWritableDatabase().insertOrThrow(TABLE_NAME, null,contentValues);
                    return false;
                }
            } catch (SQLiteConstraintException | IllegalStateException e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (SQLiteConstraintException | IllegalStateException e) {
            e.printStackTrace();
        }
        return false;
    }

    public IdentificadoresHistoriesBean searchIdentidicadorById(String identificador) {
        IdentificadoresHistoriesBean identificadoresHistoriesBean = new IdentificadoresHistoriesBean();
        final Cursor cursor = getReadableDatabase().query(
                TABLE_NAME,
                 new String[] { IDENTIFICADOR,NUM_LIKES,NUM_VIEWS},
                IDENTIFICADOR + "='" + identificador+"'",
                null, null, null, null, null);
        try {
            while (cursor.moveToNext()) {
                identificadoresHistoriesBean.setIdentificador(cursor.getString(cursor.getColumnIndex(IDENTIFICADOR)));
                identificadoresHistoriesBean.setNum_likes(cursor.getInt(cursor.getColumnIndex(NUM_LIKES)));
                identificadoresHistoriesBean.setNum_views(cursor.getInt(cursor.getColumnIndex(NUM_VIEWS)));
            }
        } catch (SQLiteConstraintException | IllegalStateException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return identificadoresHistoriesBean;
    }


}


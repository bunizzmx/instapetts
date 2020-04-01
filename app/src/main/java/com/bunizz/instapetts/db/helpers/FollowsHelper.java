package com.bunizz.instapetts.db.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.UserBean;
import com.bunizz.instapetts.db.GenericHelper;

import net.sqlcipher.database.SQLiteConstraintException;

import java.util.ArrayList;

public class FollowsHelper extends GenericHelper {

    private static final String TABLE_NAME = "relacion_follows";
    public static final String ID = "id";
    public static final String URL_PHOTO = "url_photo_user";
    public static final String UUID = "uuid_user";
    public static final String ID_USER = "id_user";
    public static final String NAME_NIP_USER = "name_nip_user";


    public static FollowsHelper getInstance(Context context) {
        return new FollowsHelper(context);
    }


    public FollowsHelper(Context context) {
        super(context);
    }

    public void saveNewFriend(UserBean userBean) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(URL_PHOTO, userBean.getPhoto_user_thumbh());
        contentValues.put(UUID, userBean.getUuid());
        contentValues.put(NAME_NIP_USER, userBean.getName_user());
        contentValues.put(ID_USER, userBean.getId());

        Log.e("SELECTIONS_aRGS","-->" + userBean.getUuid());
        final Cursor cursor = getReadableDatabase().query(
                TABLE_NAME,
                null,
                "uuid_user ='" +userBean.getUuid()+"'",
                null, null, null, null, null);
        try {
            if(cursor.moveToFirst()){}
            else{
                try {
                    Log.e("SAVE_NEW","save_new_user");
                    getWritableDatabase().insertOrThrow(TABLE_NAME, null,contentValues);
                } catch (SQLiteConstraintException | IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLiteConstraintException | IllegalStateException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }

    public ArrayList<Integer> getMyFriendsForPost() {
        ArrayList<Integer> ids_users = new ArrayList<>();
        final Cursor cursor = getReadableDatabase().query(
                TABLE_NAME,
                null,
                null,
                null, null, null, null, null);
        try {
            while (cursor.moveToNext()) {
                ids_users.add(cursor.getInt(cursor.getColumnIndex(ID_USER)));
            }
        } catch (SQLiteConstraintException | IllegalStateException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.e("RESULTADOS_BUSQUEDA","HISTORIES-->" + ids_users.size());
        return ids_users;
    }
}


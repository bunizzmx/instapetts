package com.bunizz.instapetts.db.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.NotificationBean;
import com.bunizz.instapetts.db.GenericHelper;

import net.sqlcipher.database.SQLiteConstraintException;

import java.util.ArrayList;

public class NotificationHelper extends GenericHelper {

    private static final String TABLE_NAME = "notifications";
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String BODY = "body";
    public static final String TYPE_NOTIFICACION = "type_notification";
    public static final String ID_USUARIO = "id_usuario";
    public static final String URL_RESOURCE = "url_resource";



    public static NotificationHelper getInstance(Context context) {
        return new NotificationHelper(context);
    }

    public NotificationHelper(Context context) {
        super(context);
    }

    public void saveNotification(NotificationBean notificationBean) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE, notificationBean.getTitle());
        contentValues.put(BODY, notificationBean.getBody());
        contentValues.put(TYPE_NOTIFICACION, notificationBean.getType_notification());
        contentValues.put(ID_USUARIO, notificationBean.getId_usuario());
        contentValues.put(URL_RESOURCE, notificationBean.getUrl_resource());
        try {
            getWritableDatabase().insertOrThrow(TABLE_NAME, null,contentValues);
        } catch (SQLiteConstraintException | IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<NotificationBean> getAllNotifications() {
        ArrayList<NotificationBean> notifications = new ArrayList<>();
        final Cursor cursor = getReadableDatabase().query(
                TABLE_NAME,
                null,
                null,
                null, null, null, null, null);
        try {
            while (cursor.moveToNext()) {
                NotificationBean n = new NotificationBean();
                n.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                n.setBody(cursor.getString(cursor.getColumnIndex(BODY)));
                n.setId_usuario(cursor.getInt(cursor.getColumnIndex(ID_USUARIO)));
                n.setType_notification(cursor.getInt(cursor.getColumnIndex(TYPE_NOTIFICACION)));
                n.setUrl_resource(cursor.getString(cursor.getColumnIndex(URL_RESOURCE)));
                notifications.add(n);
            }
        } catch (SQLiteConstraintException | IllegalStateException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return notifications;
    }
}

package com.bunizz.instapetts.db.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.bunizz.instapetts.App;
import com.bunizz.instapetts.beans.HistoriesBean;
import com.bunizz.instapetts.beans.NotificationBean;
import com.bunizz.instapetts.constantes.PREFERENCES;
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
    public static final String URL_EXTRA_IMAGE = "url_image_extra";
    public static final String NOTIFICATION_VIEW = "vista";
    public static final String FECHA = "fecha";
    public static final String ID_DOCUMENTO_NOTIFICATION = "id_document_notification";
    public static final String NOTIFICATION_SYNC = "sincronizada";

    public static NotificationHelper getInstance(Context context) {
        return new NotificationHelper(context);
    }

    public NotificationHelper(Context context) {
        super(context);
    }

    public void saveNotification(NotificationBean notificationBean) {
        switch (notificationBean.getType_notification()){
            case 0:
                if(App.read(PREFERENCES.PUSH_ME_GUSTAS,true))
                    save(notificationBean);
                break;
        }

    }

    void save(NotificationBean notificationBean){
        try {
            final Cursor cursor = getReadableDatabase().query(
                    TABLE_NAME,
                    new String[] {NOTIFICATION_SYNC},
                    ID_DOCUMENTO_NOTIFICATION + "='" + notificationBean.getId_document_notification()+"'",
                    null, null, null, null, null);
            try {
                if (!cursor.moveToFirst()) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(TITLE, notificationBean.getTitle());
                    contentValues.put(BODY, notificationBean.getBody());
                    contentValues.put(TYPE_NOTIFICACION, notificationBean.getType_notification());
                    contentValues.put(ID_USUARIO, notificationBean.getId_usuario());
                    contentValues.put(URL_RESOURCE, notificationBean.getUrl_resource());
                    contentValues.put(URL_EXTRA_IMAGE, notificationBean.getUrl_image_extra());
                    contentValues.put(ID_DOCUMENTO_NOTIFICATION,notificationBean.getId_document_notification());
                    contentValues.put(FECHA,notificationBean.getFecha());
                    contentValues.put(NOTIFICATION_SYNC,0);
                    contentValues.put(NOTIFICATION_VIEW,0);

                    try {
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
        } catch (SQLiteConstraintException | IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public  NotificationBean getLastNotifications(){
        final Cursor cursor = getReadableDatabase().query(
                TABLE_NAME,
                null,
                null,
                null, null, null, null, "2");
        try {
            if(cursor.moveToFirst()){
                NotificationBean n = new NotificationBean();
                Log.e("ONCREATE_APP","SERVICE SAVE ON DB : " + cursor.getInt(cursor.getColumnIndex(ID)));
                n.setId_database(cursor.getInt(cursor.getColumnIndex(ID)));
                n.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                n.setBody(cursor.getString(cursor.getColumnIndex(BODY)));
                n.setId_usuario(cursor.getInt(cursor.getColumnIndex(ID_USUARIO)));
                n.setType_notification(cursor.getInt(cursor.getColumnIndex(TYPE_NOTIFICACION)));
                n.setUrl_resource(cursor.getString(cursor.getColumnIndex(URL_RESOURCE)));
                n.setUrl_image_extra(cursor.getString(cursor.getColumnIndex(URL_EXTRA_IMAGE)));
                n.setId_document_notification(cursor.getString(cursor.getColumnIndex(ID_DOCUMENTO_NOTIFICATION)));
                n.setFecha(cursor.getString(cursor.getColumnIndex(FECHA)));
                return  n;
            }
        } catch (SQLiteConstraintException | IllegalStateException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

  public  ArrayList<String>  getNotificationForSincronized(){
        ArrayList<String> ids= new ArrayList<>();
      final Cursor cursor = getReadableDatabase().query(
              TABLE_NAME,
              new String[] {ID_DOCUMENTO_NOTIFICATION},
              NOTIFICATION_SYNC  + "=0",
              null, null, null, null, null);
      try {
          while (cursor.moveToNext()) {
              Log.e("ONCREATE_APP","SERVICE SAVE ON DB : " + cursor.getString(cursor.getColumnIndex(ID_DOCUMENTO_NOTIFICATION)));
              ids.add(cursor.getString(cursor.getColumnIndex(ID_DOCUMENTO_NOTIFICATION)));
              updateViews();
          }
      } catch (SQLiteConstraintException | IllegalStateException e) {
          e.printStackTrace();
      } finally {
          if (cursor != null) {
              cursor.close();
          }
      }
      return ids;
    }

    public void updateSync(String ID_DOCUMENT){
        ContentValues valores = new ContentValues();
        valores.put(NOTIFICATION_SYNC, 1);
        getWritableDatabase().update(TABLE_NAME,valores,ID_DOCUMENTO_NOTIFICATION + "='"+ID_DOCUMENT+"'",null);
    }

    public void deleteNotificacion(int id){
        Log.e("DELETE_NOTIFICATION","--->" + id);
        getWritableDatabase().delete(TABLE_NAME, ID + "=" +id, null) ;
    }

    public void deleteAll(){
        Log.e("DELETE_NOTIFICATION","---> All");
        getWritableDatabase().delete(TABLE_NAME, null, null) ;
    }

    public void updateViews(){
        Log.e("UPDATEALL","---> All");
        getWritableDatabase().execSQL("UPDATE "+TABLE_NAME+"  SET vista=1");
    }

    public boolean getNoViewsNotifications(){
        Log.e("UPDATEALL","---> All");
        final Cursor cursor = getReadableDatabase().query(
                TABLE_NAME,
                new String[] { NOTIFICATION_VIEW},
                NOTIFICATION_VIEW + "=1",
                null, null, null, null, null);
       if(cursor.moveToFirst())
           return  true;
       else
           return  false;
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
                Log.e("ONCREATE_APP","SERVICE SAVE ON DB : " + cursor.getInt(cursor.getColumnIndex(ID)));
                n.setId_database(cursor.getInt(cursor.getColumnIndex(ID)));
                n.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                n.setBody(cursor.getString(cursor.getColumnIndex(BODY)));
                n.setId_usuario(cursor.getInt(cursor.getColumnIndex(ID_USUARIO)));
                n.setType_notification(cursor.getInt(cursor.getColumnIndex(TYPE_NOTIFICACION)));
                n.setUrl_resource(cursor.getString(cursor.getColumnIndex(URL_RESOURCE)));
                n.setUrl_image_extra(cursor.getString(cursor.getColumnIndex(URL_EXTRA_IMAGE)));
                n.setId_document_notification(cursor.getString(cursor.getColumnIndex(ID_DOCUMENTO_NOTIFICATION)));
                n.setFecha(cursor.getString(cursor.getColumnIndex(FECHA)));
                notifications.add(n);
                updateViews();
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

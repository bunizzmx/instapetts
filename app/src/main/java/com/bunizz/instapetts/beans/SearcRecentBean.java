package com.bunizz.instapetts.beans;

import android.content.ContentValues;
import android.database.Cursor;

import com.bunizz.instapetts.db.helpers.PetHelper;
import com.bunizz.instapetts.db.helpers.SearchResentHelper;

public class SearcRecentBean {


    String url_photo;
    String name;
    String name_tag;
    String name_pet;
    int id_mascota;
    int type_mascota;
    int type_save;
    String  name_raza;
    String uuid_usuario;
    int id_usuario;

    public SearcRecentBean() {
    }

    public SearcRecentBean(String url_photo, String name, int type_mascota, int type_save, String name_raza) {
        this.url_photo = url_photo;
        this.name = name;
        this.type_mascota = type_mascota;
        this.type_save = type_save;
        this.name_raza = name_raza;
    }

    public String getUrl_photo() {
        return url_photo;
    }

    public void setUrl_photo(String url_photo) {
        this.url_photo = url_photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType_mascota() {
        return type_mascota;
    }

    public void setType_mascota(int type_mascota) {
        this.type_mascota = type_mascota;
    }

    public int getType_save() {
        return type_save;
    }

    public void setType_save(int type_save) {
        this.type_save = type_save;
    }

    public String getName_raza() {
        return name_raza;
    }

    public void setName_raza(String name_raza) {
        this.name_raza = name_raza;
    }

    public String getUuid_usuario() {
        return uuid_usuario;
    }

    public void setUuid_usuario(String uuid_usuario) {
        this.uuid_usuario = uuid_usuario;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getName_pet() {
        return name_pet;
    }

    public void setName_pet(String name_pet) {
        this.name_pet = name_pet;
    }

    public int getId_mascota() {
        return id_mascota;
    }

    public void setId_mascota(int id_mascota) {
        this.id_mascota = id_mascota;
    }

    public String getName_tag() {
        return name_tag;
    }

    public void setName_tag(String name_tag) {
        this.name_tag = name_tag;
    }

    public SearcRecentBean(Cursor cursor) {
        if (cursor.getColumnIndex(SearchResentHelper.ID_USUARIO) != -1)
            this.id_usuario =cursor.getInt(cursor.getColumnIndex(SearchResentHelper.ID_USUARIO));

        if (cursor.getColumnIndex(SearchResentHelper.UUID_USUARIO) != -1)
            this.uuid_usuario = cursor.getString(cursor.getColumnIndex(SearchResentHelper.UUID_USUARIO));

        if (cursor.getColumnIndex(SearchResentHelper.NAME) != -1)
            this.name = cursor.getString(cursor.getColumnIndex(SearchResentHelper.NAME));

        if (cursor.getColumnIndex(SearchResentHelper.RAZA_MASCOTA) != -1)
            this.name_raza = cursor.getString(cursor.getColumnIndex(SearchResentHelper.RAZA_MASCOTA));

        if (cursor.getColumnIndex(SearchResentHelper.URL) != -1)
            this.url_photo = cursor.getString(cursor.getColumnIndex(SearchResentHelper.URL));

        if (cursor.getColumnIndex(SearchResentHelper.TYPE_PET) != -1)
            this.type_mascota = cursor.getInt(cursor.getColumnIndex(SearchResentHelper.TYPE_PET));

        if (cursor.getColumnIndex(SearchResentHelper.TYPE_SAVE) != -1)
            this.type_save = cursor.getInt(cursor.getColumnIndex(SearchResentHelper.TYPE_SAVE));

        if (cursor.getColumnIndex(SearchResentHelper.NAME_PET) != -1)
            this.name_pet = cursor.getString(cursor.getColumnIndex(SearchResentHelper.NAME_PET));

        if (cursor.getColumnIndex(SearchResentHelper.ID_MASCOTA) != -1)
            this.id_mascota = cursor.getInt(cursor.getColumnIndex(SearchResentHelper.ID_MASCOTA));

        if (cursor.getColumnIndex(SearchResentHelper.NAME_TAG) != -1)
            this.name_tag = cursor.getString(cursor.getColumnIndex(SearchResentHelper.NAME_TAG));

    }


    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SearchResentHelper.ID_USUARIO, getId_usuario());
        contentValues.put(SearchResentHelper.UUID_USUARIO, getUuid_usuario());
        contentValues.put(SearchResentHelper.NAME, getName());
        contentValues.put(SearchResentHelper.URL, getUrl_photo());
        contentValues.put(SearchResentHelper.TYPE_PET, getType_mascota());
        contentValues.put(SearchResentHelper.TYPE_SAVE, getType_save());
        contentValues.put(SearchResentHelper.RAZA_MASCOTA, getName_raza());

        return contentValues;
    }
}



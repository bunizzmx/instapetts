package com.bunizz.instapetts.beans;

import android.content.ContentValues;
import android.database.Cursor;

import com.bunizz.instapetts.db.helpers.PetHelper;

public class PetBean {

    String name_pet;
    String raza_pet;
    String peso_pet;
    String color_pet;
    String descripcion_pet;
    String genero_pet;
    String rate_pet;
    String name_propietary;
    String id_propietary;
    String id_pet;
    String edad_pet;
    String url_photo;
    int type_pet;


    public PetBean() {
    }

    public PetBean(String name_pet, String raza_pet, String peso_pet, String color_pet, String descripcion_pet, String genero_pet, String rate_pet, String name_propietary, String id_propietary, String id_pet, int type_pet) {
        this.name_pet = name_pet;
        this.raza_pet = raza_pet;
        this.peso_pet = peso_pet;
        this.color_pet = color_pet;
        this.descripcion_pet = descripcion_pet;
        this.genero_pet = genero_pet;
        this.rate_pet = rate_pet;
        this.name_propietary = name_propietary;
        this.id_propietary = id_propietary;
        this.id_pet = id_pet;
        this.type_pet = type_pet;
    }

    public String getName_pet() {
        return name_pet;
    }

    public void setName_pet(String name_pet) {
        this.name_pet = name_pet;
    }

    public String getRaza_pet() {
        return raza_pet;
    }

    public void setRaza_pet(String raza_pet) {
        this.raza_pet = raza_pet;
    }

    public String getPeso_pet() {
        return peso_pet;
    }

    public void setPeso_pet(String peso_pet) {
        this.peso_pet = peso_pet;
    }

    public String getColor_pet() {
        return color_pet;
    }

    public void setColor_pet(String color_pet) {
        this.color_pet = color_pet;
    }

    public String getDescripcion_pet() {
        return descripcion_pet;
    }

    public void setDescripcion_pet(String descripcion_pet) {
        this.descripcion_pet = descripcion_pet;
    }

    public String getGenero_pet() {
        return genero_pet;
    }

    public void setGenero_pet(String genero_pet) {
        this.genero_pet = genero_pet;
    }

    public String getRate_pet() {
        return rate_pet;
    }

    public void setRate_pet(String rate_pet) {
        this.rate_pet = rate_pet;
    }

    public String getName_propietary() {
        return name_propietary;
    }

    public void setName_propietary(String name_propietary) {
        this.name_propietary = name_propietary;
    }

    public String getId_propietary() {
        return id_propietary;
    }

    public void setId_propietary(String id_propietary) {
        this.id_propietary = id_propietary;
    }

    public String getId_pet() {
        return id_pet;
    }

    public void setId_pet(String id_pet) {
        this.id_pet = id_pet;
    }

    public int getType_pet() {
        return type_pet;
    }

    public void setType_pet(int type_pet) {
        this.type_pet = type_pet;
    }

    public String getEdad_pet() {
        return edad_pet;
    }

    public void setEdad_pet(String edad_pet) {
        this.edad_pet = edad_pet;
    }

    public String getUrl_photo() {
        return url_photo;
    }

    public void setUrl_photo(String url_photo) {
        this.url_photo = url_photo;
    }

    public ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PetHelper.ID_PET, getId_pet());
        contentValues.put(PetHelper.NAME_PET, getName_pet());
        contentValues.put(PetHelper.ID_PROPIETARY, getId_propietary());
        contentValues.put(PetHelper.NAME_PROPIETARY, getName_propietary());
        contentValues.put(PetHelper.PESO_PET, getPeso_pet());
        contentValues.put(PetHelper.RAZA_PET, getRaza_pet());
        contentValues.put(PetHelper.TYPE_PET, getType_pet());
        contentValues.put(PetHelper.COLOR_PET, getColor_pet());
        contentValues.put(PetHelper.GENERO_PET, getGenero_pet());
        contentValues.put(PetHelper.DESCRIPCION_PET, getDescripcion_pet());
        contentValues.put(PetHelper.EDAD_PET, getEdad_pet());
        contentValues.put(PetHelper.RATING_PET, getRaza_pet());
        contentValues.put(PetHelper.URL_PHOTO, getUrl_photo());
        return contentValues;
    }

    public PetBean(Cursor cursor) {
        if (cursor.getColumnIndex(PetHelper.ID_PET) != -1)
            this.id_pet = cursor.getString(cursor.getColumnIndex(PetHelper.ID_PET));

        if (cursor.getColumnIndex(PetHelper.NAME_PET) != -1)
            this.name_pet = cursor.getString(cursor.getColumnIndex(PetHelper.NAME_PET));

        if (cursor.getColumnIndex(PetHelper.ID_PROPIETARY) != -1)
            this.id_propietary = cursor.getString(cursor.getColumnIndex(PetHelper.ID_PROPIETARY));

        if (cursor.getColumnIndex(PetHelper.NAME_PROPIETARY) != -1)
            this.name_propietary = cursor.getString(cursor.getColumnIndex(PetHelper.NAME_PROPIETARY));

        if (cursor.getColumnIndex(PetHelper.PESO_PET) != -1)
            this.peso_pet = cursor.getString(cursor.getColumnIndex(PetHelper.PESO_PET));

        if (cursor.getColumnIndex(PetHelper.RAZA_PET) != -1)
            this.raza_pet = cursor.getString(cursor.getColumnIndex(PetHelper.RAZA_PET));

        if (cursor.getColumnIndex(PetHelper.TYPE_PET) != -1)
            this.type_pet = cursor.getInt(cursor.getColumnIndex(PetHelper.TYPE_PET));

        if (cursor.getColumnIndex(PetHelper.COLOR_PET) != -1)
            this.color_pet = cursor.getString(cursor.getColumnIndex(PetHelper.COLOR_PET));

        if (cursor.getColumnIndex(PetHelper.GENERO_PET) != -1)
            this.genero_pet = cursor.getString(cursor.getColumnIndex(PetHelper.GENERO_PET));

        if (cursor.getColumnIndex(PetHelper.DESCRIPCION_PET) != -1)
            this.descripcion_pet = cursor.getString(cursor.getColumnIndex(PetHelper.DESCRIPCION_PET));

        if (cursor.getColumnIndex(PetHelper.EDAD_PET) != -1)
            this.edad_pet = cursor.getString(cursor.getColumnIndex(PetHelper.EDAD_PET));

        if (cursor.getColumnIndex(PetHelper.RATING_PET) != -1)
            this.rate_pet = cursor.getString(cursor.getColumnIndex(PetHelper.RATING_PET));

        if (cursor.getColumnIndex(PetHelper.URL_PHOTO) != -1)
            this.url_photo = cursor.getString(cursor.getColumnIndex(PetHelper.URL_PHOTO));


    }
}

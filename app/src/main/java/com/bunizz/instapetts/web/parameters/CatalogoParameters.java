package com.bunizz.instapetts.web.parameters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CatalogoParameters {
    @SerializedName("id_raza")
    @Expose
    int id_raza;

    @SerializedName("idioma")
    @Expose
    String idioma;

    public CatalogoParameters() {
    }

    public CatalogoParameters(int id_raza, String idioma) {
        this.id_raza = id_raza;
        this.idioma = idioma;
    }

    public int getId_raza() {
        return id_raza;
    }

    public void setId_raza(int id_raza) {
        this.id_raza = id_raza;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }
}

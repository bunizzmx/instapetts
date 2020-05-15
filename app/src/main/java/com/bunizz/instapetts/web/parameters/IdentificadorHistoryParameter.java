package com.bunizz.instapetts.web.parameters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IdentificadorHistoryParameter {

    @SerializedName("target")
    @Expose
    String target;

    @SerializedName("id_usuario")
    @Expose
    int id_usuario;

    @SerializedName("identificador")
    @Expose
    String identificador;

    public IdentificadorHistoryParameter() {
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public IdentificadorHistoryParameter(String target, int id_usuario, String identificador) {
        this.target = target;
        this.id_usuario = id_usuario;
        this.identificador = identificador;
    }
}

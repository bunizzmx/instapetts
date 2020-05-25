package com.bunizz.instapetts.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportBean {

    @SerializedName("id_usuario")
    @Expose
    int id_usuario;
    @SerializedName("id_motivo")
    @Expose
    int id_motivo;
    @SerializedName("id_recurso")
    @Expose
    int id_recurso;
    @SerializedName("tipo_recurso")
    @Expose
    int tipo_recurso;
    @SerializedName("motivo")
    @Expose
    String motivo;
    @SerializedName("descripcion")
    @Expose
    String descripcion;

    @SerializedName("target")
    @Expose
    String target;

    public ReportBean() {
    }

    public ReportBean(int id_usuario, int id_motivo, int id_recurso, int tipo_recurso, String motivo, String descripcion) {
        this.id_usuario = id_usuario;
        this.id_motivo = id_motivo;
        this.id_recurso = id_recurso;
        this.tipo_recurso = tipo_recurso;
        this.motivo = motivo;
        this.descripcion = descripcion;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_motivo() {
        return id_motivo;
    }

    public void setId_motivo(int id_motivo) {
        this.id_motivo = id_motivo;
    }

    public int getId_recurso() {
        return id_recurso;
    }

    public void setId_recurso(int id_recurso) {
        this.id_recurso = id_recurso;
    }

    public int getType_recurso() {
        return tipo_recurso;
    }

    public void setType_recurso(int type_recurso) {
        this.tipo_recurso = type_recurso;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}

package com.bunizz.instapetts.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class PlayVideos {
    @SerializedName("id")
    @Expose
    int id;

    @SerializedName("descripcion")
    @Expose
    String descripcion;

    @SerializedName("titulo")
    @Expose
    String titulo;

    @SerializedName("url_video")
    @Expose
    String url_video;

    @SerializedName("url_tumbh")
    @Expose
    String url_tumbh;

    @SerializedName("duracion")
    @Expose
    int duracion;

    @SerializedName("aspecto")
    @Expose
    String aspecto;

    @SerializedName("ancho")
    @Expose
    int ancho;

    @SerializedName("alto")
    @Expose
    int alto;

    public PlayVideos() {
    }

    public PlayVideos(int id, String descripcion, String titulo, String url_video, String url_tumbh, int duracion, String aspecto, int ancho, int alto) {
        this.id = id;
        this.descripcion = descripcion;
        this.titulo = titulo;
        this.url_video = url_video;
        this.url_tumbh = url_tumbh;
        this.duracion = duracion;
        this.aspecto = aspecto;
        this.ancho = ancho;
        this.alto = alto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrl_video() {
        return url_video;
    }

    public void setUrl_video(String url_video) {
        this.url_video = url_video;
    }

    public String getUrl_tumbh() {
        return url_tumbh;
    }

    public void setUrl_tumbh(String url_tumbh) {
        this.url_tumbh = url_tumbh;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getAspecto() {
        return aspecto;
    }

    public void setAspecto(String aspecto) {
        this.aspecto = aspecto;
    }

    public int getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public int getAlto() {
        return alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }
}

package com.bunizz.instapetts.beans;


import org.parceler.Parcel;

@Parcel
public class NotificationBeanFirestore {
    String FOTO_REMITENTE;
    int ID_RECURSO;
    int ID_REMITENTE;
    String NAME_REMITENTE;
    String URL_EXTRA;
    int TYPE_NOTIFICATION;
    String FECHA;
    String ID_DOCUMENT_NOTIFICATION;

    public NotificationBeanFirestore() {
    }

    public NotificationBeanFirestore(String FOTO_REMITENTE, int ID_RECURSO, int ID_REMITENTE, String NAME_REMITENTE, String URL_EXTRA, int TYPE_NOTIFICATION,String FECHA) {
        this.FOTO_REMITENTE = FOTO_REMITENTE;
        this.ID_RECURSO = ID_RECURSO;
        this.ID_REMITENTE = ID_REMITENTE;
        this.NAME_REMITENTE = NAME_REMITENTE;
        this.URL_EXTRA = URL_EXTRA;
        this.FECHA =FECHA;
        this.TYPE_NOTIFICATION = TYPE_NOTIFICATION;
    }

    public String getFECHA() {
        return FECHA;
    }

    public void setFECHA(String FECHA) {
        this.FECHA = FECHA;
    }

    public String getFOTO_REMITENTE() {
        return FOTO_REMITENTE;
    }

    public void setFOTO_REMITENTE(String FOTO_REMITENTE) {
        this.FOTO_REMITENTE = FOTO_REMITENTE;
    }

    public int getID_RECURSO() {
        return ID_RECURSO;
    }

    public void setID_RECURSO(int ID_RECURSO) {
        this.ID_RECURSO = ID_RECURSO;
    }

    public int getID_REMITENTE() {
        return ID_REMITENTE;
    }

    public void setID_REMITENTE(int ID_REMITENTE) {
        this.ID_REMITENTE = ID_REMITENTE;
    }

    public String getNAME_REMITENTE() {
        return NAME_REMITENTE;
    }

    public void setNAME_REMITENTE(String NAME_REMITENTE) {
        this.NAME_REMITENTE = NAME_REMITENTE;
    }

    public String getURL_EXTRA() {
        return URL_EXTRA;
    }

    public void setURL_EXTRA(String URL_EXTRA) {
        this.URL_EXTRA = URL_EXTRA;
    }

    public int getTYPE_NOTIFICATION() {
        return TYPE_NOTIFICATION;
    }

    public void setTYPE_NOTIFICATION(int TYPE_NOTIFICATION) {
        this.TYPE_NOTIFICATION = TYPE_NOTIFICATION;
    }

    public String getID_DOCUMENT_NOTIFICATION() {
        return ID_DOCUMENT_NOTIFICATION;
    }

    public void setID_DOCUMENT_NOTIFICATION(String ID_DOCUMENT_NOTIFICATION) {
        this.ID_DOCUMENT_NOTIFICATION = ID_DOCUMENT_NOTIFICATION;
    }
}

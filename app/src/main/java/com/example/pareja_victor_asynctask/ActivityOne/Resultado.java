package com.example.pareja_victor_asynctask.ActivityOne;

import android.graphics.Bitmap;
import android.media.Image;

public class Resultado {
    private boolean isImage;
    private int codigo; //indica el código de estado devuelto por el servidor web
    private String mensaje; //información del error
    private String contenido; //fichero descargado
    private Bitmap image;

    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
    }


    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }


    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}

package com.example.pareja_victor_asynctask.ActivityOne;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Conexion {

    public static Resultado conectarJava(URL url) throws IOException {
        HttpURLConnection urlConnection = null;

        int responseCode = 500;
        Resultado resultado = new Resultado();

        urlConnection = (HttpURLConnection) url.openConnection();
        responseCode = urlConnection.getResponseCode();
        resultado.setCodigo(responseCode);

        String contentType = urlConnection.getHeaderField("Content-Type");
        boolean image = contentType.startsWith("image/");

        if (responseCode == HttpURLConnection.HTTP_OK) {
            if (image){
                resultado.setImage(true);
                resultado.setImage(getBitmap(urlConnection.getInputStream()));
            } else resultado.setContenido(leer(urlConnection.getInputStream()));
        } else {
            resultado.setMensaje("Error en el acceso a la web: " + String.valueOf(responseCode));
        }
        urlConnection.disconnect();

        return resultado;
    }

    private static String leer(InputStream inputStream) throws IOException{
        BufferedReader in;
        String linea;
        StringBuilder miCadena = new StringBuilder();

        in = new BufferedReader((new InputStreamReader(inputStream)), 32000);
        while ( (linea = in.readLine()) != null) {
            miCadena.append(linea);
        }
        in.close();

        return  miCadena.toString();
    }

    public static Bitmap getBitmap(InputStream inputStream) throws IOException {
        Bitmap bmp = BitmapFactory.decodeStream(inputStream);
        return bmp;
    }
}

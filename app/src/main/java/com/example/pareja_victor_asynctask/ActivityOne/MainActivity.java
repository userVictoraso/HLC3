package com.example.pareja_victor_asynctask.ActivityOne;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.pareja_victor_asynctask.R;
import com.example.pareja_victor_asynctask.databinding.ActivityMain1Binding;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    AsyncTaskClass asyncTaskClass;
    private ActivityMain1Binding binding;
    long start, end;
    URL url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        binding = ActivityMain1Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.downloadButton.setOnClickListener(this);
        binding.URLEditText.setText("https://images1.autocasion.com/unsafe/640x480/actualidad/wp-content/uploads/2016/07/q7_aper.jpg");
        binding.webView.setVisibility(View.INVISIBLE);
        binding.URLEditText.requestFocus();

        binding.URLEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    Toast.makeText(getApplicationContext(), "Got the focus", Toast.LENGTH_LONG).show();
                    binding.webView.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "Lost the focus", Toast.LENGTH_LONG).show();
                    binding.webView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            binding.URLEditText.clearFocus();
            url = new URL(binding.URLEditText.getText().toString());
            descarga(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            mostrarMensaje(e.getMessage());
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    private void descarga(URL url) {
        start = System.currentTimeMillis();
        asyncTaskClass = new AsyncTaskClass(this);
        asyncTaskClass.execute(url);
    }

    private void mostrarMensaje(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    //ASYNC TASK CLASS
    public class AsyncTaskClass extends AsyncTask<URL, Void, Resultado> {
        private ProgressDialog progreso;
        private Context context;

        public AsyncTaskClass(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progreso = new ProgressDialog(context);
            progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progreso.setMessage("Conectando...");
            progreso.setCancelable(true);
            progreso.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    AsyncTaskClass.this.cancel(true);
                }
            });
            progreso.show();
        }

        @Override
        protected Resultado doInBackground(URL... urls) {
            Resultado resultado;
            try {
                resultado = Conexion.conectarJava(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Error de conexión: ", e.getMessage());
                resultado = new Resultado();
                resultado.setCodigo(500);
                resultado.setMensaje("Error de conexión: " + e.getMessage());
            }

            return resultado;
        }

        @Override
        protected void onPostExecute(Resultado resultado) {
            super.onPostExecute(resultado);
            progreso.dismiss();
            end = System.currentTimeMillis();
            binding.webView.setVisibility(View.VISIBLE);
            if (resultado.getCodigo() == HttpURLConnection.HTTP_OK) {
                if (resultado.isImage()){
                    binding.imageView.setVisibility(View.VISIBLE);
                    binding.webView.setVisibility(View.INVISIBLE);
                    binding.imageView.setImageBitmap(resultado.getImage());
                } else {
                    binding.imageView.setVisibility(View.INVISIBLE);
                    binding.webView.loadDataWithBaseURL(String.valueOf(url), resultado.getContenido(), "text/html", "UTF-8", null);
                }
                mostrarMensaje("Descarga finalizada.");
            } else {
                mostrarMensaje(resultado.getMensaje());
                binding.webView.loadDataWithBaseURL(String.valueOf(url), resultado.getMensaje(), "text/html", "UTF-8", null);
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            progreso.dismiss();
            mostrarMensaje("Cancelado");
        }
    }
}
package com.example.pareja_victor_asynctask.ActivityTwo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;

import com.example.pareja_victor_asynctask.databinding.ActivityMain2Binding;

import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    final String COIN_REPOSITORY = "https://dam.org.es/ficheros/cambio.txt";
    double dolarValue;
    private TextWatcher tv1;
    private TextWatcher tv2;
    ActivityMain2Binding binding;
    DecimalFormat df = new DecimalFormat("#.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //OBTENER INFO
        getInformation();

        binding.switchMoneda.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.editTextDolares.setEnabled(true);
                    binding.editTextEuro.setEnabled(false);
                    limpiarCampos();
                } else {
                    binding.editTextEuro.setEnabled(true);
                    binding.editTextDolares.setEnabled(false);
                    limpiarCampos();
                }
            }
        });
        comprobarDolarAEuro();
        comprobarEuroADolar();
        binding.editTextEuro.addTextChangedListener(tv1);
    }

    public void limpiarCampos() {
        binding.editTextDolares.removeTextChangedListener(tv2);
        binding.editTextEuro.removeTextChangedListener(tv1);
        binding.editTextDolares.setText("");
        binding.editTextEuro.setText("");
        binding.editTextDolares.addTextChangedListener(tv2);
        binding.editTextEuro.addTextChangedListener(tv1);
    }

    public void comprobarEuroADolar() {
        tv1 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (binding.editTextEuro.toString().length() < 1) {
                        limpiarCampos();
                    }
                    double resultado = Double.valueOf(binding.editTextEuro.getText().toString()) * dolarValue;
                    System.out.println(resultado);
                    binding.editTextDolares.removeTextChangedListener(tv2);
                    binding.editTextDolares.setText(String.valueOf(df.format(resultado)) + " $");
                    binding.editTextDolares.addTextChangedListener(tv2);
                } catch (NumberFormatException e) {
                    limpiarCampos();
                }
            }
        };
    }

    public void comprobarDolarAEuro() {
        tv2 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (binding.editTextDolares.toString().length() == 0) {
                        limpiarCampos();
                    }
                    double resultado = Double.valueOf(binding.editTextDolares.getText().toString()) / dolarValue;
                    binding.editTextEuro.removeTextChangedListener(tv1);
                    binding.editTextEuro.setText(String.valueOf(df.format(resultado)) + " €");
                    binding.editTextEuro.addTextChangedListener(tv1);
                } catch (NumberFormatException e) {
                    limpiarCampos();
                }
            }
        };
    }

    public void getInformation(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(COIN_REPOSITORY).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String myResponse = response.body().string();
                    String myResponseFixed = myResponse.replaceAll(",", ".");
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                dolarValue = Double.parseDouble(myResponseFixed);
                            } catch (NumberFormatException n){
                                n.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }
}
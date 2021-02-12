package com.example.pareja_victor_asynctask;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.pareja_victor_asynctask.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ejecutarEjercicioUno();
        ejecutarEjercicioDos();
    }

    public void ejecutarEjercicioUno(){
        binding.buttonEjercicio1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, com.example.pareja_victor_asynctask.ActivityOne.MainActivity.class));
            }
        });
    }

    public void ejecutarEjercicioDos(){
        binding.buttonEjercicio2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, com.example.pareja_victor_asynctask.ActivityTwo.MainActivity.class));
            }
        });
    }
}